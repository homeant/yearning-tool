package com.github.homeant.yearning.ui;

import com.github.homeant.yearning.api.YearningService;
import com.github.homeant.yearning.api.domain.LoginResp;
import com.github.homeant.yearning.config.YearningConfiguration;
import com.github.homeant.yearning.domain.YearningSettings;
import com.github.homeant.yearning.utils.YearningUtils;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.Optional;

public class ConfigurablePanel implements Disposable {
    private static final Logger logger = Logger.getInstance(YearningUtils.class);

    private JPanel rootPanel;
    private JCheckBox ldapLoginCheckBox;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private JTextField urlField;
    private JButton testButton;

    public ConfigurablePanel(Project project) {
        YearningConfiguration instance = YearningConfiguration.getInstance(project);
        if (instance != null) {
            YearningSettings settings = instance.getState();
            if (settings != null) {
                urlField.setText(settings.getUrl());
                passwordField.setText(instance.getPassword());
                usernameField.setText(settings.getUsername());
                ldapLoginCheckBox.setSelected(Optional.ofNullable(settings.getIsLdap()).orElse(false));
            }
        }
        testButton.addActionListener(e -> {
            YearningSettings settings = new YearningSettings();
            settings.setUrl(getUrl());
            YearningService yearningService = new YearningService(settings);
            try {
                LoginResp loginResp = yearningService.login(getUsername(), getPassword());
                if (loginResp != null && StringUtils.isNotBlank(loginResp.getToken())) {
                    Messages.showMessageDialog(project, "test for login success!" + loginResp.getRealName(), "Tips", UIUtil.getInformationIcon());
                }
            } catch (Exception ex) {
                Messages.showErrorDialog(project, "test for login error,error info:" + ex.getMessage(), "Tips");
            }
        });
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }


    @Override
    public void dispose() {

    }

    public void save(Project project) {
        YearningConfiguration instance = YearningConfiguration.getInstance(project);
        if (instance != null) {
            YearningSettings settings = instance.getState();
            settings.setUrl(getUrl());
            settings.setUsername(getUsername());
            settings.setIsLdap(isLdap());
            instance.setPassword(getPassword());
        }
    }

    public boolean isModified(Project project) {
        return true;
    }

    public String getUrl() {
        return urlField.getText();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public boolean isLdap() {
        return ldapLoginCheckBox.isSelected();
    }

    public void reset(Project project) {
        YearningConfiguration instance = YearningConfiguration.getInstance(project);
        if (instance != null) {
            YearningSettings settings = instance.getState();
            urlField.setText(settings.getUrl());
            passwordField.setText(instance.getPassword());
            usernameField.setText(settings.getUsername());
            ldapLoginCheckBox.setSelected(Optional.ofNullable(settings.getIsLdap()).orElse(false));
        }
    }
}
