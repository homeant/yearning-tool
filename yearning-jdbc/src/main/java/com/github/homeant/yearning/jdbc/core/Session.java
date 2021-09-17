package com.github.homeant.yearning.jdbc.core;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;


public class Session {

    @Getter
    private Properties ClientInfo = new Properties();

    private final AtomicBoolean autoCommit = new AtomicBoolean(false);

    private Map<SessionProperty, Object> connectionPropertiesMap = new HashMap<>();

    public String getUrl() {
        return (String) connectionPropertiesMap.get(SessionProperty.SERVER_URL);
    }

    public void setUrl(String url) {
        connectionPropertiesMap.put(SessionProperty.SERVER_URL, url);
    }

    public String getUserName() {
        return (String) connectionPropertiesMap.get(SessionProperty.USER);
    }

    public void setUserName(String userName) {
        connectionPropertiesMap.put(SessionProperty.USER, userName);
    }

    public String getPassword() {
        return (String) connectionPropertiesMap.get(SessionProperty.PASSWORD);
    }

    public void setPassword(String password) {
        connectionPropertiesMap.put(SessionProperty.PASSWORD, password);
    }

    public String getToken() {
        return (String) connectionPropertiesMap.get(SessionProperty.TOKEN);
    }

    public void setToken(String token) {
        connectionPropertiesMap.put(SessionProperty.TOKEN, token);
    }

    public boolean getAutoCommit() {
        return autoCommit.get();
    }

    public boolean getMock() {
        return (Boolean) connectionPropertiesMap.get(SessionProperty.MOCK);
    }

    public void setMock(boolean password) {
        connectionPropertiesMap.put(SessionProperty.MOCK, password);
    }
}
