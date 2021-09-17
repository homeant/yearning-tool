package com.github.homeant.yearning.jdbc.result;

import java.sql.SQLException;

public interface Cursor {
    JdbcColumn[] columns();

    default int columnSize() {
        return columns().length;
    }

    boolean next() throws SQLException;

    Object getValue(int column);

    int size();

    void close() throws SQLException;
}
