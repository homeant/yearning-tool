package com.github.homeant.yearning.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestWorkOrder {
    @JsonProperty("data_base")
    private String dataBase;

    @JsonProperty("is_dml")
    private Boolean isDml;

    private String source;

    private String sql;
}
