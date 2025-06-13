package com.sage.port.dao.crud;

public interface UpdateDao<T, ID> {

    T updateInformation(final ID id, final T entity);
}
