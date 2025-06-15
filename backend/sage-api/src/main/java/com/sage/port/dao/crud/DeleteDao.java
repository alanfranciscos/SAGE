package com.sage.port.dao.crud;

public interface DeleteDao<ID> {

    void deleteById(final ID id);
}
