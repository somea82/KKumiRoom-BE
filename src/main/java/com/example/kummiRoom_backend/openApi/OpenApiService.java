package com.example.kummiRoom_backend.openApi;

import com.example.kummiRoom_backend.api.entity.Course;
import com.example.kummiRoom_backend.api.entity.School;
import com.example.kummiRoom_backend.api.repository.CourseRepository;
import com.example.kummiRoom_backend.openApi.dto.requestDto.GetSchoolRequestDto;
import com.example.kummiRoom_backend.openApi.dto.requestDto.NeisTimetableRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SchoolRepository schoolRepository;
    private final CourseRepository courseRepository;

    @Value("https://open.neis.go.kr/hub/hisTimetable")
    private String testBaseUrl;

    @Value("https://open.neis.go.kr/hub/schoolInfo")
    private String getSchoolBaseUrl;

    @Value("${openApi.Key}")
    private String openApiKey;


    public List<School> getSchoolTable(GetSchoolRequestDto req) throws Exception {

        //demo 버전 : url은 하드 코딩 된 상태 ( 서울시 고등학교 기준 )
        String url = getSchoolBaseUrl
                + "?KEY=" + openApiKey
                + "&Type=json"
                + "&ATPT_OFCDC_SC_CODE=" + req.getAtptOfcdcScCode()
                + "&SCHUL_KND_SC_NM=고등학교"
                + "&pSize=400";


        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class
        );
        System.out.println("[DEBUG] getSchool 요청: " + url);
        System.out.println("[DEBUG] getSchool 응답: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            JsonNode rowArray = root.path("schoolInfo").get(1).path("row");
            System.out.println("[DEBUG] JSON 응답: " + rowArray);

            if (rowArray.isArray()) {
                return saveSchoolsFromApi(rowArray);
            }

        }
        throw new RuntimeException("NEIS API 호출 실패");
    }

    public List<Course> getCourseFromTimeTable(NeisTimetableRequestDto req) throws Exception {
        Set<String> existingCourseKeys = new HashSet<>();
        Set<String> courseNameSet = new HashSet<>();
        List<Course> courseList = new ArrayList<>();

        // 필수/선택 판별을 위한 Map
        Map<String, Set<Long>> gradeToClassSet = new HashMap<>();
        Map<String, Set<Long>> courseToClassSet = new HashMap<>();
        Map<String, JsonNode> courseToNodeMap = new HashMap<>();

        // 기존 Course를 한 번에 조회해서 중복 확인용 set 만들기
        List<Course> existingCourses = courseRepository.findAllBySchool_SchoolId(req.getSdSchulCode());
        for (Course course : existingCourses) {
            String key = course.getCourseName() + "_" + course.getSemester();
            existingCourseKeys.add(key);
        }

        for (int i = 1; i <= 2; i++) { // pindex 1과 2 두 번 반복
            String url = UriComponentsBuilder.fromHttpUrl(testBaseUrl)
                    .queryParam("KEY", openApiKey)
                    .queryParam("Type", "json")
                    .queryParam("ATPT_OFCDC_SC_CODE", req.getAtptOfcdcScCode())
                    .queryParam("SD_SCHUL_CODE", req.getSdSchulCode())
                    .queryParam("TI_FROM_YMD", "20250407")
                    .queryParam("TI_TO_YMD", "20250411")
                    .queryParam("pSize", "750")
                    .queryParam("pindex", i)
                    .toUriString();

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    String.class
            );


            System.out.println("[DEBUG] NEIS 요청: " + url);
            System.out.println("[DEBUG] NEIS 응답: " + response.getBody());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());

                JsonNode rowArray = root.path("hisTimetable").get(1).path("row");
                System.out.println("[DEBUG] JSON 응답: " + rowArray);

                if (rowArray.isArray()) {
                    for (JsonNode node : rowArray) {
                        String courseName = node.path("ITRT_CNTNT").asText();
                        String grade = node.path("GRADE").asText();
                        String className = node.path("ITRT_CNTNT").asText();
                        String semester = node.path("GRADE").asText() + "학년 " + node.path("SEM").asText() + "학기";
                        Long classNm = node.path("CLASS_NM").asLong();

                        String courseKey = courseName + "_" + grade;

                        String key = courseName + "_" + semester;

                        // 반별 과목 개설 정보 수집
                        gradeToClassSet.computeIfAbsent(grade, k -> new HashSet<>()).add(classNm);
                        //과목별 개설 반 정보 수집
                        courseToClassSet.computeIfAbsent(courseKey, k -> new HashSet<>()).add(classNm);

                        if (!courseToNodeMap.containsKey(courseKey)) {
                            courseToNodeMap.put(courseKey, node);
                        }


                        if (existingCourseKeys.contains(key)) continue; // 중복 → 스킵
                        if (courseNameSet.contains(courseKey)) continue;
                        courseNameSet.add(courseKey);
                        System.out.println(courseNameSet);
                    }
                }
            } else {
                throw new RuntimeException("NEIS API 호출 실패");
            }
        }

        for (String courseKey : courseNameSet) {
            JsonNode node = courseToNodeMap.get(courseKey);

            String courseName = node.path("ITRT_CNTNT").asText();
            String grade = node.path("GRADE").asText();
            String semester = grade + "학년 " + node.path("SEM").asText() + "학기";

            String courseTcourseTypeype = determineCourseType(courseKey, grade, gradeToClassSet, courseToClassSet);


            Course course = Course.builder()
                    .school(schoolRepository.findBySchoolId(node.path("SD_SCHUL_CODE").asLong()))
                    .courseName(courseName)
                    .courseType(courseTcourseTypeype)
                    .courseArea(node.path("ORD_SC_NM").asText()) // 예: 일반계
                    .semester(node.path("GRADE").asText() + "학년 " + node.path("SEM").asText() + "학기") // 예: 1학년 1학기
                    .description(node.path("DGHT_CRSE_SC_NM").asText())
                    .updatedAt(LocalDateTime.now())
                    .maxStudents(0) // 임의 초기값
                    .build();
            courseList.add(course);
        }

        return courseRepository.saveAll(courseList);
    }


    private String determineCourseType(String courseKey, String grade,
                                       Map<String, Set<Long>> gradeToClassSet,
                                       Map<String, Set<Long>> courseToClassSet) {
        Set<Long> allClasses = gradeToClassSet.get(grade);
        Set<Long> offeringClasses = courseToClassSet.get(courseKey);

        if (allClasses != null && offeringClasses != null && allClasses.equals(offeringClasses)) {
            return "필수";
        }
        return "선택";
    }

    public List<School> saveSchoolsFromApi(JsonNode rowArray) {
        List<School> schoolEntities = new ArrayList<>();


        for (JsonNode node : rowArray) {

            // DB에 이미 있는지 확인
            Optional<School> existing = schoolRepository.findBySchoolName(node.path("SCHUL_NM").asText());
            School school = School.builder()
                    .schoolId(Long.valueOf(node.path("SD_SCHUL_CODE").asText()))
                    .schoolName(node.path("SCHUL_NM").asText())
                    .address(node.path("ORG_RDNMA").asText())
                    .eduId(node.path("ATPT_OFCDC_SC_CODE").asText())
                    .schoolType(node.path("SCHUL_KND_SC_NM").asText())
                    .homepage(node.path("HMPG_ADRES").asText())
                    .build();

            if (existing.isPresent()) {
                // 업데이트 로직: 필드 덮어쓰기 또는 유지
                School existSchool = existing.get();
                existSchool.setSchoolName(school.getSchoolName());
                existSchool.setAddress(school.getAddress());
                existSchool.setEduId(school.getEduId());
                existSchool.setSchoolType(school.getSchoolType());
                existSchool.setHomepage(school.getHomepage());
                schoolEntities.add(existSchool);
            } else {
                // 신규 등록
                schoolEntities.add(school);
            }
        }

        return schoolRepository.saveAll(schoolEntities);
    }

}
