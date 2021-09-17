package com.github.homeant.yearning.jdbc;

import com.github.homeant.yearning.jdbc.api.YearningService;
import com.github.homeant.yearning.jdbc.connection.JdbcConnection;
import com.github.homeant.yearning.jdbc.constant.TableType;
import com.github.homeant.yearning.jdbc.constant.TypeDescriptor;
import com.github.homeant.yearning.jdbc.core.JdbcWrapper;
import com.github.homeant.yearning.jdbc.core.Session;
import com.github.homeant.yearning.jdbc.result.*;
import com.github.homeant.yearning.jdbc.utils.ColumnUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.*;


public class JdbcDatabaseMetaData implements DatabaseMetaData, JdbcWrapper {

    private final Log log = LogFactory.getLog(JdbcDatabaseMetaData.class);

    private final JdbcConnection connection;

    private final YearningService yearningService;

    private final Session session;


    private static final String PERCENT_SYMBOL = "%";

    private static final String[] MYSQL_KEYWORDS = new String[]{"ACCESSIBLE", "ADD", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE",
            "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN",
            "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CUBE", "CUME_DIST", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP",
            "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE",
            "DEFAULT", "DELAYED", "DELETE", "DENSE_RANK", "DESC", "DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL",
            "EACH", "ELSE", "ELSEIF", "EMPTY", "ENCLOSED", "ESCAPED", "EXCEPT", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FIRST_VALUE", "FLOAT", "FLOAT4",
            "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "FUNCTION", "GENERATED", "GET", "GRANT", "GROUP", "GROUPING", "GROUPS", "HAVING",
            "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT", "INSENSITIVE",
            "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IO_AFTER_GTIDS", "IO_BEFORE_GTIDS", "IS", "ITERATE",
            "JOIN", "JSON_TABLE", "KEY", "KEYS", "KILL", "LAG", "LAST_VALUE", "LEAD", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD",
            "LOCALTIME", "LOCALTIMESTAMP", "LOCK", "LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_BIND", "MASTER_SSL_VERIFY_SERVER_CERT",
            "MATCH", "MAXVALUE", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL",
            "NOT", "NO_WRITE_TO_BINLOG", "NTH_VALUE", "NTILE", "NULL", "NUMERIC", "OF", "ON", "OPTIMIZE", "OPTIMIZER_COSTS", "OPTION", "OPTIONALLY", "OR",
            "ORDER", "OUT", "OUTER", "OUTFILE", "OVER", "PARTITION", "PERCENT_RANK", "PERSIST", "PERSIST_ONLY", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE",
            "RANGE", "RANK", "READ", "READS", "READ_WRITE", "REAL", "RECURSIVE", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE",
            "RESIGNAL", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "ROW", "ROWS", "ROW_NUMBER", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT",
            "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SMALLINT", "SPATIAL", "SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING",
            "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STORED", "STRAIGHT_JOIN", "SYSTEM", "TABLE", "TERMINATED", "THEN",
            "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE",
            "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "VIRTUAL", "WHEN", "WHERE", "WHILE",
            "WINDOW", "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL"};

    // SQL:2003 reserved words from 'ISO/IEC 9075-2:2003 (E), 2003-07-25'
    /* package private */ static final List<String> SQL2003_KEYWORDS = Arrays.asList("ABS", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS",
            "ASENSITIVE", "ASYMMETRIC", "AT", "ATOMIC", "AUTHORIZATION", "AVG", "BEGIN", "BETWEEN", "BIGINT", "BINARY", "BLOB", "BOOLEAN", "BOTH", "BY", "CALL",
            "CALLED", "CARDINALITY", "CASCADED", "CASE", "CAST", "CEIL", "CEILING", "CHAR", "CHARACTER", "CHARACTER_LENGTH", "CHAR_LENGTH", "CHECK", "CLOB",
            "CLOSE", "COALESCE", "COLLATE", "COLLECT", "COLUMN", "COMMIT", "CONDITION", "CONNECT", "CONSTRAINT", "CONVERT", "CORR", "CORRESPONDING", "COUNT",
            "COVAR_POP", "COVAR_SAMP", "CREATE", "CROSS", "CUBE", "CUME_DIST", "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_PATH",
            "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_USER", "CURSOR", "CYCLE", "DATE", "DAY",
            "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELETE", "DENSE_RANK", "DEREF", "DESCRIBE", "DETERMINISTIC", "DISCONNECT", "DISTINCT",
            "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELEMENT", "ELSE", "END", "END-EXEC", "ESCAPE", "EVERY", "EXCEPT", "EXEC", "EXECUTE", "EXISTS", "EXP",
            "EXTERNAL", "EXTRACT", "FALSE", "FETCH", "FILTER", "FLOAT", "FLOOR", "FOR", "FOREIGN", "FREE", "FROM", "FULL", "FUNCTION", "FUSION", "GET",
            "GLOBAL", "GRANT", "GROUP", "GROUPING", "HAVING", "HOLD", "HOUR", "IDENTITY", "IN", "INDICATOR", "INNER", "INOUT", "INSENSITIVE", "INSERT", "INT",
            "INTEGER", "INTERSECT", "INTERSECTION", "INTERVAL", "INTO", "IS", "JOIN", "LANGUAGE", "LARGE", "LATERAL", "LEADING", "LEFT", "LIKE", "LN", "LOCAL",
            "LOCALTIME", "LOCALTIMESTAMP", "LOWER", "MATCH", "MAX", "MEMBER", "MERGE", "METHOD", "MIN", "MINUTE", "MOD", "MODIFIES", "MODULE", "MONTH",
            "MULTISET", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NEW", "NO", "NONE", "NORMALIZE", "NOT", "NULL", "NULLIF", "NUMERIC", "OCTET_LENGTH", "OF",
            "OLD", "ON", "ONLY", "OPEN", "OR", "ORDER", "OUT", "OUTER", "OVER", "OVERLAPS", "OVERLAY", "PARAMETER", "PARTITION", "PERCENTILE_CONT",
            "PERCENTILE_DISC", "PERCENT_RANK", "POSITION", "POWER", "PRECISION", "PREPARE", "PRIMARY", "PROCEDURE", "RANGE", "RANK", "READS", "REAL",
            "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT", "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX",
            "REGR_SXY", "REGR_SYY", "RELEASE", "RESULT", "RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLLBACK", "ROLLUP", "ROW", "ROWS", "ROW_NUMBER", "SAVEPOINT",
            "SCOPE", "SCROLL", "SEARCH", "SECOND", "SELECT", "SENSITIVE", "SESSION_USER", "SET", "SIMILAR", "SMALLINT", "SOME", "SPECIFIC", "SPECIFICTYPE",
            "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQRT", "START", "STATIC", "STDDEV_POP", "STDDEV_SAMP", "SUBMULTISET", "SUBSTRING", "SUM",
            "SYMMETRIC", "SYSTEM", "SYSTEM_USER", "TABLE", "TABLESAMPLE", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING",
            "TRANSLATE", "TRANSLATION", "TREAT", "TRIGGER", "TRIM", "TRUE", "UESCAPE", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UPDATE", "UPPER", "USER",
            "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VAR_POP", "VAR_SAMP", "WHEN", "WHENEVER", "WHERE", "WIDTH_BUCKET", "WINDOW", "WITH", "WITHIN",
            "WITHOUT", "YEAR");

    private static volatile String mysqlKeywords = null;

    public JdbcDatabaseMetaData(JdbcConnection connection) throws SQLException {
        log.info("instantiate");
        this.connection = connection;
        JdbcConnection unwrap = connection.unwrap(JdbcConnection.class);
        yearningService = unwrap.getYearningService();
        session = unwrap.getSession();
    }

    @Override
    public boolean allProceduresAreCallable() throws SQLException {
        log.info("allProceduresAreCallable");
        return false;
    }

    @Override
    public boolean allTablesAreSelectable() throws SQLException {
        log.info("allTablesAreSelectable");
        return false;
    }

    @Override
    public String getURL() throws SQLException {
        log.info("allTablesAreSelectable");
        return session.getUrl();
    }

    @Override
    public String getUserName() throws SQLException {
        log.info("getUserName");
        return session.getUserName();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        log.info("isReadOnly");
        return true;
    }

    @Override
    public boolean nullsAreSortedHigh() throws SQLException {
        log.info("nullsAreSortedHigh");
        return false;
    }

    @Override
    public boolean nullsAreSortedLow() throws SQLException {
        log.info("nullsAreSortedLow");
        return false;
    }

    @Override
    public boolean nullsAreSortedAtStart() throws SQLException {
        log.info("nullsAreSortedAtStart");
        return false;
    }

    @Override
    public boolean nullsAreSortedAtEnd() throws SQLException {
        log.info("nullsAreSortedAtEnd");
        return false;
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        log.info("getDatabaseProductName");
        return "Yearning-MYSQL";
    }

    @Override
    public String getDatabaseProductVersion() throws SQLException {
        log.info("getDatabaseProductVersion");
        return "release";
    }

    @Override
    public String getDriverName() throws SQLException {
        log.info("getDriverName");
        return "Yearning-MYSQL";
    }

    @Override
    public String getDriverVersion() throws SQLException {
        return "Yearning-MYSQL-1.0";
    }

    @Override
    public int getDriverMajorVersion() {
        return 1;
    }

    @Override
    public int getDriverMinorVersion() {
        return 0;
    }

    @Override
    public boolean usesLocalFiles() throws SQLException {
        log.info("usesLocalFiles");
        return false;
    }

    @Override
    public boolean usesLocalFilePerTable() throws SQLException {
        log.info("usesLocalFilePerTable");
        return false;
    }

    @Override
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        log.info("supportsMixedCaseIdentifiers");
        return true;
    }

    @Override
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        log.info("storesUpperCaseIdentifiers");
        return false;
    }

    @Override
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        log.info("storesLowerCaseIdentifiers");
        return false;
    }

    @Override
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        log.info("storesMixedCaseIdentifiers");
        return true;
    }

    @Override
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        log.info("supportsMixedCaseQuotedIdentifiers");
        return true;
    }

    @Override
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        log.info("storesUpperCaseQuotedIdentifiers");
        return true;
    }

    @Override
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        log.info("storesLowerCaseQuotedIdentifiers");
        return false;
    }

    @Override
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        log.info("storesMixedCaseQuotedIdentifiers");
        return true;
    }

    @Override
    public String getIdentifierQuoteString() throws SQLException {
        log.info("getIdentifierQuoteString");
        return "`";
    }

    @Override
    public String getSQLKeywords() throws SQLException {
        log.info("getSQLKeywords");
        if (mysqlKeywords != null) {
            return mysqlKeywords;
        }

        synchronized (this.getConnection()) {
            // double check, maybe it's already set
            if (mysqlKeywords != null) {
                return mysqlKeywords;
            }

            Set<String> mysqlKeywordSet = new TreeSet<>();
            StringBuilder mysqlKeywordsBuffer = new StringBuilder();

            Collections.addAll(mysqlKeywordSet, MYSQL_KEYWORDS);
            mysqlKeywordSet.removeAll(SQL2003_KEYWORDS);

            for (String keyword : mysqlKeywordSet) {
                mysqlKeywordsBuffer.append(",").append(keyword);
            }

            mysqlKeywords = mysqlKeywordsBuffer.substring(1);
            return mysqlKeywords;
        }
    }

    @Override
    public String getNumericFunctions() throws SQLException {
        log.info("getNumericFunctions");
        return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,"
                + "POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
    }

    @Override
    public String getStringFunctions() throws SQLException {
        log.info("getStringFunctions");
        return "ASCII,BIN,BIT_LENGTH,CHAR,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONV,ELT,EXPORT_SET,FIELD,FIND_IN_SET,HEX,INSERT,"
                + "INSTR,LCASE,LEFT,LENGTH,LOAD_FILE,LOCATE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH,MID,OCT,OCTET_LENGTH,ORD,POSITION,"
                + "QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SPACE,STRCMP,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING,"
                + "SUBSTRING_INDEX,TRIM,UCASE,UPPER";
    }

    @Override
    public String getSystemFunctions() throws SQLException {
        log.info("getSystemFunctions");
        return "DATABASE,USER,SYSTEM_USER,SESSION_USER,PASSWORD,ENCRYPT,LAST_INSERT_ID,VERSION";
    }

    @Override
    public String getTimeDateFunctions() throws SQLException {
        log.info("getTimeDateFunctions");
        return "DAYOFWEEK,WEEKDAY,DAYOFMONTH,DAYOFYEAR,MONTH,DAYNAME,MONTHNAME,QUARTER,WEEK,YEAR,HOUR,MINUTE,SECOND,PERIOD_ADD,"
                + "PERIOD_DIFF,TO_DAYS,FROM_DAYS,DATE_FORMAT,TIME_FORMAT,CURDATE,CURRENT_DATE,CURTIME,CURRENT_TIME,NOW,SYSDATE,"
                + "CURRENT_TIMESTAMP,UNIX_TIMESTAMP,FROM_UNIXTIME,SEC_TO_TIME,TIME_TO_SEC";
    }

    @Override
    public String getSearchStringEscape() throws SQLException {
        log.info("getSearchStringEscape");
        return "\\";
    }

    @Override
    public String getExtraNameCharacters() throws SQLException {
        log.info("getExtraNameCharacters");
        return "#@";
    }

    @Override
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        log.info("supportsAlterTableWithAddColumn");
        return true;
    }

    @Override
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        log.info("supportsAlterTableWithDropColumn");
        return true;
    }

    @Override
    public boolean supportsColumnAliasing() throws SQLException {
        log.info("supportsColumnAliasing");
        return true;
    }

    @Override
    public boolean nullPlusNonNullIsNull() throws SQLException {
        log.info("nullPlusNonNullIsNull");
        return true;
    }

    @Override
    public boolean supportsConvert() throws SQLException {
        log.info("supportsConvert");
        return false;
    }

    @Override
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        log.info("supportsConvert");
        return JdbcType.supportsConvert(fromType, toType);
    }

    @Override
    public boolean supportsTableCorrelationNames() throws SQLException {
        log.info("supportsTableCorrelationNames");
        return true;
    }

    @Override
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        log.info("supportsDifferentTableCorrelationNames");
        return true;
    }

    @Override
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        log.info("supportsExpressionsInOrderBy");
        return true;
    }

    @Override
    public boolean supportsOrderByUnrelated() throws SQLException {
        log.info("supportsOrderByUnrelated");
        return false;
    }

    @Override
    public boolean supportsGroupBy() throws SQLException {
        log.info("supportsGroupBy");
        return true;
    }

    @Override
    public boolean supportsGroupByUnrelated() throws SQLException {
        log.info("supportsGroupByUnrelated");
        return true;
    }

    @Override
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        log.info("supportsGroupByBeyondSelect");
        return true;
    }

    @Override
    public boolean supportsLikeEscapeClause() throws SQLException {
        log.info("supportsLikeEscapeClause");
        return true;
    }

    @Override
    public boolean supportsMultipleResultSets() throws SQLException {
        log.info("supportsMultipleResultSets");
        return true;
    }

    @Override
    public boolean supportsMultipleTransactions() throws SQLException {
        log.info("supportsMultipleTransactions");
        return true;
    }

    @Override
    public boolean supportsNonNullableColumns() throws SQLException {
        log.info("supportsNonNullableColumns");
        return true;
    }

    @Override
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        log.info("supportsMinimumSQLGrammar");
        return true;
    }

    @Override
    public boolean supportsCoreSQLGrammar() throws SQLException {
        log.info("supportsCoreSQLGrammar");
        return true;
    }

    @Override
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        log.info("supportsExtendedSQLGrammar");
        return false;
    }

    @Override
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        log.info("supportsANSI92EntryLevelSQL");
        return true;
    }

    @Override
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        log.info("supportsANSI92IntermediateSQL");
        return false;
    }

    @Override
    public boolean supportsANSI92FullSQL() throws SQLException {
        log.info("supportsANSI92FullSQL");
        return false;
    }

    @Override
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        log.info("supportsIntegrityEnhancementFacility");
        return false;
    }

    @Override
    public boolean supportsOuterJoins() throws SQLException {
        log.info("supportsOuterJoins");
        return true;
    }

    @Override
    public boolean supportsFullOuterJoins() throws SQLException {
        log.info("supportsFullOuterJoins");
        return false;
    }

    @Override
    public boolean supportsLimitedOuterJoins() throws SQLException {
        log.info("supportsLimitedOuterJoins");
        return true;
    }

    @Override
    public String getSchemaTerm() throws SQLException {
        log.info("getSchemaTerm");
        return "";
    }

    @Override
    public String getProcedureTerm() throws SQLException {
        log.info("getProcedureTerm");
        return "PROCEDURE";
    }

    @Override
    public String getCatalogTerm() throws SQLException {
        log.info("getCatalogTerm");
        return "database";
    }

    @Override
    public boolean isCatalogAtStart() throws SQLException {
        log.info("isCatalogAtStart");
        return true;
    }

    @Override
    public String getCatalogSeparator() throws SQLException {
        return ".";
    }

    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        log.info("supportsSchemasInDataManipulation");
        return false;
    }

    @Override
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        log.info("supportsSchemasInProcedureCalls");
        return false;
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        log.info("supportsSchemasInTableDefinitions");
        return false;
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        log.info("supportsSchemasInIndexDefinitions");
        return false;
    }

    @Override
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        log.info("supportsSchemasInPrivilegeDefinitions");
        return false;
    }

    @Override
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        log.info("supportsCatalogsInDataManipulation");
        return true;
    }

    @Override
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        log.info("supportsCatalogsInPrivilegeDefinitions");
        return true;
    }

    @Override
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        log.info("supportsCatalogsInTableDefinitions");
        return true;
    }

    @Override
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        log.info("supportsCatalogsInIndexDefinitions");
        return true;
    }

    @Override
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        log.info("supportsCatalogsInPrivilegeDefinitions");
        return true;
    }

    @Override
    public boolean supportsPositionedDelete() throws SQLException {
        log.info("supportsPositionedDelete");
        return false;
    }

    @Override
    public boolean supportsPositionedUpdate() throws SQLException {
        log.info("supportsPositionedUpdate");
        return false;
    }

    @Override
    public boolean supportsSelectForUpdate() throws SQLException {
        log.info("supportsSelectForUpdate");
        return true;
    }

    @Override
    public boolean supportsStoredProcedures() throws SQLException {
        log.info("supportsStoredProcedures");
        return true;
    }

    @Override
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        log.info("supportsSubqueriesInComparisons");
        return true;
    }

    @Override
    public boolean supportsSubqueriesInExists() throws SQLException {
        log.info("supportsSubqueriesInExists");
        return true;
    }

    @Override
    public boolean supportsSubqueriesInIns() throws SQLException {
        log.info("supportsSubqueriesInIns");
        return true;
    }

    @Override
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        log.info("supportsSubqueriesInQuantifieds");
        return false;
    }

    @Override
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        log.info("supportsCorrelatedSubqueries");
        return true;
    }

    @Override
    public boolean supportsUnion() throws SQLException {
        log.info("supportsUnion");
        return true;
    }

    @Override
    public boolean supportsUnionAll() throws SQLException {
        log.info("supportsUnionAll");
        return true;
    }

    @Override
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        log.info("supportsOpenCursorsAcrossCommit");
        return false;
    }

    @Override
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        log.info("supportsOpenCursorsAcrossRollback");
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        log.info("supportsOpenStatementsAcrossCommit");
        return false;
    }

    @Override
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        log.info("supportsOpenStatementsAcrossRollback");
        return false;
    }

    @Override
    public int getMaxBinaryLiteralLength() throws SQLException {
        log.info("getMaxBinaryLiteralLength");
        return 16777208;
    }

    @Override
    public int getMaxCharLiteralLength() throws SQLException {
        log.info("getMaxCharLiteralLength");
        return 16777208;
    }

    @Override
    public int getMaxColumnNameLength() throws SQLException {
        log.info("getMaxColumnNameLength");
        return 64;
    }

    @Override
    public int getMaxColumnsInGroupBy() throws SQLException {
        log.info("getMaxColumnsInGroupBy");
        return 64;
    }

    @Override
    public int getMaxColumnsInIndex() throws SQLException {
        log.info("supportsSubqueriesInIns");
        return 16;
    }

    @Override
    public int getMaxColumnsInOrderBy() throws SQLException {
        log.info("getMaxColumnsInOrderBy");
        return 64;
    }

    @Override
    public int getMaxColumnsInSelect() throws SQLException {
        log.info("getMaxColumnsInSelect");
        return 256;
    }

    @Override
    public int getMaxColumnsInTable() throws SQLException {
        log.info("getMaxColumnsInTable");
        return 512;
    }

    @Override
    public int getMaxConnections() throws SQLException {
        log.info("getMaxConnections");
        return 0;
    }

    @Override
    public int getMaxCursorNameLength() throws SQLException {
        log.info("getMaxCursorNameLength");
        return 64;
    }

    @Override
    public int getMaxIndexLength() throws SQLException {
        log.info("getMaxIndexLength");
        return 256;
    }

    @Override
    public int getMaxSchemaNameLength() throws SQLException {
        log.info("getMaxSchemaNameLength");
        return 0;
    }

    @Override
    public int getMaxProcedureNameLength() throws SQLException {
        log.info("getMaxProcedureNameLength");
        return 0;
    }

    @Override
    public int getMaxCatalogNameLength() throws SQLException {
        log.info("getMaxCatalogNameLength");
        return 32;
    }

    @Override
    public int getMaxRowSize() throws SQLException {
        log.info("getMaxRowSize");
        return Integer.MAX_VALUE - 8;
    }

    @Override
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        log.info("doesMaxRowSizeIncludeBlobs");
        return false;
    }

    @Override
    public int getMaxStatementLength() throws SQLException {
        log.info("getMaxStatementLength");
        return 65535 - 4;
    }

    @Override
    public int getMaxStatements() throws SQLException {
        log.info("getMaxStatements");
        return 0;
    }

    @Override
    public int getMaxTableNameLength() throws SQLException {
        log.info("getMaxTableNameLength");
        return 64;
    }

    @Override
    public int getMaxTablesInSelect() throws SQLException {
        log.info("getMaxTablesInSelect");
        return 256;
    }

    @Override
    public int getMaxUserNameLength() throws SQLException {
        log.info("getMaxUserNameLength");
        return 16;
    }

    @Override
    public int getDefaultTransactionIsolation() throws SQLException {
        log.info("getDefaultTransactionIsolation");
        return Connection.TRANSACTION_NONE;
    }

    @Override
    public boolean supportsTransactions() throws SQLException {
        log.info("supportsTransactions");
        return false;
    }

    @Override
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        log.info("supportsTransactionIsolationLevel");
        return Connection.TRANSACTION_NONE == level;
    }

    @Override
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        log.info("supportsDataDefinitionAndDataManipulationTransactions");
        return false;
    }

    @Override
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        log.info("supportsDataManipulationTransactionsOnly");
        return false;
    }

    @Override
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        log.info("dataDefinitionCausesTransactionCommit");
        return true;
    }

    @Override
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        log.info("dataDefinitionIgnoredInTransactions");
        return false;
    }

    @Override
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        log.info("getProcedures");
        return new JdbcResultSet(null,new DefaultCursor(Collections.emptyList(),ColumnUtils.proceduresDefinition()));
    }

    @Override
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        log.info("getProcedureColumns");
        return new JdbcResultSet(null,new DefaultCursor(Collections.emptyList(),ColumnUtils.primaryKeysColumnDefinition()));
    }

    @Override
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        log.info("getTables:" + catalog + "|" + schemaPattern + "|" + tableNamePattern + "|" + Arrays.toString(types));
        final Statement statement = connection.createStatement();
        StringBuilder builder = new StringBuilder("SHOW FULL TABLES FROM " + catalog);
        if (StringUtils.isNotBlank(tableNamePattern)) {
            if (!"%".equals(tableNamePattern)) {
                String table = tableNamePattern;
                if (tableNamePattern.contains(",")) {
                    table = tableNamePattern.substring(0, tableNamePattern.indexOf(","));
                }
                builder.append(" LIKE ").append("`%").append(table).append("%`");
            }
        }
        ResultSet resultSet = statement.executeQuery(builder.toString());
        int tableTypeIndex = resultSet.findColumn("Table_type");
        List<Row> rows = Lists.newArrayList();
        while (resultSet.next()) {
            TableType tableType = TableType.getTableTypeCompliantWith(resultSet.getString(tableTypeIndex));
            Object[] rowVal = new Object[10];
            rowVal[0] = catalog;
            rowVal[1] = null;
            rowVal[2] = resultSet.getString(1);
            rowVal[3] = tableType.getName();
            rowVal[4] = null;

            rows.add(new RowImpl(rowVal));
        }
        resultSet.close();
        log.info(rows);
        return new JdbcResultSet(null, new DefaultCursor(rows, ColumnUtils.tableDefinition()));
    }

    @Override
    public ResultSet getSchemas() throws SQLException {
        return getSchemas(null, null);
    }

    @Override
    public ResultSet getCatalogs() throws SQLException {
        log.info("getCatalogs");
        List<String> schemaList = yearningService.getSchemas(connection.getSource());
        List<Row> rows = Lists.newArrayList();
        for (String schema : schemaList) {
            rows.add(new RowImpl(new Object[]{schema}));
        }
        log.info(rows);
        return new JdbcResultSet(null, new DefaultCursor(rows, ColumnUtils.databaseColumnDefinition()));
    }

    @Override
    public ResultSet getTableTypes() throws SQLException {
        log.info("getTableTypes");
        List<Row> rows = Lists.newArrayList();
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_TYPE", JdbcType.VARCHAR, 256));
        rows.add(new RowImpl(new Object[]{TableType.LOCAL_TEMPORARY.getName()}));
        rows.add(new RowImpl(new Object[]{TableType.SYSTEM_TABLE.getName()}));
        rows.add(new RowImpl(new Object[]{TableType.SYSTEM_VIEW.getName()}));
        rows.add(new RowImpl(new Object[]{TableType.TABLE.getName()}));
        rows.add(new RowImpl(new Object[]{TableType.VIEW.getName()}));
        return new JdbcResultSet(null, new DefaultCursor(rows, columns));
    }

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        log.info("getColumns:" + catalog + "|" + schemaPattern + "|" + tableNamePattern + "|" + columnNamePattern);
        List<String> tables = Lists.newArrayList();
        if (StringUtils.isBlank(tableNamePattern) || PERCENT_SYMBOL.equals(tableNamePattern) || tableNamePattern.contains(PERCENT_SYMBOL)) {
            ResultSet tablesResult = getTables(catalog, schemaPattern, tableNamePattern, null);
            while (tablesResult.next()) {
                tables.add(tablesResult.getString(1));
            }
        } else {
            tables.add(tableNamePattern.replace("\\", ""));
        }
        List<Row> rows = Lists.newArrayList();
        for (String table : tables) {
            final Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW FULL COLUMNS FROM " + catalog + "." + table);
            int index = 1;
            while (resultSet.next()) {
                TypeDescriptor typeDesc = new TypeDescriptor(resultSet.getString("Type"), resultSet.getString("Null"));
                Object[] row = new Object[24];
                row[0] = catalog;
                row[1] = null;
                row[2] = table;
                row[3] = resultSet.getString("Field");
                row[4] = typeDesc.getJdbcType().getJdbcType();
                row[5] = typeDesc.getJdbcType().getName();
                Integer columnSize = typeDesc.getColumnSize();
                if (columnSize == null) {              // COLUMN_SIZE
                    row[6] = null;
                } else {
                    String collation = resultSet.getString("Collation");
                    int minLength = 1;
                    if (collation != null) {
                        // not null collation could only be returned by server for character types, so we don't need to check type name
                        if (collation.contains("ucs2") || collation.contains("utf16")) {
                            minLength = 2;
                        } else if (collation.contains("utf32")) {
                            minLength = 4;
                        }
                    }
                    row[6] = minLength == 1 ? columnSize
                            : (Integer) (columnSize / minLength);
                }
                row[7] = 65535;
                row[8] = typeDesc.getDecimalDigits() == null ? null : typeDesc.getDecimalDigits().toString();
                row[9] = 10;
                row[10] = typeDesc.getNullability();
                row[11] = resultSet.getString("Comment");
                row[12] = resultSet.getString("Default");
                row[13] = '0';
                row[14] = '0';
                if (StringUtils.indexOfIgnoreCase(typeDesc.getJdbcType().getName(), "CHAR") != -1
                        || StringUtils.indexOfIgnoreCase(typeDesc.getJdbcType().getName(), "BLOB") != -1
                        || StringUtils.indexOfIgnoreCase(typeDesc.getJdbcType().getName(), "TEXT") != -1
                        || StringUtils.indexOfIgnoreCase(typeDesc.getJdbcType().getName(), "ENUM") != -1
                        || StringUtils.indexOfIgnoreCase(typeDesc.getJdbcType().getName(), "SET") != -1
                        || StringUtils.indexOfIgnoreCase(typeDesc.getJdbcType().getName(), "BINARY") != -1) {
                    row[15] = row[6];
                } else {
                    row[15] = null;
                }
                row[16] = index;
                row[17] = typeDesc.getNullability();
                row[18] = null;
                row[19] = null;
                row[20] = null;
                row[21] = null;
                row[22] = "";
                String extra = resultSet.getString("Extra");
                if (StringUtils.isNotBlank(extra)) {
                    row[22] = StringUtils.indexOfIgnoreCase(extra, "auto_increment") != -1 ? "YES" : "NO";
                    row[23] = StringUtils.indexOfIgnoreCase(extra, "generated") != -1 ? "YES" : "NO";
                }
                rows.add(new RowImpl(row));
                index++;
            }
        }
        log.info(rows);
        return new JdbcResultSet(null, new DefaultCursor(rows, ColumnUtils.tableColumnDefinition()));
    }

    @Override
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        log.info("getColumnPrivileges:" + table + "|" + schema + "|" + table + "|" + columnNamePattern);
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), ColumnUtils.columnPrivilegesDefinition()));
    }

    @Override
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        log.info("getTablePrivileges:" + catalog + "|" + schemaPattern + "|" + tableNamePattern);
        return null;
    }

    @Override
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return null;
    }

    @Override
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        log.info("getVersionColumns:" + catalog + "|" + schema + "|" + table);
        if (table == null) {
            throw new SQLException("table name is empty", "S1009");
        }
        String sql = "SHOW COLUMNS FROM " + table + " from " + catalog + " WHERE Extra LIKE '%on update CURRENT_TIMESTAMP%'";
        final Statement statement = connection.createStatement();
        statement.setMaxRows(0);
        statement.setFetchSize(0);
        List<Row> rows = Lists.newArrayList();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            TypeDescriptor typeDesc = new TypeDescriptor(resultSet.getString("Type"), resultSet.getString("Null"));
            Object[] row = new Object[8];
            row[0] = null;                                                                           // SCOPE is not used
            row[1] = resultSet.getBytes("Field");                                                      // COLUMN_NAME
            row[2] = typeDesc.getJdbcType().getJdbcType();            // DATA_TYPE
            row[3] = typeDesc.getJdbcType().getName();                                              // TYPE_NAME
            row[4] = typeDesc.getColumnSize() == null ? null : typeDesc.getColumnSize();       // COLUMN_SIZE
            row[5] = 65535;                                   // BUFFER_LENGTH
            row[6] = typeDesc.getDecimalDigits() == null ? null : typeDesc.getDecimalDigits(); // DECIMAL_DIGITS
            row[7] = Short.parseShort(DatabaseMetaData.versionColumnNotPseudo + "");  // PSEUDO_COLUMN
        }
        return new JdbcResultSet(statement, new DefaultCursor(rows, ColumnUtils.versionColumnsDefinition()));
    }

    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        log.info("getPrimaryKeys:" + catalog + "|" + schema + "|" + table);
        table = table.replace("\\", "");
        List<Row> rows = Lists.newArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SHOW KEYS FROM " + table + " from " + catalog);
        TreeMap<String, Object[]> sortMap = new TreeMap<>();
        while (resultSet.next()) {
            String keyType = resultSet.getString("Key_name");
            if (keyType != null) {
                if (keyType.equalsIgnoreCase("PRIMARY") || keyType.equalsIgnoreCase("PRI")) {
                    Object[] row = new Object[6];
                    String columnName = resultSet.getString("Column_name");
                    row[0] = catalog;
                    row[2] = resultSet.getString("Table");
                    row[3] = columnName;
                    row[4] = resultSet.getInt("Seq_in_index");
                    row[5] = keyType;
                    sortMap.put(columnName, row);
                }
            }
        }
        for (Object[] objects : sortMap.values()) {
            rows.add(new RowImpl(objects));
        }
        return new JdbcResultSet(null, new DefaultCursor(rows, ColumnUtils.primaryKeysColumnDefinition()));
    }

    @Override
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        log.info("getImportedKeys:" + catalog + "|" + schema + "|" + table);
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), ColumnUtils.importedKeysColumnDefinition()));
    }

    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        log.info("getExportedKeys:" + catalog + "|" + schema + "|" + table);
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), Collections.emptyList()));
    }

    @Override
    public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        log.info("getCrossReference:" + parentCatalog + "|" + parentSchema + "|" + parentTable);
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), Collections.emptyList()));
    }

    @Override
    public ResultSet getTypeInfo() throws SQLException {
        log.info("getTypeInfo");
        List<Row> rows = Lists.newArrayList();
        rows.add(new RowImpl(getTypeInfo("BIT")));
        rows.add(new RowImpl(getTypeInfo("BOOL")));
        rows.add(new RowImpl(getTypeInfo("TINYINT")));
        rows.add(new RowImpl(getTypeInfo("TINYINT UNSIGNED")));
        rows.add(new RowImpl(getTypeInfo("BIGINT")));
        rows.add(new RowImpl(getTypeInfo("BIGINT UNSIGNED")));
        rows.add(new RowImpl(getTypeInfo("LONG VARBINARY")));
        rows.add(new RowImpl(getTypeInfo("MEDIUMBLOB")));
        rows.add(new RowImpl(getTypeInfo("LONGBLOB")));
        rows.add(new RowImpl(getTypeInfo("BLOB")));
        rows.add(new RowImpl(getTypeInfo("VARBINARY")));
        rows.add(new RowImpl(getTypeInfo("TINYBLOB")));
        rows.add(new RowImpl(getTypeInfo("BINARY")));
        rows.add(new RowImpl(getTypeInfo("LONG VARCHAR")));
        rows.add(new RowImpl(getTypeInfo("MEDIUMTEXT")));
        rows.add(new RowImpl(getTypeInfo("LONGTEXT")));
        rows.add(new RowImpl(getTypeInfo("TEXT")));
        rows.add(new RowImpl(getTypeInfo("CHAR")));
        rows.add(new RowImpl(getTypeInfo("ENUM")));
        rows.add(new RowImpl(getTypeInfo("SET")));
        rows.add(new RowImpl(getTypeInfo("DECIMAL")));
        rows.add(new RowImpl(getTypeInfo("NUMERIC")));
        rows.add(new RowImpl(getTypeInfo("INTEGER")));
        rows.add(new RowImpl(getTypeInfo("INTEGER UNSIGNED")));
        rows.add(new RowImpl(getTypeInfo("INT")));
        rows.add(new RowImpl(getTypeInfo("INT UNSIGNED")));
        rows.add(new RowImpl(getTypeInfo("MEDIUMINT")));
        rows.add(new RowImpl(getTypeInfo("MEDIUMINT UNSIGNED")));
        rows.add(new RowImpl(getTypeInfo("SMALLINT")));
        rows.add(new RowImpl(getTypeInfo("SMALLINT UNSIGNED")));
        rows.add(new RowImpl(getTypeInfo("FLOAT")));
        rows.add(new RowImpl(getTypeInfo("DOUBLE")));
        rows.add(new RowImpl(getTypeInfo("DOUBLE PRECISION")));
        rows.add(new RowImpl(getTypeInfo("REAL")));
        rows.add(new RowImpl(getTypeInfo("VARCHAR")));
        rows.add(new RowImpl(getTypeInfo("TINYTEXT")));
        rows.add(new RowImpl(getTypeInfo("DATE")));
        rows.add(new RowImpl(getTypeInfo("YEAR")));
        rows.add(new RowImpl(getTypeInfo("TIME")));
        rows.add(new RowImpl(getTypeInfo("DATETIME")));
        rows.add(new RowImpl(getTypeInfo("TIMESTAMP")));
        return new JdbcResultSet(null, new DefaultCursor(rows, ColumnUtils.typeInfoDefinition()));
    }

    private Object[] getTypeInfo(String mysqlTypeName) throws SQLException {

        JdbcType mt = JdbcType.getByName(mysqlTypeName);
        Object[] rowVal = new Object[18];

        rowVal[0] = mysqlTypeName;                                                     // Type name
        rowVal[1] = Integer.toString(mt.getJdbcType()).getBytes();                          // JDBC Data type
        // JDBC spec reserved only 'int' type for precision, thus we need to cut longer values
        rowVal[2] = Integer.toString(mt.getPrecision() > Integer.MAX_VALUE ? Integer.MAX_VALUE : mt.getPrecision().intValue()).getBytes(); // Precision
        switch (mt) {
            case TINYBLOB:
            case BLOB:
            case MEDIUMBLOB:
            case LONGBLOB:
            case TINYTEXT:
            case TEXT:
            case MEDIUMTEXT:
            case LONGTEXT:
            case JSON:
            case BINARY:
            case VARBINARY:
            case CHAR:
            case VARCHAR:
            case ENUM:
            case SET:
            case DATE:
            case TIME:
            case DATETIME:
            case TIMESTAMP:
            case GEOMETRY:
            case UNKNOWN:
                rowVal[3] = "'";                                                       // Literal Prefix
                rowVal[4] = "'";                                                       // Literal Suffix
                break;
            default:
                rowVal[3] = "";                                                        // Literal Prefix
                rowVal[4] = "";                                                        // Literal Suffix
        }
        rowVal[5] = mt.getCreateParams();                                              // Create Params
        rowVal[6] = Integer.toString(DatabaseMetaData.typeNullable).getBytes();    // Nullable
        rowVal[7] = "true";                                                            // Case Sensitive
        rowVal[8] = Integer.toString(DatabaseMetaData.typeSearchable).getBytes();  // Searchable
        rowVal[9] = mt.isAllowed(JdbcType.FIELD_FLAG_UNSIGNED) ? "true" : "false";    // Unsignable
        rowVal[10] = "false";                                                          // Fixed Prec Scale
        rowVal[11] = "false";                                                          // Auto Increment
        rowVal[12] = mt.getName();                                                     // Locale Type Name
        switch (mt) {
            case DECIMAL:
            case DECIMAL_UNSIGNED:
            case DOUBLE:
            case DOUBLE_UNSIGNED:
                rowVal[13] = "-308";                                                   // Minimum Scale
                rowVal[14] = "308";                                                    // Maximum Scale
                break;
            case FLOAT:
            case FLOAT_UNSIGNED:
                rowVal[13] = "-38";                                                    // Minimum Scale
                rowVal[14] = "38";                                                     // Maximum Scale
                break;
            default:
                rowVal[13] = "0";                                                      // Minimum Scale
                rowVal[14] = "0";                                                      // Maximum Scale
        }

        rowVal[15] = "0";                                                              // SQL Data Type (not used)
        rowVal[16] = "0";                                                              // SQL DATETIME SUB (not used)
        rowVal[17] = "10";                                                             // NUM_PREC_RADIX (2 or 10)

        return rowVal;
    }

    @Override
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        log.info("getIndexInfo:" + catalog + "|" + schema + "|" + table + "|" + unique + "|" + approximate);
        table = table.replace("\\", "");
        ResultSet resultSet = connection.createStatement().executeQuery("SHOW INDEX FROM " + table + " FROM " + catalog);
        List<Row> rows = Lists.newArrayList();
        final SortedMap<IndexMetaDataKey, Row> sortedRows = new TreeMap<>();
        while (resultSet.next()) {
            Object[] row = new Object[14];
            row[0] = catalog;      // TABLE_CAT
            row[1] = null;            // TABLE_SCHEM
            row[2] = resultSet.getString("Table");                     // TABLE_NAME

            boolean indexIsUnique = resultSet.getInt("Non_unique") == 0;

            row[3] = !indexIsUnique ? "true" : "false";   // NON_UNIQUE
            row[4] = null;                                          // INDEX_QUALIFIER
            row[5] = resultSet.getString("Key_name");                  // INDEX_NAME
            short indexType = DatabaseMetaData.tableIndexOther;
            row[6] = Integer.toString(indexType).getBytes();        // TYPE
            row[7] = resultSet.getInt("Seq_in_index");              // ORDINAL_POSITION
            row[8] = resultSet.getString("Column_name");               // COLUMN_NAME
            row[9] = resultSet.getString("Collation");                 // ASC_OR_DESC

            long cardinality = resultSet.getLong("Cardinality");
            row[10] = cardinality;             // CARDINALITY
            row[11] = "0";                                     // PAGES
            row[12] = null;                                         // FILTER_CONDITION

            IndexMetaDataKey indexInfoKey = new IndexMetaDataKey(!indexIsUnique, indexType, resultSet.getString("Key_name").toLowerCase(),
                    resultSet.getShort("Seq_in_index"));

            if (unique) {
                if (indexIsUnique) {
                    sortedRows.put(indexInfoKey, new RowImpl(row));
                }
            } else {
                // All rows match
                sortedRows.put(indexInfoKey, new RowImpl(row));
            }
        }
        rows.addAll(sortedRows.values());
        return new JdbcResultSet(null, new DefaultCursor(rows, ColumnUtils.indexInfoColumnDefinition()));
    }

    protected class IndexMetaDataKey implements Comparable<IndexMetaDataKey> {
        Boolean columnNonUnique;
        Short columnType;
        String columnIndexName;
        Short columnOrdinalPosition;

        IndexMetaDataKey(boolean columnNonUnique, short columnType, String columnIndexName, short columnOrdinalPosition) {
            this.columnNonUnique = columnNonUnique;
            this.columnType = columnType;
            this.columnIndexName = columnIndexName;
            this.columnOrdinalPosition = columnOrdinalPosition;
        }

        @Override
        public int compareTo(IndexMetaDataKey indexInfoKey) {
            int compareResult;

            if ((compareResult = this.columnNonUnique.compareTo(indexInfoKey.columnNonUnique)) != 0) {
                return compareResult;
            }
            if ((compareResult = this.columnType.compareTo(indexInfoKey.columnType)) != 0) {
                return compareResult;
            }
            if ((compareResult = this.columnIndexName.compareTo(indexInfoKey.columnIndexName)) != 0) {
                return compareResult;
            }
            return this.columnOrdinalPosition.compareTo(indexInfoKey.columnOrdinalPosition);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj == this) {
                return true;
            }

            if (!(obj instanceof IndexMetaDataKey)) {
                return false;
            }
            return compareTo((IndexMetaDataKey) obj) == 0;
        }

        @Override
        public int hashCode() {
            assert false : "hashCode not designed";
            return 0;
        }
    }

    @Override
    public boolean supportsResultSetType(int type) throws SQLException {
        log.info("supportsResultSetType:" + type);
        return type == ResultSet.TYPE_FORWARD_ONLY || type == ResultSet.TYPE_SCROLL_INSENSITIVE;
    }

    @Override
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean ownInsertsAreVisible(int type) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean othersDeletesAreVisible(int type) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean othersInsertsAreVisible(int type) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean updatesAreDetected(int type) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean deletesAreDetected(int type) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean insertsAreDetected(int type) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean supportsBatchUpdates() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        log.info("getUDTs" + Arrays.toString(new Object[]{catalog, schemaPattern, typeNamePattern, types}));
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), ColumnUtils.UDTDefinition()));
    }

    @Override
    public Connection getConnection() throws SQLException {
        log.info("getConnection");
        return this.connection;
    }

    @Override
    public boolean supportsSavepoints() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return true;
    }

    @Override
    public boolean supportsNamedParameters() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public boolean supportsMultipleOpenResults() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return true;
    }

    @Override
    public boolean supportsGetGeneratedKeys() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return true;
    }

    @Override
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        log.info("getSuperTypes");
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TYPE_CAT", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("TYPE_SCHEM", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("TYPE_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("SUPERTYPE_CAT", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("SUPERTYPE_SCHEM", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("SUPERTYPE_NAME", JdbcType.CHAR, 32));
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), columns));
    }

    @Override
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        log.info("getSuperTables");
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_CAT", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("TABLE_SCHEM", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("TABLE_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("SUPERTABLE_NAME", JdbcType.CHAR, 32));
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), columns));
    }

    @Override
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        log.info("getAttributes");
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), ColumnUtils.attributesDefinition()));
    }

    @Override
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        log.info("supportsResultSetHoldability");
        return holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        log.info("getResultSetHoldability");
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public int getDatabaseMajorVersion() throws SQLException {
        log.info("getDatabaseMajorVersion");
        return 0;
    }

    @Override
    public int getDatabaseMinorVersion() throws SQLException {
        log.info("getDatabaseMinorVersion");
        return 0;
    }

    /**
     * 主要JDBC版本号
     *
     * @return
     * @throws SQLException
     */
    @Override
    public int getJDBCMajorVersion() throws SQLException {
        log.info("getJDBCMajorVersion");
        return 4;
    }

    /**
     * 次要JDBC版本号
     *
     * @return
     * @throws SQLException
     */
    @Override
    public int getJDBCMinorVersion() throws SQLException {
        log.info("getJDBCMinorVersion");
        return 2;
    }

    @Override
    public int getSQLStateType() throws SQLException {
        log.info("getSQLStateType");
        return DatabaseMetaData.sqlStateSQL99;
    }

    @Override
    public boolean locatorsUpdateCopy() throws SQLException {
        log.info("locatorsUpdateCopy");
        return false;
    }

    @Override
    public boolean supportsStatementPooling() throws SQLException {
        log.info("supportsStatementPooling");
        return false;
    }

    @Override
    public RowIdLifetime getRowIdLifetime() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return RowIdLifetime.ROWID_UNSUPPORTED;
    }

    @Override
    public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
        log.info("getSchemas:" + catalog + "|" + schemaPattern);
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), ColumnUtils.schemaColumnDefinition()));
    }


    @Override
    public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return true;
    }

    @Override
    public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public ResultSet getClientInfoProperties() throws SQLException {
        log.info("getClientInfoProperties");
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("NAME", JdbcType.VARCHAR, 255));
        columns.add(new JdbcColumn("MAX_LEN", JdbcType.INT, 10));
        columns.add(new JdbcColumn("DEFAULT_VALUE", JdbcType.VARCHAR, 255));
        columns.add(new JdbcColumn("DESCRIPTION", JdbcType.VARCHAR, 255));
        return new JdbcResultSet(null, new DefaultCursor(Collections.emptyList(), columns));
    }

    @Override
    public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
        log.info("getFunctions:" + catalog + "|" + schemaPattern + "|" + functionNamePattern);
        return new JdbcResultSet(null,new DefaultCursor(Collections.emptyList(),Collections.emptyList()));
    }

    @Override
    public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
        log.info("getFunctionColumns");
        return new JdbcResultSet(null,new DefaultCursor(Collections.emptyList(),Collections.emptyList()));
    }

    @Override
    public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        log.info("getPseudoColumns");
        return new JdbcResultSet(null,new DefaultCursor(Collections.emptyList(),Collections.emptyList()));
    }

    @Override
    public boolean generatedKeyAlwaysReturned() throws SQLException {
        log.info("generatedKeyAlwaysReturned");
        return true;
    }
}
