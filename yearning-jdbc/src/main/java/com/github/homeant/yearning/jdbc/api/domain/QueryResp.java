package com.github.homeant.yearning.jdbc.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QueryResp {

    @JsonProperty("data")
    private List<Map<String, Object>> list;

    private Long total;

    @JsonProperty("title")
    private List<Column> columns;

    @JsonProperty("status")
    private boolean status;

    @Data
    public static class Column {
        private String fixed;

        private String key;

        private String title;

        private String width;
    }
}
