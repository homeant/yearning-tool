package com.github.homeant.yearning.jdbc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private JsonUtils() {

    }

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
    }

    public static String writeAsString(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("failed to convert to JSON", e);
        }
    }

}
