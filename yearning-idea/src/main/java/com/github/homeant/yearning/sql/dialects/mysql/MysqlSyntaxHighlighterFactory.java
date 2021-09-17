package com.github.homeant.yearning.sql.dialects.mysql;

import com.intellij.sql.dialects.base.SqlSyntaxHighlighterFactory;

public class MysqlSyntaxHighlighterFactory extends SqlSyntaxHighlighterFactory.Base {
    public MysqlSyntaxHighlighterFactory(){
        super(MysqlSqlDialect.INSTANCE);
    }
}
