package com.sage.port.dao.crud;

public interface CrudDao<T, ID> extends
        CreateDao<T>,
        ReadDao<T, ID>,
        UpdateDao<T, ID>,
        DeleteDao<ID> {

}
