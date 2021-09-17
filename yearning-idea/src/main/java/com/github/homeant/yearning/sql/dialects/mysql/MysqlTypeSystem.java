package com.github.homeant.yearning.sql.dialects.mysql;

import com.intellij.sql.dialects.SqlTypeSystemBase;

public class MysqlTypeSystem extends SqlTypeSystemBase {

    public static final MysqlTypeSystem INSTANCE = new MysqlTypeSystem();

    protected MysqlTypeSystem() {
        super(MysqlSqlDialect.INSTANCE);
    }
}
