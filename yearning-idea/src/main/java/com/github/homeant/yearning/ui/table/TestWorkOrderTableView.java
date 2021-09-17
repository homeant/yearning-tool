package com.github.homeant.yearning.ui.table;

import com.github.homeant.yearning.api.domain.TestWorkOrderResp;
import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TestWorkOrderTableView extends TableView<TestWorkOrderResp>{

    private final ListTableModel<TestWorkOrderResp> model;

    public TestWorkOrderTableView(){
        super();
        this.model = new TestWorkOrderTableModel();
        setModelAndUpdateColumns(model);
    }

    public void setItems(List<TestWorkOrderResp> testRespList) {
        this.model.setItems(testRespList);
    }
}
