package com.github.homeant.yearning.jdbc.api.domain;

import lombok.Data;

@Data
public class LoginResp {

    private String permissions;

    private String realName;

    private String token;

}
