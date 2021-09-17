package com.github.homeant.yearning.config;

import com.github.homeant.yearning.ui.ConfigurablePanel;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@Slf4j
public class YearningConfigurable implements Configurable {

    private ConfigurablePanel panel;

    private final Project project;


    public YearningConfigurable(Project project){
        this.project = project;
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Yearning";
    }

    @Override
    public @Nullable JComponent createComponent() {
        if(panel==null){
            panel = new ConfigurablePanel(project);
        }
        return panel.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return panel!=null && panel.isModified(project);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (panel != null) {
            panel.save(project);
        }
    }

    @Override
    public void reset() {
        if (panel != null) {
            panel.reset(project);
        }
    }
}
