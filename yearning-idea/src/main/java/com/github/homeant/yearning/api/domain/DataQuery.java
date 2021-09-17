package com.github.homeant.yearning.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataQuery {
    @JsonProperty("data_base")
    private String dataBase;

    private String source;

    private String sql;
}
