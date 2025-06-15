package com.sage.port.dao.crud;

import java.util.List;
import java.util.Optional;

public interface ReadDao<T, ID> {

    Optional<T> findById(final ID id);

    List<T> readAll();

}
