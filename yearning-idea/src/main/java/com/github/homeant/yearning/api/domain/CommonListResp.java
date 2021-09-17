package com.github.homeant.yearning.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommonListResp<T> implements Serializable {
    private Integer code;

    @JsonProperty("payload")
    private List<T> data;

    private String text;

    public boolean isSuccessful() {
        return 1200 == code;
    }
}
