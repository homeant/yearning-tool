package com.github.homeant.yearning.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataBaseResp {
    @JsonProperty("admin")
    private List<String> adminList;

    @JsonProperty("results")
    private List<String> dataBaseList;
}
