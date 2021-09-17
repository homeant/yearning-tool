package com.github.homeant.yearning.jdbc.api.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SchemaResp implements Serializable {

    private List<Info> info;

    @Data
    public static class Info{
        private String title;

        private List<Children> children;
    }

    @Data
    public static class Children{
        private String title;
    }
}
