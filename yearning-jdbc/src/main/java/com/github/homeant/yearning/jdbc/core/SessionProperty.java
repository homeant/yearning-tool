package com.github.homeant.yearning.jdbc.core;

public enum SessionProperty {
    /**
     * 服务器地址
     */
    SERVER_URL("serverURL", true, String.class),
    USER("user", false, String.class),
    PASSWORD("password", false, String.class),
    TOKEN("token",false,String.class),
    MOCK("mock",false,Boolean.class);


    private String propertyKey;

    private boolean required;

    private Class<?> valueType;

    SessionProperty(String propertyKey, boolean required, Class<?> valueType, String... aliases) {
        this.propertyKey = propertyKey;
        this.required = required;
        this.valueType = valueType;
    }
}
