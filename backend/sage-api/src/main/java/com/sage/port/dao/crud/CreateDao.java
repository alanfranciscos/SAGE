package com.sage.port.dao.crud;

public interface CreateDao<T> {

    T save(final T entity);
}
