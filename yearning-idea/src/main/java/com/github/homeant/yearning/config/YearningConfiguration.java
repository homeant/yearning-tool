package com.github.homeant.yearning.config;

import com.github.homeant.yearning.api.YearningService;
import com.github.homeant.yearning.domain.YearningSettings;
import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.homeant.yearning.utils.YearningUtils.getService;

@State(name = "YearningSettings", storages = {@Storage("yearning.xml")})
public class YearningConfiguration implements PersistentStateComponent<YearningSettings> {

    private final YearningSettings settings = new YearningSettings();

    private YearningService yearningService;

    private final Project project;

    public YearningConfiguration(Project project) {
        this.project = project;
    }


    public static YearningConfiguration getInstance(Project project) {
        return getService(project, YearningConfiguration.class);
    }

    @Override
    public @Nullable YearningSettings getState() {
        return settings;
    }

    @Override
    public void loadState(@NotNull YearningSettings state) {
        XmlSerializerUtil.copyBean(state, settings);
        settings.setPassword(getPassword());
        yearningService = new YearningService(settings);
    }

    @Nullable
    public YearningService getYearningService() {
        return this.yearningService;
    }


    public void setPassword(String password) {
        var credentialAttributes = createCredentialAttributes(project.getName()); // see previous sample
        var credentials = new Credentials(null, password);
        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }

    public String getPassword() {
        var credentialAttributes = createCredentialAttributes(project.getName()); // see previous sample
        return PasswordSafe.getInstance().getPassword(credentialAttributes);
    }


    private CredentialAttributes createCredentialAttributes(String key) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName("Yearning", key));
    }
}
