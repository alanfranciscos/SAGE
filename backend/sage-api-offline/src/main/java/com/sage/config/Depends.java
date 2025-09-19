package com.sage.config;

import java.sql.Connection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sage.config.settings.Settings;
import com.sage.dao.assist.OldAssistDaoImpl;
import com.sage.dao.resident.OldResidentDaoImpl;
import com.sage.dao.resident.ResidentHeaderDaoImpl;
import com.sage.dao.resident.control.ControlResidentDaoImpl;
import com.sage.dao.resident.emergency.ResidentEmergencyContactDaoImpl;
import com.sage.dao.sse.AssistSseDaoImpl;
import com.sage.port.dao.assist.AssistDao;
import com.sage.port.dao.resident.ControlResidentDao;
import com.sage.port.dao.resident.ResidentDao;
import com.sage.port.dao.resident.ResidentEmergencyContactDao;
import com.sage.port.dao.resident.ResidentHeaderDao;
import com.sage.port.dao.sse.AssistSseDao;

@Configuration
public class Depends {

    @Bean
    public ResidentHeaderDao residentHeaderDao(final Connection connection) {
        return new ResidentHeaderDaoImpl(connection);
    }

    @Bean
    public ResidentDao residentDao(final Connection connection) {
        return new OldResidentDaoImpl(connection);
    }

    @Bean
    public ResidentEmergencyContactDao residentEmergencyContactDao(final Connection connection) {
        return new ResidentEmergencyContactDaoImpl(connection);
    }

    @Bean
    public ControlResidentDao controlResidentDao(final Connection connection) {
        return new ControlResidentDaoImpl(connection);
    }

    @Bean
    public AssistDao assistDao(final Connection connection) {
        return new OldAssistDaoImpl(connection);
    }

    @Bean
    public AssistSseDao assistSseDao(final Settings settings) {
        return new AssistSseDaoImpl(settings);
    }

}
