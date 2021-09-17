package com.github.homeant.yearning.jdbc.result;

import java.sql.SQLException;
import java.util.List;

public class DefaultCursor implements Cursor {

    private final Row[] rows;

    private final JdbcColumn[] columns;

    private Row row;

    private int current = -1;

    private boolean close = false;

    public DefaultCursor(Row[] rows, JdbcColumn[] columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public DefaultCursor(List<Row> rows, List<JdbcColumn> columns) {
        this.rows = rows.toArray(new Row[0]);
        this.columns = columns.toArray(new JdbcColumn[0]);
    }

    public static DefaultCursor emptyCursor() {
        return new DefaultCursor(new Row[]{}, new JdbcColumn[]{});
    }


    @Override
    public JdbcColumn[] columns() {
        return columns;
    }

    @Override
    public boolean next() throws SQLException {
        if (current < rows.length - 1) {
            current++;
            this.row = rows[current];
            return true;
        }
        return false;
    }

    @Override
    public Object getValue(int column) {
        return row.getValue(column);
    }

    @Override
    public int size() {
        return rows.length;
    }

    private void checkClose() throws SQLException {
        if (close) {
            throw new SQLException("this cursor is closed");
        }
    }

    @Override
    public void close() throws SQLException {
        close = true;
        row = null;
    }
}
