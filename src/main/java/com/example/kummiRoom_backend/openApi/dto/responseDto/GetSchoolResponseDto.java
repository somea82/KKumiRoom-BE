package com.example.kummiRoom_backend.openApi.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetSchoolResponseDto {
    @JsonProperty("SD_SCHUL_CODE")  // 학교 ID
    private String schoolId;

    @JsonProperty("SCHUL_NM")       // 학교 이름
    private String schoolName;

    @JsonProperty("ORG_RDNMA")      // 주소
    private String address;

    @JsonProperty("ATPT_OFCDC_SC_CODE") // 교육청 ID
    private String officeEduCode;

    @JsonProperty("HS_SC_NM")       // 학교 계열
    private String schoolType;

    @JsonProperty("HMPG_ADRES")     // 홈페이지 주소
    private String homepage;
}
