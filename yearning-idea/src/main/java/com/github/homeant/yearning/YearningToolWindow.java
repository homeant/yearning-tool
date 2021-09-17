package com.github.homeant.yearning;

import com.github.homeant.yearning.config.YearningConfiguration;
import com.github.homeant.yearning.ui.panel.WorkOrderListPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class YearningToolWindow implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        var contentManager = toolWindow.getContentManager();
        contentManager.removeAllContents(true);

        var yearningService = YearningConfiguration.getInstance(project).getYearningService();
        var workOrderListPanel = new WorkOrderListPanel(project, yearningService);

        ContentFactory factory = ContentFactory.SERVICE.getInstance();

        var content = factory.createContent(workOrderListPanel, "工单列表", false);
        content.setCloseable(false);

        contentManager.addContent(content);

        toolWindow.setType(ToolWindowType.DOCKED, null);
    }
}
