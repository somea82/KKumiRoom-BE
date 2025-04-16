package com.example.kummiRoom_backend.openApi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/openai")
public class OpenApiController {
    private final OpenApiService openApiService;

    @Value("https://open.neis.go.kr/hub/hisTimetable")
    private String server1BaseUrl;

    @Value("a3f4664fd08e4aaf8253d94322698bc3")
    private String openApiKey;

    @PostMapping("/load")
    public ResponseEntity<?> getTimetable(@RequestBody NeisTimetableRequestDto request) {
        try {
            return ResponseEntity.ok(openApiService.getTimeTable(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}