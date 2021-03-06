package com.github.homeant.yearning.jdbc.result;

import com.github.homeant.yearning.jdbc.core.JdbcWrapper;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@CommonsLog
public class JdbcResultSet implements ResultSet, JdbcWrapper {

    private Cursor cursor;

    private final Statement statement;

    private int rowNumber;

    private boolean wasNull = false;
    private boolean wasLast = false;

    private int fetchSize = 0;

    public JdbcResultSet(Statement statement, Cursor cursor) {
        this.cursor = cursor;
        this.fetchSize = cursor.size();
        this.statement = statement;
    }


    @Override
    public boolean next() throws SQLException {
        if (cursor.next()) {
            rowNumber++;
            return true;
        }
        wasLast = true;
        return false;
    }

    @Override
    public void close() throws SQLException {
        if (statement != null) {
            statement.close();
        }
        cursor.close();
    }

    @Override
    public boolean wasNull() throws SQLException {
        return wasNull;
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        Object value = getObject(columnIndex);
        if (value == null) {
            return null;
        }
        return Objects.toString(value);
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        Object value = getObject(columnIndex);
        if (value == null) {
            return false;
        }
        return Boolean.valueOf(Objects.toString(value));
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        Object object = getObject(columnIndex);
        if (object == null) {
            return 0;
        }
        return Byte.parseByte(Objects.toString(object));
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        Object value = getObject(columnIndex);
        if (value == null) {
            return 0;
        }
        return Short.parseShort(Objects.toString(value));
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        Object object = getObject(columnIndex);
        if (object == null) {
            return 0;
        }
        return Integer.parseInt(Objects.toString(object));
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        Object value = getObject(columnIndex);
        if (value == null) {
            return 0;
        }
        return Long.parseLong(Objects.toString(value));
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        Object object = getObject(columnIndex);
        if (object == null) {
            return 0;
        }
        return Float.parseFloat(Objects.toString(object));
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        Object object = getObject(columnIndex);
        if (object == null) {
            return 0;
        }
        return Double.parseDouble(Objects.toString(object));
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        Object object = getObject(columnIndex);
        if (object == null) {
            return null;
        }
        return new BigDecimal(Objects.toString(object)).setScale(scale);
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return new byte[0];
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return getString(findColumn(columnLabel));
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        return false;
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        return 0;
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        return getShort(findColumn(columnLabel));
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return getInt(findColumn(columnLabel));
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        return getLong(findColumn(columnLabel));
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        return getFloat(findColumn(columnLabel));
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        return getDouble(findColumn(columnLabel));
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        int column = findColumn(columnLabel);
        return getBigDecimal(column, scale);
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        return new byte[0];
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public String getCursorName() throws SQLException {
        return null;
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return new JdbcResultSetMetaData(this.cursor.columns());
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return getObjectInternal(columnIndex - 1);
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return getObject(findColumn(columnLabel));
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        for (int i = 1; i <= this.cursor.columns().length; i++) {
            JdbcColumn column = this.cursor.columns()[i - 1];
            if (StringUtils.equalsIgnoreCase(column.getName(), columnLabel) || StringUtils.equalsIgnoreCase(column.getFullName(), columnLabel)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {

        return null;
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {

        return null;
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        Object object = getObject(columnIndex);
        if (object == null) {
            return null;
        }
        return new BigDecimal(Objects.toString(object));
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return getBigDecimal(findColumn(columnLabel));
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {

        return rowNumber == 0;
    }

    @Override
    public boolean isAfterLast() throws SQLException {

        return rowNumber > 0 && wasLast;
    }

    @Override
    public boolean isFirst() throws SQLException {

        return rowNumber == 1;
    }

    @Override
    public boolean isLast() throws SQLException {

        throw new SQLFeatureNotSupportedException("isLast not supported");
    }

    @Override
    public void beforeFirst() throws SQLException {

        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public void afterLast() throws SQLException {

        throw new SQLException("ResultSet is forward-only");

    }

    @Override
    public boolean first() throws SQLException {

        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public boolean last() throws SQLException {

        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public int getRow() throws SQLException {
        return rowNumber;
    }

    @Override
    public boolean absolute(int row) throws SQLException {

        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public boolean relative(int rows) throws SQLException {

        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public boolean previous() throws SQLException {

        throw new SQLException("ResultSet is forward-only");
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        if (direction != FETCH_FORWARD) {
            throw new SQLException("Fetch direction must be FETCH_FORWARD");
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return FETCH_FORWARD;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        if (rows < 0) {
            throw new SQLException("Rows is negative");
        }
//        if (rows != getFetchSize()) {
//            throw new SQLException("Fetch size cannot be changed");
//        }
        this.fetchSize = rows;
    }

    @Override
    public int getFetchSize() throws SQLException {
        return fetchSize;
    }

    @Override
    public int getType() throws SQLException {
        return TYPE_FORWARD_ONLY;
    }

    @Override
    public int getConcurrency() throws SQLException {
        return CONCUR_READ_ONLY;
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public boolean rowInserted() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNull(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateByte(int columnIndex, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateShort(int columnIndex, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateInt(int columnIndex, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateLong(int columnIndex, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateFloat(int columnIndex, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateDouble(int columnIndex, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateString(int columnIndex, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateDate(int columnIndex, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateTime(int columnIndex, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateObject(int columnIndex, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateObject(int columnIndex, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNull(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBoolean(String columnLabel, boolean x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateByte(String columnLabel, byte x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateShort(String columnLabel, short x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateInt(String columnLabel, int x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateLong(String columnLabel, long x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateFloat(String columnLabel, float x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateDouble(String columnLabel, double x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBigDecimal(String columnLabel, BigDecimal x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateString(String columnLabel, String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBytes(String columnLabel, byte[] x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateDate(String columnLabel, Date x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateTime(String columnLabel, Time x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateTimestamp(String columnLabel, Timestamp x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, int length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateObject(String columnLabel, Object x, int scaleOrLength) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateObject(String columnLabel, Object x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void insertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void deleteRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void refreshRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public Statement getStatement() throws SQLException {

        return statement;
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return null;
    }

    @Override
    public Ref getRef(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public Array getArray(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return null;
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        return null;
    }

    @Override
    public void updateRef(int columnIndex, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateRef(String columnLabel, Ref x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(String columnLabel, Blob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(int columnIndex, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(String columnLabel, Clob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateArray(int columnIndex, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateArray(String columnLabel, Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public RowId getRowId(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("RowId not supported");
    }

    @Override
    public RowId getRowId(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("RowId not supported");
    }

    @Override
    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public int getHoldability() throws SQLException {
        return HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return statement.isClosed();
    }

    @Override
    public void updateNString(int columnIndex, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNString(String columnLabel, String nString) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public NClob getNClob(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("NClob not supported");
    }

    @Override
    public NClob getNClob(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("NClob not supported");
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("SQLXML not supported");
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("SQLXML not supported");
    }

    @Override
    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("SQLXML not supported");
    }

    @Override
    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        throw new SQLFeatureNotSupportedException("SQLXML not supported");
    }

    @Override
    public String getNString(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("NString not supported");
    }

    @Override
    public String getNString(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("NString not supported");
    }

    @Override
    public Reader getNCharacterStream(int columnIndex) throws SQLException {
        throw new SQLFeatureNotSupportedException("NCharacterStream not supported");
    }

    @Override
    public Reader getNCharacterStream(String columnLabel) throws SQLException {
        throw new SQLFeatureNotSupportedException("NCharacterStream not supported");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        throw new SQLFeatureNotSupportedException("Writes not supported");
    }

    @Override
    public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
        Object object = getObject(columnIndex);
        return (T) object;
    }

    @Override
    public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
        return getObject(findColumn(columnLabel), type);
    }


    Object getObjectInternal(int columnIndex) throws SQLException {
        Object value = cursor.getValue(columnIndex);
        if (value == null) {
            wasNull = true;
        }
        return value;
    }

    public boolean hasRows() {
        return cursor.size() > 0;
    }
}
