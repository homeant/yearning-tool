package com.github.homeant.yearning.jdbc.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Query {
    @JsonProperty("data_base")
    private String dataBase;

    private String source;

    private String sql;
}
