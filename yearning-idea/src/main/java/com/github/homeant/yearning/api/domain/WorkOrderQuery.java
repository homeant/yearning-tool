package com.github.homeant.yearning.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderQuery {
    private String explain;
    private List picker;
    private Integer status;
    private String text;
    private Integer type;
    private boolean valve;
    private String workId;
}
