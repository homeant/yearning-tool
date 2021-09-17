package com.github.homeant.yearning.jdbc.api.domain;

import lombok.Data;

@Data
public class QueryTicket {
    private String assigned;

    private int export;

    private String idc;

    private String text;
}
