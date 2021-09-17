package com.github.homeant.yearning.ui;


import com.github.homeant.yearning.api.YearningService;
import com.github.homeant.yearning.api.domain.SubmitWorkOrder;
import com.github.homeant.yearning.api.domain.TestWorkOrder;
import com.github.homeant.yearning.ui.table.TestWorkOrderTableView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ScrollPaneFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.Optional;

/**
 *
 */
public class WorkOrderDialog extends DialogWrapper {
    private JPanel rootPane;
    private JComboBox<ComboBoxItem> auditComboBox;
    private JComboBox<ComboBoxItem> typeComboBox;
    private JComboBox<ComboBoxItem> idcComboBox;
    private JComboBox<ComboBoxItem> dataSourceComboBox;
    private JComboBox<ComboBoxItem> dataBaseComboBox;
    private JEditorPane sqlEditorPane;
    private JTextArea remarkTextArea;
    private JPanel resultPanel;
    private TestWorkOrderTableView tableView;

    private final YearningService yearningService;

    private final Action testAction;

    public WorkOrderDialog(Project project, YearningService yearningService) {
        super(project, true);
        this.setSize(980, 500);
        this.setTitle("工单提交");
        this.yearningService = yearningService;
        testAction = new AbstractAction("Test") {
            @Override
            public void actionPerformed(ActionEvent e) {
                var testWorkOrder = new TestWorkOrder();
                testWorkOrder.setDataBase(getDataBase());
                testWorkOrder.setIsDml(Objects.equals(getSqlType(), "dml"));
                testWorkOrder.setSource(getDataSource());
                testWorkOrder.setSql(getSql());
                var testRespList = yearningService.testWorkOrder(testWorkOrder);
                tableView.setItems(testRespList);
                resultPanel.setVisible(true);
                var count = testRespList.stream().filter(resp -> !"0".equals(resp.getLevel())).count();
                if (count == 0) {
                    WorkOrderDialog.super.myOKAction.setEnabled(true);
                }else{
                    WorkOrderDialog.super.myOKAction.setEnabled(false);
                }
            }
        };
        this.init();
    }

    /**
     * Factory method. It creates panel with dialog options. Options panel is located at the
     * center of the dialog's content pane. The implementation can return {@code null}
     * value. In this case there will be no options panel.
     */
    @Override
    protected @Nullable JComponent createCenterPanel() {
        return rootPane;
    }

    @Override
    protected Action @NotNull [] createActions() {
        var helpAction = getHelpAction();
        return helpAction == myHelpAction && getHelpId() == null ?
                new Action[]{getOKAction(), getTestAction(), getCancelAction()} :
                new Action[]{getOKAction(), getTestAction(), getCancelAction(), helpAction};
    }

    private Action getTestAction() {
        return this.testAction;
    }

    @Override
    protected void init() {
        typeComboBox.addItem(ComboBoxItem.builder().name("DDL").value("ddl").build());
        typeComboBox.addItem(ComboBoxItem.builder().name("DML").value("dml").build());
        yearningService.fetchIdc().forEach(idc -> idcComboBox.addItem(ComboBoxItem.builder().name(idc).value(idc).build()));
        dataSourceComboBox.addActionListener(this::dataSourceAction);
        idcComboBox.addActionListener(this::idcAction);
        tableView = new TestWorkOrderTableView();
        resultPanel.add(ScrollPaneFactory.createScrollPane(tableView), BorderLayout.CENTER);
        resultPanel.setVisible(false);
        super.myOKAction.setEnabled(false);
        super.init();
    }

    @Override
    protected void doOKAction() {
        var workOrder = new SubmitWorkOrder();
        workOrder.setAssigned("");
        workOrder.setBackup(1);
        workOrder.setDataBase(getDataBase());
        workOrder.setDelay("");
        workOrder.setExport(0);
        workOrder.setIdc(getIdc());
        workOrder.setRealName("");
        workOrder.setDataSource(getDataSource());
        workOrder.setSql(getSql());
        workOrder.setTable("");
        workOrder.setText(getRemark());
        workOrder.setTp(1);
        workOrder.setType(1);
        yearningService.submitWorkOrder(workOrder);
        super.doOKAction();
    }

    private void dataSourceAction(ActionEvent event) {
        // 审批人
        var resp = yearningService.fetchDataBase(getDataSource());
        dataBaseComboBox.removeAllItems();
        auditComboBox.removeAllItems();
        Optional.ofNullable(resp.getAdminList()).ifPresent(admin -> admin.forEach(assigned -> auditComboBox.addItem(ComboBoxItem.builder().name(assigned).value(assigned).build())));
        Optional.ofNullable(resp.getDataBaseList()).ifPresent(result -> result.forEach(dataBase -> dataBaseComboBox.addItem(ComboBoxItem.builder().name(dataBase).value(dataBase).build())));

    }

    private void idcAction(ActionEvent event) {
        String idc = getIdc();
        String sqlType = getSqlType();
        if (StringUtils.isNotBlank(idc) && StringUtils.isNotBlank(sqlType)) {
            // 数据源
            dataSourceComboBox.removeAllItems();
            yearningService.fetchDataSource(idc, sqlType).getSource().forEach(source -> dataSourceComboBox.addItem(ComboBoxItem.builder().name(source).value(source).build()));
        }
    }

    private String getIdc() {
        var item = (ComboBoxItem) idcComboBox.getSelectedItem();
        if (item != null) {
            return item.getValue();
        }
        return null;
    }

    private String getSqlType() {
        var item = (ComboBoxItem) typeComboBox.getSelectedItem();
        if (item != null) {
            return item.getValue();
        }
        return null;
    }

    private String getDataSource() {
        var item = (ComboBoxItem) dataSourceComboBox.getSelectedItem();
        if (item != null) {
            return item.getValue();
        }
        return null;
    }

    private String getDataBase() {
        var item = (ComboBoxItem) dataBaseComboBox.getSelectedItem();
        if (item != null) {
            return item.getValue();
        }
        return null;
    }

    public String getSql() {
        return sqlEditorPane.getText();
    }

    public String getRemark() {
        return remarkTextArea.getText();
    }

    public void setSqlValue(String sqlText) {
        sqlEditorPane.setText(sqlText);
    }
}
