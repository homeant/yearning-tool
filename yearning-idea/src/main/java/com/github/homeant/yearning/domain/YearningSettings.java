package com.github.homeant.yearning.domain;

import com.intellij.util.xmlb.annotations.Transient;
import lombok.Data;

import java.io.Serializable;

@Data
public class YearningSettings implements Serializable {
    private String url;

    private String username;

    @Transient
    private String password;

    private Boolean isLdap;
}
