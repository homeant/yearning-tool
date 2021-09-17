package com.github.homeant.yearning.jdbc.core;

import com.github.homeant.yearning.jdbc.api.YearningService;
import com.github.homeant.yearning.jdbc.api.domain.QueryResp;
import com.github.homeant.yearning.jdbc.connection.JdbcConnection;
import com.github.homeant.yearning.jdbc.result.JdbcResultSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcStatement implements Statement, JdbcWrapper {

    private Log log = LogFactory.getLog(JdbcStatement.class);

    protected JdbcConnection connection;


    protected YearningService yearningService;

    private boolean isClosed = false;

    protected JdbcResultSet resultSet = null;

    private int maxRows = -1;

    private int fetchSize = -1;

    private boolean isPoolable = false;

    private int resultSetType;

    private int resultSetConcurrency;

    private int resultSetHoldability;

    private static final Pattern USE_PATTERN = Pattern.compile("use ([\\S]+)");

    public JdbcStatement(JdbcConnection connection, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        this.connection = connection;
        JdbcConnection unwrap = this.connection.unwrap(JdbcConnection.class);
        yearningService = unwrap.getYearningService();
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.resultSetHoldability = resultSetHoldability;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        sql = sql.replace("\n", " ").replace("\t", " ");
        log.info("executeQuery:" + sql);
        String catalog = this.connection.getCatalog();
        String source = this.connection.getSource();
        try {
            QueryResp queryResp = yearningService.query(source, catalog, sql);
            return new JdbcResultSet(this, connection.builderResult(queryResp));
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }


    @Override
    public int executeUpdate(String sql) throws SQLException {
        log.info("executeUpdate:" + sql);
        throw new SQLFeatureNotSupportedException("executeUpdate not supported");
    }

    @Override
    public void close() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        Connection connection = this.connection;
        if (connection == null || isClosed) {
            return;
        }
        this.isClosed = true;
        this.resultSet = null;
        this.connection = null;
        this.yearningService = null;

    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public int getMaxRows() throws SQLException {
        log.info("getMaxRows:" + this.maxRows);
        synchronized (this.getConnection()) {
            if (this.maxRows <= 0) {
                return 0;
            }
            return this.maxRows;
        }
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        log.info("setMaxRows:" + max);
        this.setLargeMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public void cancel() throws SQLException {
        log.info("cancel");

    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        sql = sql.replace("\n", " ").replace("\t", " ");
        Matcher usrMatcher = USE_PATTERN.matcher(sql);
        if (usrMatcher.find()) {
            this.connection.setCatalog(usrMatcher.group(1));
            return false;
        }
        log.info("execute:" + sql);
        try {
            QueryResp queryResp = yearningService.query(connection.getSource(), connection.getCatalog(), sql);
            resultSet = new JdbcResultSet(this, connection.builderResult(queryResp));
        } catch (Exception e) {
            throw new SQLException(e);
        }
        //指示ResultSet对象的类型的常数，其光标只能向前移动。
        return true;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        log.info("getResultSet");
        synchronized (this.getConnection()) {
            return (this.resultSet != null) ? resultSet : null;
        }
    }

    @Override
    public int getUpdateCount() throws SQLException {
        log.info("getUpdateCount");
        return Integer.parseInt(getLargeUpdateCount() + "");
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        if (this.resultSet == null) {
            return -1;
        }

        if (this.resultSet.hasRows()) {
            return -1;
        }
        return this.resultSet.getFetchSize();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        log.info("setFetchDirection:" + direction);
        if (ResultSet.FETCH_REVERSE != direction
                || ResultSet.FETCH_FORWARD != direction
                || ResultSet.FETCH_UNKNOWN != direction) {
            throw new SQLException("Invalid direction specified");
        }
    }

    @Override
    public int getFetchDirection() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return ResultSet.FETCH_FORWARD;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        log.info("setFetchSize:" + rows);
        this.fetchSize = rows;
    }

    @Override
    public int getFetchSize() throws SQLException {
        log.info("getFetchSize");
        return fetchSize;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public int getResultSetType() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        throw new SQLFeatureNotSupportedException("addBatch not supported");
    }

    @Override
    public void clearBatch() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        throw new SQLFeatureNotSupportedException("clearBatch not supported");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        throw new SQLFeatureNotSupportedException("Batching not supported");
    }

    @Override
    public Connection getConnection() throws SQLException {
        log.info("getConnection");
        return this.connection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        throw new SQLFeatureNotSupportedException("Generated keys not supported");
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        throw new SQLFeatureNotSupportedException("executeUpdate not supported");
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        throw new SQLFeatureNotSupportedException("executeUpdate not supported");
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        log.info("method:" + Thread.currentThread().getStackTrace()[1].getMethodName());
        throw new SQLFeatureNotSupportedException("executeUpdate not supported");
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        log.info("execute:" + sql + "|" + autoGeneratedKeys);
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        log.info("execute:" + sql + "|" + Arrays.toString(columnIndexes));
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        log.info("execute:" + sql + "|" + Arrays.toString(columnNames));
        return false;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        log.info("getResultSetHoldability");
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    @Override
    public boolean isClosed() throws SQLException {
        log.info("isClosed");
        return isClosed;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        log.info("setPoolable");
        this.isPoolable = poolable;
    }

    @Override
    public boolean isPoolable() throws SQLException {
        log.info("isPoolable");
        return isPoolable;
    }


    @Override
    public void closeOnCompletion() throws SQLException {
        log.info("closeOnCompletion");

    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        log.info("isCloseOnCompletion");
        return false;
    }

    @Override
    public void setLargeMaxRows(long max) throws SQLException {
        log.info("setLargeMaxRows:" + max);
        synchronized (this.getConnection()) {
            if (max == 0) {
                max = -1;
            }
            this.maxRows = (int) max;
        }
    }

    @Override
    public long getLargeMaxRows() throws SQLException {
        log.info("getLargeMaxRows:" + getMaxRows());
        return getMaxRows();
    }


}
