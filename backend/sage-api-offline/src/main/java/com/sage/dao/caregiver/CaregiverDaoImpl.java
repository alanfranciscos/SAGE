package com.sage.dao.caregiver;

import java.sql.Connection;
import java.util.logging.Logger;

public class CaregiverDaoImpl {

    private static final Logger logger = Logger.getLogger(CaregiverDaoImpl.class.getName());

    private final Connection connection;

    public CaregiverDaoImpl(Connection connection) {
        this.connection = connection;
    }
}
