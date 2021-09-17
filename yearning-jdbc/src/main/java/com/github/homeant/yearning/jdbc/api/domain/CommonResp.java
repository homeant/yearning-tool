package com.github.homeant.yearning.jdbc.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CommonResp<T> implements Serializable {
    private Integer code;

    @JsonProperty("payload")
    private T data;

    private String text;

    public boolean isSuccessful() {
        return 1200 == code;
    }
}
