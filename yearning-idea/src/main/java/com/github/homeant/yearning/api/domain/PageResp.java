package com.github.homeant.yearning.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageResp<T> {
    @JsonProperty("page")
    private Integer size;

    @JsonProperty("data")
    private List<T> list;
}
