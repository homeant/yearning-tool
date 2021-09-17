package com.github.homeant.yearning.sql.dialects.mysql;

import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.sql.dialects.base.SqlElementFactory;
import com.intellij.sql.dialects.base.SqlParserDefinitionBase;
import com.intellij.sql.psi.stubs.SqlFileElementType;
import org.jetbrains.annotations.NotNull;

public class MysqlParserDefinition extends SqlParserDefinitionBase {

    private static final IFileElementType MYSQL_SQL_FILE;

    private static final com.intellij.sql.dialects.mysql.MysqlParserDefinition MYSQL_PARSER_DEFINITION;

    static {
        MYSQL_SQL_FILE = new SqlFileElementType("YEARNING_SQL_FILE", MysqlSqlDialect.INSTANCE);
        MYSQL_PARSER_DEFINITION = new com.intellij.sql.dialects.mysql.MysqlParserDefinition();
    }



    @Override
    protected SqlElementFactory createElementFactory() {
        return MYSQL_PARSER_DEFINITION.getElementFactory();
    }

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return MYSQL_PARSER_DEFINITION.createLexer(project);
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return MYSQL_PARSER_DEFINITION.createParser(project);
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return MYSQL_SQL_FILE;
    }
}
