package com.github.homeant.yearning.ui.panel;

import com.github.homeant.yearning.YearningDataKeys;
import com.github.homeant.yearning.api.YearningService;
import com.github.homeant.yearning.api.domain.PageQuery;
import com.github.homeant.yearning.api.domain.PageResp;
import com.github.homeant.yearning.api.domain.WorkOrderQuery;
import com.github.homeant.yearning.api.domain.WorkOrderResp;
import com.github.homeant.yearning.ui.table.WorkOrderTableModel;
import com.github.homeant.yearning.ui.table.WorkOrderTableView;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static javax.swing.SwingConstants.CENTER;

public class WorkOrderListPanel extends SimpleToolWindowPanel {

    private final Project project;

    private final YearningService yearningService;

    private WorkOrderTableView workOrderTableView;

    private Integer currentPage;

    public WorkOrderListPanel(Project project, YearningService yearningService) {
        super(true, true);
        this.project = project;
        this.yearningService = yearningService;
        this.currentPage = 1;
        init();
    }

    @Override
    public @Nullable Object getData(@NotNull @NonNls String dataId) {
        if (YearningDataKeys.TABLE_VIEW_KEY.is(dataId)) {
            return workOrderTableView;
        }
        return super.getData(dataId);
    }

    public Integer getCurrentPage() {
        return currentPage;
    }


    private void init() {
        initToolBar();
        initContent();
    }

    private void initContent() {
        if (yearningService != null) {
            workOrderTableView = new WorkOrderTableView(yearningService);

            var jPanel = new JPanel(new BorderLayout());
            jPanel.setBorder(JBUI.Borders.customLine(JBColor.border(), 0, 0, 1, 0));
            jPanel.add(ScrollPaneFactory.createScrollPane(workOrderTableView), BorderLayout.CENTER);
            var workOrderDetailsPanel = new WorkOrderDetailsPanel(yearningService);
            var subWorkOrderDetailsPanel = workOrderDetailsPanel.getRootPanel();
            jPanel.add(ScrollPaneFactory.createScrollPane(subWorkOrderDetailsPanel), BorderLayout.EAST);
            var model = (WorkOrderTableModel) workOrderTableView.getModel();
            model.setItems(loadData());
            subWorkOrderDetailsPanel.setVisible(false);
            workOrderTableView.addMouseListener(new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseClicked(MouseEvent e) {
                    var workOrderResp = workOrderTableView.getSelectedObject();
                    if (workOrderResp != null) {
                        workOrderDetailsPanel.setWorkOrder(workOrderResp);
                        subWorkOrderDetailsPanel.setVisible(true);
                    }
                }
            });

            Splitter splitter = new Splitter(false);
            splitter.setFirstComponent(jPanel);
            splitter.setSecondComponent(subWorkOrderDetailsPanel);
            splitter.setHonorComponentsMinimumSize(true);

            super.setContent(splitter);

            workOrderTableView.setVisible(true);
        } else {
            var panel = new YearningPanel(new GridBagLayout());
            var messageLabel = new JBLabel("Not support");
            messageLabel.setHorizontalAlignment(CENTER);
            messageLabel.setVerticalAlignment(CENTER);
            panel.add(messageLabel, new GridBagConstraints());
            super.setContent(panel);
        }
    }

    private void initToolBar() {
        var actionManager = ActionManager.getInstance();
        var group = (ActionGroup) actionManager.getAction("Console.TableResult.Yearning.Group");
        var actionToolbar = actionManager.createActionToolbar("Yearning", group, true);
        actionToolbar.setTargetComponent(this);
        var toolBarBox = Box.createVerticalBox();
        toolBarBox.add(actionToolbar.getComponent());
        super.setToolbar(toolBarBox);
        actionToolbar.getComponent().setVisible(true);
    }

    private List<WorkOrderResp> loadData() {
        var workOrderQuery = new WorkOrderQuery();
        workOrderQuery.setStatus(7);
        workOrderQuery.setType(2);
        PageQuery<WorkOrderQuery> query = new PageQuery<>();
        query.setCurrentPage(currentPage);
        query.setFind(workOrderQuery);
        PageResp<WorkOrderResp> pageResp = yearningService.fetchWorkOrder(query);
        return pageResp.getList();
    }

}
