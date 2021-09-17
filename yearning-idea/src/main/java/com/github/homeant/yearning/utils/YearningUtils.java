package com.github.homeant.yearning.utils;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.ColumnInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class YearningUtils {
    private static final Logger LOG = Logger.getInstance(YearningUtils.class);

    public static <T> T getService(Class<T> clazz) {
        return ServiceManager.getService(clazz);
    }

    public static <T> T getService(@NotNull Project project, Class<T> clazz) {
        return ServiceManager.getService(project, clazz);
    }

    public static  <T, W> ColumnInfo<T, W> newColumnInfo(String name, Function<T, W> valueFun) {
        return new ColumnInfo<T, W>(name) {

            @Override
            public @Nullable W valueOf(T t) {
                return valueFun.apply(t);
            }
        };
    }
}
