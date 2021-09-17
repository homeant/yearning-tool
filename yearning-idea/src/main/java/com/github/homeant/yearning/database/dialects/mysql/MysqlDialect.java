package com.github.homeant.yearning.database.dialects.mysql;

import com.intellij.database.Dbms;
import com.intellij.database.dialects.mysqlbase.MysqlBaseDialect;
import com.intellij.openapi.util.NlsSafe;
import org.jetbrains.annotations.NotNull;

public class MysqlDialect extends MysqlBaseDialect {

    private final Dbms dbms;

    public MysqlDialect(Dbms dbms) {
        this.dbms = dbms;
    }

    @Override
    public @NotNull Dbms getDbms() {
        return this.dbms;
    }

    @Override
    public @NlsSafe @NotNull String getDisplayName() {
        return "Yearning SQL";
    }
}
