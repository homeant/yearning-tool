package com.github.homeant.yearning.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubmitWorkOrder {
    private String assigned;
    private Integer backup;
    @JsonProperty("data_base")
    private String dataBase;
    private String delay;
    private Integer export;
    private String idc;
    @JsonProperty("real_name")
    private String realName;
    @JsonProperty("source")
    private String dataSource;
    private String sql;
    private String table;
    private String text;
    private Integer tp;
    private Integer type;
}
