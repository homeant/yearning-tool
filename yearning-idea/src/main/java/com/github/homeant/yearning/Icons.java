package com.github.homeant.yearning;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.IconManager;
import icons.DatabaseIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


public class Icons {

    public static final Icon LOGO = load("/img/yearning-logo.png");

    @NotNull
    private static Icon load(@NotNull String path) {
        return IconLoader.getIcon(path,Icons.class);
    }


}
