package com.github.homeant.yearning.jdbc;


import com.google.common.collect.Lists;
import lombok.extern.apachecommons.CommonsLog;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommonsLog
public class YearningDriverTest {
    private Connection connect;

    @BeforeTest
    public void testDriver() throws SQLException {
        YearningDriver instance = YearningDriver.INSTANCE;
        Properties properties = new Properties();
        properties.put("user", "xxx");
        properties.put("password", "xxx");
        connect = instance.connect("jdbc:yearning://ysql.xxxx.com:443?mock=false&source=QA", properties);

    }

    @Test
    public void useTest() throws SQLException {
        Statement statement = connect.createStatement();
        boolean execute = statement.execute("use supplier");
        if (execute) {
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                log.info(resultSet.getString(1));
            }
        }
    }

    @Test
    public void getCatalogs() throws SQLException {
        DatabaseMetaData metaData = connect.getMetaData();
        ResultSet resultSet = metaData.getCatalogs();
        List<String> catalogList = Lists.newArrayList();
        while (resultSet.next()) {
            catalogList.add(resultSet.getString(1));
        }
        log.info("catalogList:" + catalogList);
        resultSet.close();
        connect.close();
    }

    @Test
    public void getSchemas() throws SQLException {
        DatabaseMetaData metaData = connect.getMetaData();
        ResultSet resultSet = metaData.getSchemas();
        List<String> schemaList = Lists.newArrayList();
        while (resultSet.next()) {
            schemaList.add(resultSet.getString(1));
        }
        log.info("schemaList:" + schemaList);
        resultSet.close();
        connect.close();
    }

    @Test
    public void getTables() throws SQLException {
        DatabaseMetaData metaData = connect.getMetaData();
        ResultSet columns = metaData.getTables("test", null, null, null);
        while (columns.next()) {
            log.info(columns.getString(3));
        }
    }


    @Test
    public void getColumns() throws SQLException {
        DatabaseMetaData metaData = connect.getMetaData();
        ResultSet columns = metaData.getColumns("test", null, "t_test", null);
        while (columns.next()) {
            log.info(columns.getString(4) + "|" + columns.getInt("COLUMN_SIZE"));

        }
    }

    @Test
    public void getPrimaryKeys() throws SQLException {
        DatabaseMetaData metaData = connect.getMetaData();
        ResultSet columns = metaData.getPrimaryKeys("test", null, "t_test");
        while (columns.next()) {
            log.info(columns.getString(4));
        }
    }

    @Test
    public void selectTest() throws SQLException {
        Statement statement = connect.createStatement();
        statement.execute("SELECT t.* FROM t_user t ");
        ResultSet resultSet = statement.getResultSet();
        while (resultSet.next()) {
            log.info("result:" + resultSet.getString(1));
        }
    }
}
