package com.sage.config;

import java.sql.Connection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sage.dao.assist.CaregiverAssignmentResidentDaoImpl;
import com.sage.dao.controlresident.ControlResidentDaoImpl;
import com.sage.dao.resident.ResidentDaoImpl;
import com.sage.port.dao.assist.CaregiverAssignmentResidentDao;
import com.sage.port.dao.controlresident.ControlResidentDao;
import com.sage.port.dao.resident.ResidentDao;

@Configuration
public class Depends {

    @Bean
    public CaregiverAssignmentResidentDao getCaregiverAssignmentResidentDao(final Connection connection) {
        return new CaregiverAssignmentResidentDaoImpl(connection);
    }

    @Bean
    public ControlResidentDao getControlResidentDao(final Connection connection) {
        return new ControlResidentDaoImpl(connection);
    }

    @Bean
    public ResidentDao getResidentDao(final Connection connection) {
        return new ResidentDaoImpl(connection);
    }
}
