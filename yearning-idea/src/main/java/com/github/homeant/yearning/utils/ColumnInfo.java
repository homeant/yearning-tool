package com.github.homeant.yearning.utils;

import javax.swing.table.TableColumn;

public class ColumnInfo {

    public static TableColumn newColumn(String column){
        TableColumn tableColumn = new TableColumn();
        tableColumn.setHeaderValue(column);
        return tableColumn;
    }
}
