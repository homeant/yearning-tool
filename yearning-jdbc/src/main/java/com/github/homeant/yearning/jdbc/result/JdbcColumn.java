package com.github.homeant.yearning.jdbc.result;


import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import static org.apache.commons.lang3.StringUtils.EMPTY;


/**
 * @author huangtianhui
 */
@Data
public class JdbcColumn implements Serializable {
    private final String catalog;
    private final String schema;
    private final String table;
    private final String label;
    private final String name;
    private final int length;
    private final int scale;
    private final JdbcType jdbcType;

    /**
     *
     * @param name column name
     * @param jdbcType <code>JdbcType</code>
     * @param catalog catalog
     * @param schema schema
     * @param table table name
     * @param label column label
     * @param length column length
     * @param scale scale
     */
    public JdbcColumn(String name, JdbcType jdbcType, String catalog, String schema, String table, String label, int length, int scale) {
        this.name = name;
        this.jdbcType = jdbcType;
        this.catalog = catalog;
        this.schema = schema;
        this.table = table;
        this.label = label;
        this.length = length;
        this.scale = scale;
    }

    public JdbcColumn(String name, JdbcType jdbcType, String catalog, String schema, String table, String label, int length) {
        this(name, jdbcType, catalog, schema, table, label, length, 0);
    }

    public JdbcColumn(String name, JdbcType jdbcType, String label, int length) {
        this(name, jdbcType, EMPTY, EMPTY, EMPTY, label, length, 0);
    }

    public JdbcColumn(String name, JdbcType jdbcType, String label, int length, int scale) {
        this(name, jdbcType, EMPTY, EMPTY, EMPTY, label, length, scale);
    }

    public JdbcColumn(String name, JdbcType jdbcType, int length) {
        this(name, jdbcType, EMPTY, EMPTY, EMPTY, null, length, 0);
    }

    public JdbcColumn(String name, JdbcType jdbcType, int length, int scale) {
        this(name, jdbcType, EMPTY, EMPTY, EMPTY, null, length, scale);
    }

    public JdbcColumn(String name, JdbcType jdbcType) {
        this(name, jdbcType, EMPTY, EMPTY, EMPTY, null, 0, 0);
    }

    public String getFullName(){
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isNotBlank(table)){
            builder.append(table).append(".");
        }
        builder.append(name);
        return builder.toString();
    }
}
