package com.github.homeant.yearning.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class DataSourceResp {
    private List<String> assigned;

    private List<String> source;
}
