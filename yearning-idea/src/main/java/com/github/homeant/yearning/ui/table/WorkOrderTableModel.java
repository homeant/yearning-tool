package com.github.homeant.yearning.ui.table;

import com.github.homeant.yearning.api.domain.WorkOrderResp;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import static com.github.homeant.yearning.utils.YearningUtils.newColumnInfo;


public class WorkOrderTableModel extends ListTableModel<WorkOrderResp> {

    public WorkOrderTableModel() {
        super();
        setColumnInfos(new ColumnInfo[]{
                newColumnInfo("工单编号", WorkOrderResp::getWorkId),
                newColumnInfo("描述", WorkOrderResp::getText),
                newColumnInfo("工单类型", (WorkOrderResp r) -> Objects.equals(1, r.getType()) ? "DML" : "DDL"),
                newColumnInfo("是否备份", (WorkOrderResp r) -> Objects.equals(1, r.getBackup()) ? "是" : "否"),
                newColumnInfo("提交时间", WorkOrderResp::getDate),
                newColumnInfo("当前操作人", WorkOrderResp::getAssigned)
        });
    }

}
