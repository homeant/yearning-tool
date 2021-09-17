package com.github.homeant.yearning.jdbc.api.domain;

import lombok.Data;

import java.util.List;

@Data
public class ApprovalUserResp {
    private List<String> assigned;

    private List<String> source;
}
