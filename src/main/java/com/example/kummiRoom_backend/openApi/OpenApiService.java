package com.example.kummiRoom_backend.openApi;

import com.example.kummiRoom_backend.api.entity.School;
import com.example.kummiRoom_backend.openApi.dto.requestDto.GetSchoolRequestDto;
import com.example.kummiRoom_backend.openApi.dto.requestDto.NeisTimetableRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OpenApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SchoolRepository schoolRepository;

    @Value("https://open.neis.go.kr/hub/hisTimetable")
    private String testBaseUrl;

    @Value("https://open.neis.go.kr/hub/schoolInfo")
    private String getSchoolBaseUrl;

    @Value("a3f4664fd08e4aaf8253d94322698bc3")
    private String openApiKey;

    public List<HisTimetableRow> getTimeTable(NeisTimetableRequestDto req) throws Exception {

        String url = UriComponentsBuilder.fromHttpUrl(testBaseUrl)
                .queryParam("KEY", openApiKey)
                .queryParam("Type", "json")
                .queryParam("ATPT_OFCDC_SC_CODE", req.getAtptOfcdcScCode())
                .queryParam("SD_SCHUL_CODE", req.getSdSchulCode())
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

            List<HisTimetableRow> schoolList = new ArrayList<>();

            if (rowArray.isArray()) {
                for (JsonNode node : rowArray) {
                    String name = node.path("SCHUL_NM").asText();
                    HisTimetableRow row = new HisTimetableRow();
                    row.setSchoolName(name);
                    schoolList.add(row);
                }
            }

            return schoolList;
        } else {
            throw new RuntimeException("NEIS API 호출 실패");
        }
    }

    public void getSchoolTable(GetSchoolRequestDto req) throws Exception {

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

            List<HisTimetableRow> schoolList = new ArrayList<>();

            if (rowArray.isArray()) {
                //저장 함수
                saveSchoolsFromApi(rowArray);
            }

        } else {
            throw new RuntimeException("NEIS API 호출 실패");
        }
    }

    public void saveSchoolsFromApi(JsonNode rowArray) {
        List<School> schoolEntities = new ArrayList<>();


        for (JsonNode node : rowArray) {
            String schoolId = node.path("SD_SCHUL_CODE").asText();

            // DB에 이미 있는지 확인
            Optional<School> existing = schoolRepository.findById(schoolId);
            School school = School.builder()
                    .schul_code(node.path("SCHUL_KND_SC_NM").asText())
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
        System.out.println("[DEBUG] saveSchoolsFromApi: " + schoolEntities);

        schoolRepository.saveAll(schoolEntities);
    }

}
