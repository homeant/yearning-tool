package com.github.homeant.yearning.action;

import com.github.homeant.yearning.api.YearningService;
import com.github.homeant.yearning.config.YearningConfiguration;
import com.github.homeant.yearning.domain.YearningSettings;
import com.github.homeant.yearning.ui.WorkOrderDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class NewWorkOrderAction extends DumbAwareAction {
    /**
     * Implement this method to provide your action handler.
     *
     * @param event Carries information on the invocation place
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        var project = event.getData(CommonDataKeys.PROJECT);
        var instance = YearningConfiguration.getInstance(project);
        YearningSettings state = instance.getState();
        if(state!=null) {
            var panel = new WorkOrderDialog(project, new YearningService(state));
            var editor = event.getData(CommonDataKeys.EDITOR);
            if(editor!=null) {
                var selectedText = editor.getSelectionModel().getSelectedText();
                panel.setSqlValue(selectedText);
            }
            panel.show();
        }
    }
}
