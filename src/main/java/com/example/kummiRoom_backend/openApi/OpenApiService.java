package com.example.kummiRoom_backend.openApi;

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
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("https://open.neis.go.kr/hub/hisTimetable")
    private String server1BaseUrl;

    @Value("a3f4664fd08e4aaf8253d94322698bc3")
    private String openApiKey;

    public List<HisTimetableRow> getTimeTable(NeisTimetableRequestDto req) throws Exception {

        String url = UriComponentsBuilder.fromHttpUrl(server1BaseUrl)
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

}
