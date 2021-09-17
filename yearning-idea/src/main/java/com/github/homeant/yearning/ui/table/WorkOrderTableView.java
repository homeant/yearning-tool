package com.github.homeant.yearning.ui.table;

import com.github.homeant.yearning.api.YearningService;
import com.github.homeant.yearning.api.domain.PageQuery;
import com.github.homeant.yearning.api.domain.WorkOrderQuery;
import com.github.homeant.yearning.api.domain.WorkOrderResp;
import com.github.homeant.yearning.core.DataGrid;
import com.github.homeant.yearning.ui.panel.WorkOrderDetailsPanel;
import com.intellij.ide.DataManager;
import com.intellij.ui.table.TableView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class WorkOrderTableView extends TableView<WorkOrderResp> implements DataGrid {

    private final WorkOrderTableModel model;

    private final YearningService yearningService;

    private int currentPage;

    private int pageCount;

    public WorkOrderTableView(YearningService yearningService) {
        super();
        currentPage = 1;
        pageCount = -1;
        this.yearningService = yearningService;
        this.model = new WorkOrderTableModel();
        setModelAndUpdateColumns(model);
    }

    @Override
    public Boolean isFirstPage() {
        return currentPage == 1;
    }

    @Override
    public Boolean isLastPage() {
        return currentPage == pageCount;
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void load(int currentPage) {
        this.currentPage = currentPage;
        var query = WorkOrderQuery.builder().type(2).status(7).build();
        this.load(query);
    }

    @Override
    public void loadFirstPage() {
        this.currentPage = 1;
        var query = WorkOrderQuery.builder().type(2).status(7).build();
        this.load(query);
    }

    @Override
    public void loadPreviousPage() {
        this.currentPage = currentPage - 1;
        var query = WorkOrderQuery.builder().type(2).status(7).build();
        this.load(query);
    }

    @Override
    public void loadNextPage() {
        this.currentPage = currentPage + 1;
        var query = WorkOrderQuery.builder().type(2).status(7).build();
        this.load(query);
    }

    @Override
    public void loadLastPage() {
        this.currentPage = pageCount;
        var query = WorkOrderQuery.builder().type(2).status(7).build();
        this.load(query);
    }

    private void load(WorkOrderQuery query) {
        var pageQuery = PageQuery.newQuery(currentPage, query);
        var resp = yearningService.fetchWorkOrder(pageQuery);
        this.pageCount = (resp.getSize() - 1) / 20 + 1;
        this.model.setItems(resp.getList());
    }


}
