package com.github.homeant.yearning.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PageQuery<T> {
    @JsonProperty("page")
    private Integer currentPage;

    private T find;

    public static <T> PageQuery<T> newQuery(T find){
        PageQuery<T> pageQuery = new PageQuery<>();
        pageQuery.setFind(find);
        return pageQuery;
    }

    public static <T> PageQuery<T> newQuery(int page,T find){
        PageQuery<T> pageQuery = new PageQuery<>();
        pageQuery.setFind(find);
        pageQuery.setCurrentPage(page);
        return pageQuery;
    }
}
