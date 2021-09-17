package com.github.homeant.yearning.ui.panel;

import com.github.homeant.yearning.api.YearningService;
import com.github.homeant.yearning.api.domain.WorkOrderResp;

import javax.swing.*;

public class WorkOrderDetailsPanel {
    private JPanel headerPanel;
    private JPanel contentPanel;
    private JButton recallButton;
    private JTextArea sqlArea;
    private JLabel workIdText;
    private JLabel submitDateText;
    private JLabel dataBaseText;
    private JLabel dataSourceText;
    private JLabel idcText;
    private JLabel tableText;
    private JLabel userNameText;
    private JLabel statusText;
    private JLabel remarkText;
    private JLabel typeText;
    private JPanel rootPanel;

    private final YearningService yearningService;

    private WorkOrderResp workOrder;

    public WorkOrderDetailsPanel(YearningService yearningService){
        this.yearningService = yearningService;
        workOrder = new WorkOrderResp();
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public void setWorkOrder(WorkOrderResp workOrder) {
        this.workOrder = workOrder;
        this.bind();
    }

    private void bind(){
        workIdText.setText(workOrder.getWorkId());
        idcText.setText(workOrder.getIdc());
        tableText.setText(workOrder.getTable());
        userNameText.setText(workOrder.getUsername());
        dataSourceText.setText(workOrder.getDataSource());
        statusText.setText(workOrder.getStatus()+"");
        remarkText.setText(workOrder.getText());
        dataBaseText.setText(workOrder.getDataBase());
        submitDateText.setText(workOrder.getDate());
        typeText.setText(workOrder.getType()==1?"DML":"DDL");
        sqlArea.setText(workOrder.getText());
    }










}
