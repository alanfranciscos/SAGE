package com.sage.config;

import java.sql.Connection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sage.dao.resident.ResidentDaoImpl;
import com.sage.dao.resident.ResidentHeaderDaoImpl;
import com.sage.port.dao.resident.ResidentDao;
import com.sage.port.dao.resident.ResidentHeaderDao;

@Configuration
public class Depends {

    @Bean
    public ResidentHeaderDao residentHeaderDao(final Connection connection) {
        return new ResidentHeaderDaoImpl(connection);
    }

    @Bean
    public ResidentDao residentDao(final Connection connection) {
        return new ResidentDaoImpl(connection);
    }

}
