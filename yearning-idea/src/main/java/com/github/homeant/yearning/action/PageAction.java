package com.github.homeant.yearning.action;

import com.github.homeant.yearning.YearningDataKeys;
import com.github.homeant.yearning.core.DataGrid;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


public abstract class PageAction extends DumbAwareAction {

    protected PageAction(Icon icon) {
        super(icon);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        var dataGrid = e.getData(YearningDataKeys.TABLE_VIEW_KEY);
        if (dataGrid != null) {
            e.getPresentation().setVisible(true);
            e.getPresentation().setEnabled(this.isEnabled(dataGrid));
        } else {
            e.getPresentation().setEnabledAndVisible(false);
        }

    }

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        var dataGrid = e.getData(YearningDataKeys.TABLE_VIEW_KEY);
        if (dataGrid != null) {
            this.actionPerformed(dataGrid);
        }
    }

    protected abstract boolean isEnabled(DataGrid dataGrid);

    protected abstract void actionPerformed(DataGrid dataGrid);

    public static class First extends PageAction {
        public First() {
            super(AllIcons.Actions.Play_first);
        }

        @Override
        protected boolean isEnabled(DataGrid dataGrid) {
            return !dataGrid.isFirstPage();
        }

        @Override
        protected void actionPerformed(DataGrid dataGrid) {
            dataGrid.loadFirstPage();
        }
    }

    public static class Previous extends PageAction {
        public Previous() {
            super(AllIcons.Actions.Play_back);
        }

        @Override
        protected boolean isEnabled(DataGrid dataGrid) {
            return !dataGrid.isFirstPage();
        }

        @Override
        protected void actionPerformed(DataGrid dataGrid) {
            dataGrid.loadPreviousPage();
        }
    }

    public static class Next extends PageAction {
        public Next() {
            super(AllIcons.Actions.Play_forward);
        }

        @Override
        protected boolean isEnabled(DataGrid dataGrid) {
            return !dataGrid.isLastPage();
        }

        @Override
        protected void actionPerformed(DataGrid dataGrid) {
            dataGrid.loadNextPage();
        }
    }

    public static class Last extends PageAction {
        public Last() {
            super(AllIcons.Actions.Play_last);
        }

        @Override
        protected boolean isEnabled(DataGrid dataGrid) {
            return !dataGrid.isLastPage();
        }

        @Override
        protected void actionPerformed(DataGrid dataGrid) {
            dataGrid.loadLastPage();
        }
    }

    public static class Reload extends PageAction {
        public Reload() {
            super(PlatformIcons.SYNCHRONIZE_ICON);
        }

        @Override
        protected boolean isEnabled(DataGrid dataGrid) {
            return true;
        }

        @Override
        protected void actionPerformed(DataGrid dataGrid) {
            dataGrid.load(dataGrid.getCurrentPage());
        }
    }
}
