package com.example.kummiRoom_backend.openApi.dto.requestDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetSchoolRequestDto {
    @JsonProperty("ATPT_OFCDC_SC_CODE")
    private String atptOfcdcScCode;
}
