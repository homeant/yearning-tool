package com.github.homeant.yearning.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WorkOrderResp {
    private String assigned;
    private Integer backup;
    private Integer current;
    private Integer currentStep;
    private String dataBase;
    private String date;
    private String delay;
    @JsonProperty("execute_time")
    private String executeTime;
    private String executor;
    private Integer id;
    private String idc;
    private Integer isKill;
    private Integer percent;
    private String realName;
    private List<String> relevant;
    @JsonProperty("source")
    private String dataSource;
    private String sql;
    private Integer status;
    private String table;
    private String text;
    private String time;
    private Integer type;
    private String username;
    private String uuid;
    private String workId;
}
