package com.github.homeant.yearning.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TestWorkOrderResp {

    @JsonProperty("Backup")
    private String backup;

    @JsonProperty("InsulateWordList")
    private String insulateWordList;

    @JsonProperty("IsOSCAllow")
    private Boolean isOSCAllow;

    @JsonProperty("IsOk")
    private Boolean isOk;

    @JsonProperty("Mark")
    private Boolean mark;

    @JsonProperty("Start")
    private Object start;
    @JsonProperty("Stop")
    private Object stop;


    @JsonProperty("Table")
    private String table;

    @JsonProperty("TableInfo")
    private Object tableInfo;

    @JsonProperty("TableList")
    private Object tableList;


    @JsonProperty("TableSize")
    private Long tableSize;

    @JsonProperty("ThreadId")
    private Long threadId;

    @JsonProperty("affect_rows")
    private Long affectRows;

    private String error;
    private String level;
    private String sql;
    private String status;
}
