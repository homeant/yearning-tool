package com.github.homeant.yearning.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Login {
    private String username;

    private String password;
}
