package com.github.homeant.yearning.sql.dialects.mysql;

import com.github.homeant.yearning.Constants;
import com.github.homeant.yearning.database.dialects.mysql.MysqlHolder;
import com.intellij.database.Dbms;
import com.intellij.psi.tree.IElementType;
import com.intellij.sql.dialects.SqlLanguageDialectEx;
import com.intellij.sql.dialects.SqlTypeSystem;
import com.intellij.sql.dialects.SqlTypeSystemBase;
import com.intellij.sql.dialects.mysql.MysqlDialect;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class MysqlSqlDialect extends SqlLanguageDialectEx {

    public static final MysqlSqlDialect INSTANCE = new MysqlSqlDialect();

    public MysqlSqlDialect() {
        super(Constants.YEARNING_MY_SQL);
    }

    @Override
    public @NotNull SqlTypeSystemBase getTypeSystem() {
        return MysqlTypeSystem.INSTANCE;
    }

    @Override
    public @NotNull BuiltinFunctions getSupportedFunctions() {
        return MysqlDialect.INSTANCE.getSupportedFunctions();
    }

    @Override
    public @NotNull Dbms getDbms() {
        return MysqlHolder.DBMS;
    }

    @Override
    public boolean isReservedKeyword(IElementType iElementType) {
        return MysqlDialect.INSTANCE.isReservedKeyword(iElementType);
    }

    @Override
    public boolean isOperatorSupported(IElementType iElementType) {
        return MysqlDialect.INSTANCE.isOperatorSupported(iElementType);
    }

    @Override
    public @NotNull Set<String> getKeywords() {
        return MysqlDialect.INSTANCE.getKeywords();
    }

    @Override
    public @NotNull Set<String> getReservedKeywords() {
        return MysqlDialect.INSTANCE.getReservedKeywords();
    }

    @Override
    public @NotNull Set<String> getSystemVariables() {
        return MysqlDialect.INSTANCE.getSystemVariables();
    }
}
