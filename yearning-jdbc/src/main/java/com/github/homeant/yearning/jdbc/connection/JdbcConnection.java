package com.github.homeant.yearning.jdbc.connection;

import com.github.homeant.yearning.jdbc.JdbcDatabaseMetaData;
import com.github.homeant.yearning.jdbc.api.YearningService;
import com.github.homeant.yearning.jdbc.api.domain.QueryResp;
import com.github.homeant.yearning.jdbc.core.*;
import com.github.homeant.yearning.jdbc.result.*;
import com.github.homeant.yearning.jdbc.utils.JsonUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JdbcConnection implements Connection, JdbcWrapper {

    private Log log = LogFactory.getLog(JdbcConnection.class);

    private final Session session;

    private boolean isClosed;

    private String source;

    private String catalog;

    private String schema;

    private final YearningService yearningService;

    private Map<String, Class<?>> typeMap;

    public JdbcConnection(String url, Properties info) {
        log.info("instantiate url:" + url + "|info:" + JsonUtils.writeAsString(info));
        session = new Session();
        session.setUrl(url);
        session.setUserName(info.getProperty("user", null));
        session.setPassword(info.getProperty("password", null));
        Pattern pattern = Pattern.compile("mock=([a-z]*)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            session.setMock(Boolean.parseBoolean(matcher.group(1)));
        } else {
            session.setMock(false);
        }
        Matcher sourceMatcher = Pattern.compile("source=([^&]+)").matcher(url);
        if (sourceMatcher.find()) {
            this.source = sourceMatcher.group(1);
        }
        yearningService = new YearningService(session);
    }

    @Override
    public Statement createStatement() throws SQLException {
        return createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        log.info("prepareStatement:" + sql);
        return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        log.info("method:prepareCall:" + sql);
        return null;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        log.info("method:nativeSQL:" + sql);
        return sql;
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        log.info("method:setAutoCommit");
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        log.info("method:getAutoCommit");
        return session.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        log.info("commit ....");
    }

    @Override
    public void rollback() throws SQLException {
        log.info("rollback ...");
    }

    @Override
    public void close() throws SQLException {
        log.info("close ...");
        isClosed = false;
    }

    @Override
    public boolean isClosed() throws SQLException {
        log.info("close:" + isClosed);
        return isClosed;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        log.info("getMetaData");
        return new JdbcDatabaseMetaData(this);
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        log.info("setReadOnly:" + readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        log.info("isReadOnly");
        return true;
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        log.info("setCatalog:" + catalog);
        this.catalog = catalog;
    }

    @Override
    public String getCatalog() throws SQLException {
        log.info("getCatalog:" + this.catalog);
        return this.catalog;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        log.info("setTransactionIsolation:" + level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        log.info("getTransactionIsolation");
        return TRANSACTION_READ_UNCOMMITTED;
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        log.info("getWarnings");
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        log.info("clearWarnings");
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return createStatement(resultSetType, resultSetConcurrency, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        log.info("prepareStatement:" + sql + "|" + resultSetType + "|" + resultSetConcurrency);
        return prepareStatement(sql, resultSetType, resultSetConcurrency, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        log.info("prepareCall:" + Arrays.toString(new Object[]{sql, resultSetType, resultSetConcurrency}));
        return null;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        log.info("getTypeMap");
        if (this.typeMap == null) {
            typeMap = Maps.newHashMap();
        }
        return typeMap;
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        log.info("setTypeMap");
        this.typeMap = map;
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        log.info("setHoldability:" + holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        log.info("getHoldability");
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        log.info("setSavepoint");
        throw new SQLFeatureNotSupportedException("Savepoint not Supported Yet");
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        log.info("setSavepoint:" + name);
        throw new SQLFeatureNotSupportedException("Savepoint not Supported Yet");
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        log.info("rollback");
        throw new SQLFeatureNotSupportedException("Savepoint not Supported Yet");
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        log.info("releaseSavepoint");
        throw new SQLFeatureNotSupportedException("releaseSavepoint not Supported Yet");
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return new JdbcStatement(this, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        log.info("prepareStatement:" + Arrays.toString(new Object[]{sql, resultSetType, resultSetConcurrency, resultSetHoldability}));
        return new JdbcPreparedStatement(this, sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        log.info("prepareCall:" + Arrays.toString(new Object[]{sql, resultSetType, resultSetConcurrency, resultSetHoldability}));
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        log.info("prepareStatement:" + Arrays.toString(new Object[]{sql, autoGeneratedKeys}));
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        log.info("prepareStatement:" + Arrays.toString(new Object[]{sql, columnIndexes}));
        return null;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        log.info("prepareStatement:" + Arrays.toString(new Object[]{sql, columnNames}));
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        log.info("createClob");
        throw new SQLFeatureNotSupportedException("Clob not supported yet");
    }

    @Override
    public Blob createBlob() throws SQLException {
        log.info("createBlob");
        throw new SQLFeatureNotSupportedException("Blob not supported yet");
    }

    @Override
    public NClob createNClob() throws SQLException {
        log.info("createNClob");
        throw new SQLFeatureNotSupportedException("NClob not supported yet");
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        log.info("createSQLXML");
        throw new SQLFeatureNotSupportedException("SQLXML not supported yet");
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        log.info("isValid");
        if (timeout < 0) {
            throw new SQLException("timeout is less than 0");
        }
        return !isClosed;
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        log.info("setClientInfo|" + name + ":" + value);
        this.session.getClientInfo().put(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        log.info("setClientInfo:" + JsonUtils.writeAsString(properties));
        this.session.getClientInfo().putAll(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        log.info("getClientInfo:" + name);
        return session.getClientInfo().getProperty(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        log.info("getClientInfo");
        return session.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        log.info("createArrayOf");
        throw new SQLFeatureNotSupportedException("Array not supported yet");
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        log.info("createStruct");
        throw new SQLFeatureNotSupportedException("Struct not supported yet");
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        log.info("setSchema:" + schema);
        this.schema = schema;
    }

    @Override
    public String getSchema() throws SQLException {
        log.info("getSchema");
        return schema;
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        log.info("abort");
        throw new SQLFeatureNotSupportedException("abort not supported yet");
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        log.info("setNetworkTimeout:" + milliseconds);
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        log.info("getNetworkTimeout");
        throw new SQLFeatureNotSupportedException("getNetworkTimeout not supported yet");
    }

    public Cursor builderResult(QueryResp queryResp) {
        List<QueryResp.Column> columns = queryResp.getColumns();
        if (CollectionUtils.isNotEmpty(columns)) {
            List<Row> rows = Lists.newArrayList();
            List<JdbcColumn> columnList = Lists.newArrayList();
            for (int i = 0; i < columns.size(); i++) {
                QueryResp.Column column = columns.get(i);
                columnList.add(new JdbcColumn(column.getKey(), JdbcType.VARCHAR, 255));
            }
            if (CollectionUtils.isNotEmpty(queryResp.getList())) {
                for (Map<String, Object> map : queryResp.getList()) {
                    Object[] rowVal = new Object[columns.size()];
                    for (int i = 0; i < columns.size(); i++) {
                        QueryResp.Column column = columns.get(i);
                        rowVal[i] = map.get(column.getKey());
                    }
                    rows.add(new RowImpl(rowVal));
                }
            }
            return new DefaultCursor(rows, columnList);
        } else {
            return DefaultCursor.emptyCursor();
        }
    }


    public Session getSession() {
        return session;
    }

    public YearningService getYearningService() {
        return this.yearningService;
    }

    public String getSource() {
        return source;
    }
}
