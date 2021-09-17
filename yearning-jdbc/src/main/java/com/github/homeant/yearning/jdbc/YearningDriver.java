package com.github.homeant.yearning.jdbc;

import com.github.homeant.yearning.jdbc.connection.JdbcConnection;
import com.github.homeant.yearning.jdbc.utils.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class YearningDriver implements Driver {

    private Log log = LogFactory.getLog(YearningDriver.class);

    static final YearningDriver INSTANCE;

    static {
        try {
            INSTANCE = new YearningDriver();
            DriverManager.registerDriver(INSTANCE);
        } catch (SQLException ex) {
            throw new IllegalStateException("Unable to register " + YearningDriver.class.getName(), ex);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return new JdbcConnection(url, info);
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        log.info("acceptsURL:" + url);
        return url.contains("jdbc:yearning://");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        log.info("getPropertyInfo:" + url + "|" + JsonUtils.writeAsString(info));
        DriverPropertyInfo[] dpi = new DriverPropertyInfo[2];
        DriverPropertyInfo databaseTermProperty = new DriverPropertyInfo("databaseTerm", "CATALOG");
        databaseTermProperty.required = false;

        DriverPropertyInfo useInformationSchemaProperty = new DriverPropertyInfo("useInformationSchema", "false");
        useInformationSchemaProperty.required = false;

        dpi[0] = databaseTermProperty;
        dpi[1] = useInformationSchemaProperty;

        return dpi;

    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
