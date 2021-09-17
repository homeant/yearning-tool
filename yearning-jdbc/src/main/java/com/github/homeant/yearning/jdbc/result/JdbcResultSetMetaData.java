package com.github.homeant.yearning.jdbc.result;

import com.github.homeant.yearning.jdbc.core.JdbcWrapper;
import lombok.extern.apachecommons.CommonsLog;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@CommonsLog
public class JdbcResultSetMetaData implements ResultSetMetaData, JdbcWrapper {

    private final JdbcColumn[] columns;

    public JdbcResultSetMetaData(JdbcColumn[] columns) {
        this.columns = columns;
    }


    @Override
    public int getColumnCount() throws SQLException {
        
        return columns.length;
    }

    @Override
    public boolean isAutoIncrement(int column) throws SQLException {
        
        return false;
    }

    @Override
    public boolean isCaseSensitive(int column) throws SQLException {
        
        return true;
    }

    @Override
    public boolean isSearchable(int column) throws SQLException {
        
        return true;
    }

    @Override
    public boolean isCurrency(int column) throws SQLException {
        
        return false;
    }

    @Override
    public int isNullable(int column) throws SQLException {
        
        return columnNullableUnknown;
    }

    @Override
    public boolean isSigned(int column) throws SQLException {
        
        return JdbcType.isSigned(getColumn(column).getJdbcType());
    }

    @Override
    public int getColumnDisplaySize(int column) throws SQLException {
        
        return 0;
    }

    @Override
    public String getColumnLabel(int column) throws SQLException {
        
        JdbcColumn jdbcColumn = getColumn(column);
        return jdbcColumn.getLabel()==null?jdbcColumn.getName():jdbcColumn.getLabel();
    }

    @Override
    public String getColumnName(int column) throws SQLException {
        
        return getColumn(column).getName();
    }

    @Override
    public String getSchemaName(int column) throws SQLException {
        
        return getColumn(column).getSchema();
    }

    @Override
    public int getPrecision(int column) throws SQLException {
        
        return getColumn(column).getLength();
    }

    @Override
    public int getScale(int column) throws SQLException {
        
        return getColumn(column).getScale();
    }

    @Override
    public String getTableName(int column) throws SQLException {
        
        return getColumn(column).getTable();
    }

    @Override
    public String getCatalogName(int column) throws SQLException {
        
        return getColumn(column).getCatalog();
    }

    @Override
    public int getColumnType(int column) throws SQLException {
        
        return getColumn(column).getJdbcType().getJdbcType();
    }

    @Override
    public String getColumnTypeName(int column) throws SQLException {
        
        return  getColumn(column).getJdbcType().getName();
    }

    @Override
    public boolean isReadOnly(int column) throws SQLException {
        
        return true;
    }

    @Override
    public boolean isWritable(int column) throws SQLException {
        
        return false;
    }

    @Override
    public boolean isDefinitelyWritable(int column) throws SQLException {
        
        return false;
    }

    @Override
    public String getColumnClassName(int column) throws SQLException {
        
        return  getColumn(column).getJdbcType().getClassName();
    }

    private JdbcColumn getColumn(int columnIndex){
        return this.columns[columnIndex-1];
    }
}
