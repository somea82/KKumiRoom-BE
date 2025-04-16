package com.example.kummiRoom_backend.openApi.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeisTimetableRequestDto {
    @JsonProperty("ATPT_OFCDC_SC_CODE")
    private String atptOfcdcScCode;
    @JsonProperty("SD_SCHUL_CODE")
    private String sdSchulCode;
//    private String grade;
//    private String classNm;
//    private String ay;
//    private String sem;
//    private String fromYmd;
//    private String toYmd;
}
