package com.github.homeant.yearning.jdbc.result;

import java.util.Arrays;

public class RowImpl implements Row {

    private final Object[] row;

    public RowImpl(Object[] row) {
        this.row = row;
    }

    @Override
    public Object getValue(int column) {
        return row[column];
    }

    @Override
    public String toString() {
        return "RowImpl{" +
                "row=" + Arrays.toString(row) +
                '}';
    }
}
