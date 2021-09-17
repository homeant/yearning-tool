package com.github.homeant.yearning.jdbc.utils;

import com.github.homeant.yearning.jdbc.result.JdbcColumn;
import com.github.homeant.yearning.jdbc.result.JdbcType;
import com.google.common.collect.Lists;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class ColumnUtils {
    public static List<JdbcColumn> databaseColumnDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_CAT", JdbcType.VARCHAR, "INFORMATION_SCHEMA", EMPTY, "CATALOGS", EMPTY, 0));
        return columns;
    }

    public static List<JdbcColumn> schemaColumnDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_SCHEM", JdbcType.CHAR, EMPTY, EMPTY, "SCHEMATA", EMPTY, 0));
        columns.add(new JdbcColumn("TABLE_CATALOG", JdbcType.CHAR, EMPTY, EMPTY, "SCHEMATA", EMPTY, 0));
        return columns;
    }


    public static List<JdbcColumn> attributesDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TYPE_CAT", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("TYPE_SCHEM", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("TYPE_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("ATTR_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("DATA_TYPE", JdbcType.SMALLINT, 32));
        columns.add(new JdbcColumn("ATTR_TYPE_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("ATTR_SIZE", JdbcType.INT, 32));
        columns.add(new JdbcColumn("DECIMAL_DIGITS", JdbcType.INT, 32));
        columns.add(new JdbcColumn("NUM_PREC_RADIX", JdbcType.INT, 32));
        columns.add(new JdbcColumn("NULLABLE ", JdbcType.INT, 32));
        columns.add(new JdbcColumn("REMARKS", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("ATTR_DEF", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("SQL_DATA_TYPE", JdbcType.INT, 32));
        columns.add(new JdbcColumn("SQL_DATETIME_SUB", JdbcType.INT, 32));
        columns.add(new JdbcColumn("CHAR_OCTET_LENGTH", JdbcType.INT, 32));
        columns.add(new JdbcColumn("ORDINAL_POSITION", JdbcType.INT, 32));
        columns.add(new JdbcColumn("IS_NULLABLE", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("SCOPE_CATALOG", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("SCOPE_SCHEMA", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("SCOPE_TABLE", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("SOURCE_DATA_TYPE", JdbcType.SMALLINT, 32));
        return columns;
    }

    public static List<JdbcColumn> tableDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_CAT", JdbcType.VARCHAR, 255));
        columns.add(new JdbcColumn("TABLE_SCHEM", JdbcType.VARCHAR, 0));
        columns.add(new JdbcColumn("TABLE_NAME", JdbcType.VARCHAR, 255));
        columns.add(new JdbcColumn("TABLE_TYPE", JdbcType.VARCHAR, 5));
        columns.add(new JdbcColumn("REMARKS", JdbcType.VARCHAR, 0));
        columns.add(new JdbcColumn("TYPE_CAT", JdbcType.VARCHAR, 0));
        columns.add(new JdbcColumn("TYPE_SCHEM", JdbcType.VARCHAR, 0));
        columns.add(new JdbcColumn("TYPE_NAME", JdbcType.VARCHAR, 0));
        columns.add(new JdbcColumn("SELF_REFERENCING_COL_NAME", JdbcType.VARCHAR, 0));
        columns.add(new JdbcColumn("REF_GENERATION", JdbcType.VARCHAR, 0));
        return columns;
    }

    public static List<JdbcColumn> tableColumnDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_CAT", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("TABLE_SCHEM", JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("TABLE_NAME", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("COLUMN_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("DATA_TYPE", JdbcType.INT, 5));
        columns.add(new JdbcColumn("TYPE_NAME", JdbcType.CHAR, 16));
        columns.add(new JdbcColumn("COLUMN_SIZE", JdbcType.INT, Integer.toString(Integer.MAX_VALUE).length()));
        columns.add(new JdbcColumn("BUFFER_LENGTH", JdbcType.INT, 10));
        columns.add(new JdbcColumn("DECIMAL_DIGITS", JdbcType.INT, 10));
        columns.add(new JdbcColumn("NUM_PREC_RADIX", JdbcType.INT, 10));
        columns.add(new JdbcColumn("NULLABLE", JdbcType.INT, 10));
        columns.add(new JdbcColumn("REMARKS", JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("COLUMN_DEF", JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("SQL_DATA_TYPE", JdbcType.INT, 10));
        columns.add(new JdbcColumn("SQL_DATETIME_SUB", JdbcType.INT, 10));
        columns.add(new JdbcColumn("CHAR_OCTET_LENGTH", JdbcType.INT, Integer.toString(Integer.MAX_VALUE).length()));
        columns.add(new JdbcColumn("ORDINAL_POSITION", JdbcType.INT, 10));
        columns.add(new JdbcColumn("IS_NULLABLE", JdbcType.CHAR, 3));
        columns.add(new JdbcColumn("SCOPE_CATALOG", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("SCOPE_SCHEMA", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("SCOPE_TABLE", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("SOURCE_DATA_TYPE", JdbcType.SMALLINT, 10));
        columns.add(new JdbcColumn("IS_AUTOINCREMENT", JdbcType.CHAR, 3));
        columns.add(new JdbcColumn("IS_GENERATEDCOLUMN", JdbcType.CHAR, 3));
        return columns;
    }

    public static List<JdbcColumn> primaryKeysColumnDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_CAT", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("TABLE_SCHEM", JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("TABLE_NAME", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("COLUMN_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("KEY_SEQ", JdbcType.SMALLINT, 5));
        columns.add(new JdbcColumn("PK_NAME", JdbcType.CHAR, 32));
        return columns;
    }

    public static List<JdbcColumn> importedKeysColumnDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("PKTABLE_CAT", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("PKTABLE_SCHEM", JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("PKTABLE_NAME", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("PKCOLUMN_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("FKTABLE_CAT", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("FKTABLE_SCHEM", JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("FKTABLE_NAME", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("FKCOLUMN_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("KEY_SEQ", JdbcType.SMALLINT, 2));
        columns.add(new JdbcColumn("UPDATE_RULE", JdbcType.SMALLINT, 2));
        columns.add(new JdbcColumn("DELETE_RULE", JdbcType.SMALLINT, 2));
        columns.add(new JdbcColumn("FK_NAME", JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("PK_NAME", JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("DEFERRABILITY", JdbcType.SMALLINT, 2));
        return columns;
    }

    public static List<JdbcColumn> indexInfoColumnDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_CAT", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("TABLE_SCHEM", JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("TABLE_NAME", JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("NON_UNIQUE", JdbcType.BOOLEAN, 4));
        columns.add(new JdbcColumn("INDEX_QUALIFIER", JdbcType.CHAR, 1));
        columns.add(new JdbcColumn("INDEX_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("TYPE", JdbcType.SMALLINT, 32));
        columns.add(new JdbcColumn("ORDINAL_POSITION", JdbcType.SMALLINT, 5));
        columns.add(new JdbcColumn("COLUMN_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("ASC_OR_DESC", JdbcType.CHAR, 1));
        columns.add(new JdbcColumn("CARDINALITY", JdbcType.BIGINT, 20));
        columns.add(new JdbcColumn("PAGES", JdbcType.BIGINT, 20));
        columns.add(new JdbcColumn("FILTER_CONDITION", JdbcType.CHAR, 32));
        return columns;
    }

    public static List<JdbcColumn> typeInfoDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TYPE_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("DATA_TYPE", JdbcType.INT, 5));
        columns.add(new JdbcColumn("PRECISION", JdbcType.INT, 10));
        columns.add(new JdbcColumn("LITERAL_PREFIX", JdbcType.CHAR, 4));
        columns.add(new JdbcColumn("LITERAL_SUFFIX", JdbcType.CHAR, 4));
        columns.add(new JdbcColumn("CREATE_PARAMS", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("NULLABLE", JdbcType.SMALLINT, 5));
        columns.add(new JdbcColumn("CASE_SENSITIVE", JdbcType.BOOLEAN, 3));
        columns.add(new JdbcColumn("SEARCHABLE", JdbcType.SMALLINT, 3));
        columns.add(new JdbcColumn("UNSIGNED_ATTRIBUTE", JdbcType.BOOLEAN, 3));
        columns.add(new JdbcColumn("FIXED_PREC_SCALE", JdbcType.BOOLEAN, 3));
        columns.add(new JdbcColumn("AUTO_INCREMENT", JdbcType.BOOLEAN, 3));
        columns.add(new JdbcColumn("LOCAL_TYPE_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("MINIMUM_SCALE", JdbcType.SMALLINT, 5));
        columns.add(new JdbcColumn("MAXIMUM_SCALE", JdbcType.SMALLINT, 5));
        columns.add(new JdbcColumn("SQL_DATA_TYPE", JdbcType.INT, 10));
        columns.add(new JdbcColumn("SQL_DATETIME_SUB", JdbcType.INT, 10));
        columns.add(new JdbcColumn("NUM_PREC_RADIX", JdbcType.INT, 10));
        return columns;
    }

    public static List<JdbcColumn> versionColumnsDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("SCOPE", JdbcType.SMALLINT, 5));
        columns.add(new JdbcColumn("COLUMN_NAME", JdbcType.CHAR, 32));
        columns.add(new JdbcColumn("DATA_TYPE", JdbcType.INT, 5));
        columns.add(new JdbcColumn("TYPE_NAME", JdbcType.CHAR, 16));
        columns.add(new JdbcColumn("COLUMN_SIZE", JdbcType.INT, 16));
        columns.add(new JdbcColumn("BUFFER_LENGTH", JdbcType.INT, 16));
        columns.add(new JdbcColumn("DECIMAL_DIGITS", JdbcType.SMALLINT, 16));
        columns.add(new JdbcColumn("PSEUDO_COLUMN", JdbcType.SMALLINT, 5));
        return columns;
    }

    public static List<JdbcColumn> tablePrivilegesDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_CAT", JdbcType.CHAR, 64));
        columns.add(new JdbcColumn("TABLE_SCHEM", JdbcType.CHAR, 1));
        columns.add(new JdbcColumn("TABLE_NAME", JdbcType.CHAR, 64));
        columns.add(new JdbcColumn("GRANTOR", JdbcType.CHAR, 77));
        columns.add(new JdbcColumn("GRANTEE", JdbcType.CHAR, 77));
        columns.add(new JdbcColumn("PRIVILEGE", JdbcType.CHAR, 64));
        columns.add(new JdbcColumn("IS_GRANTABLE", JdbcType.CHAR, 3));
        return columns;
    }

    public static List<JdbcColumn> columnPrivilegesDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TABLE_CAT", JdbcType.CHAR, 64));
        columns.add(new JdbcColumn("TABLE_SCHEM", JdbcType.CHAR, 1));
        columns.add(new JdbcColumn("TABLE_NAME", JdbcType.CHAR, 64));
        columns.add(new JdbcColumn("COLUMN_NAME", JdbcType.CHAR, 64));
        columns.add(new JdbcColumn("GRANTOR", JdbcType.CHAR, 77));
        columns.add(new JdbcColumn("GRANTEE", JdbcType.CHAR, 77));
        columns.add(new JdbcColumn("PRIVILEGE", JdbcType.CHAR, 64));
        columns.add(new JdbcColumn("IS_GRANTABLE", JdbcType.CHAR, 3));
        return columns;
    }

    public static List<JdbcColumn> UDTDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("TYPE_CAT",JdbcType.VARCHAR, 32));
        columns.add(new JdbcColumn("TYPE_SCHEM",JdbcType.VARCHAR, 32));
        columns.add(new JdbcColumn("TYPE_NAME",JdbcType.VARCHAR, 32));
        columns.add(new JdbcColumn("CLASS_NAME",JdbcType.VARCHAR, 32));
        columns.add(new JdbcColumn("DATA_TYPE",JdbcType.INT, 10));
        columns.add(new JdbcColumn("REMARKS",JdbcType.VARCHAR, 32));
        columns.add(new JdbcColumn("BASE_TYPE",JdbcType.SMALLINT, 10));
        return columns;
    }

    public static List<JdbcColumn> proceduresDefinition() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn("PROCEDURE_CAT",JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("PROCEDURE_SCHEM",JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("PROCEDURE_NAME",JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("reserved1",JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("reserved2",JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("reserved3",JdbcType.CHAR, 0));
        columns.add(new JdbcColumn("REMARKS",JdbcType.CHAR, 255));
        columns.add(new JdbcColumn("PROCEDURE_TYPE",JdbcType.SMALLINT, 6));
        columns.add(new JdbcColumn("SPECIFIC_NAME",JdbcType.CHAR, 255));
        return columns;
    }

    protected List<JdbcColumn>  procedureColumnsFields() {
        List<JdbcColumn> columns = Lists.newArrayList();
        columns.add(new JdbcColumn( "PROCEDURE_CAT",JdbcType.CHAR, 512));
        columns.add(new JdbcColumn( "PROCEDURE_SCHEM",JdbcType.CHAR, 512));
        columns.add(new JdbcColumn( "PROCEDURE_NAME",JdbcType.CHAR, 512));
        columns.add(new JdbcColumn( "COLUMN_NAME",JdbcType.CHAR, 512));
        columns.add(new JdbcColumn( "COLUMN_TYPE",JdbcType.CHAR, 64));
        columns.add(new JdbcColumn( "DATA_TYPE",JdbcType.SMALLINT, 6));
        columns.add(new JdbcColumn( "TYPE_NAME",JdbcType.CHAR, 64));
        columns.add(new JdbcColumn( "PRECISION",JdbcType.INT, 12));
        columns.add(new JdbcColumn( "LENGTH",JdbcType.INT, 12));
        columns.add(new JdbcColumn( "SCALE",JdbcType.SMALLINT, 12));
        columns.add(new JdbcColumn( "RADIX",JdbcType.SMALLINT, 6));
        columns.add(new JdbcColumn( "NULLABLE",JdbcType.SMALLINT, 6));
        columns.add(new JdbcColumn( "REMARKS",JdbcType.CHAR, 512));
        columns.add(new JdbcColumn( "COLUMN_DEF",JdbcType.CHAR, 512));
        columns.add(new JdbcColumn( "SQL_DATA_TYPE",JdbcType.INT, 12));
        columns.add(new JdbcColumn( "SQL_DATETIME_SUB",JdbcType.INT, 12));
        columns.add(new JdbcColumn( "CHAR_OCTET_LENGTH",JdbcType.INT, 12));
        columns.add(new JdbcColumn( "ORDINAL_POSITION",JdbcType.INT, 12));
        columns.add(new JdbcColumn( "IS_NULLABLE",JdbcType.CHAR, 512));
        columns.add(new JdbcColumn( "SPECIFIC_NAME",JdbcType.CHAR, 512));
        return columns;
    }


}
