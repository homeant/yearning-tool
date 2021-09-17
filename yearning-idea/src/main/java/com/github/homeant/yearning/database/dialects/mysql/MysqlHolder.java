package com.github.homeant.yearning.database.dialects.mysql;

import com.github.homeant.yearning.Constants;
import com.github.homeant.yearning.Icons;
import com.intellij.database.Dbms;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;
import com.intellij.util.ConcurrencyUtil;

public final class MysqlHolder {
    public static final Dbms DBMS;

    static {
        DBMS = Dbms.create(Constants.YEARNING_MY_SQL, Icons.LOGO);
    }
}
