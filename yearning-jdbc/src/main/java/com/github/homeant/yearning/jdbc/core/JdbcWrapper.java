package com.github.homeant.yearning.jdbc.core;

import java.sql.SQLException;
import java.sql.Wrapper;

public interface JdbcWrapper extends Wrapper {
    @Override
    default boolean isWrapperFor(Class<?> iface) throws SQLException {
        if (!iface.isInstance(this)) {
            return false;
        }
        return iface.isAssignableFrom(getClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    default <T> T unwrap(Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) this;
        }
        throw new SQLException(this.getClass().getName() + " not unwrappable from " + iface.getName());
    }
}
