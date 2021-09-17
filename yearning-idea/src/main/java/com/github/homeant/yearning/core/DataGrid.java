package com.github.homeant.yearning.core;

public interface DataGrid {
    void load(int currentPage);

    void loadFirstPage();

    void loadPreviousPage();

    void loadNextPage();

    void loadLastPage();

    Boolean isFirstPage();

    Boolean isLastPage();

    int getCurrentPage();
}
