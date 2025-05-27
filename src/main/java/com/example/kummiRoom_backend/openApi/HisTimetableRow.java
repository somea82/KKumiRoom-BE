package com.example.kummiRoom_backend.openApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HisTimetableRow {
    @JsonProperty("SCHUL_NM")
    private String schoolName;
}
