package com.sage.config.security;

import com.sage.dto.v1.caregiver.response.CaregiverResponseDto;
import com.sage.dto.v1.caregiver.response.CaregiverResponseFromPasswordTableDto;
import com.sage.services.caregiver.CaregiverServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class CustomCaregiverDetailsService implements UserDetailsService {

    @Autowired
    CaregiverServiceImpl caregiverServiceImpl;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CaregiverResponseDto caregiver = this.caregiverServiceImpl.findByEmailAndReturnsCaregiverResponseDto(username).orElseThrow(() -> new UsernameNotFoundException("Caregiver not found"));
        CaregiverResponseFromPasswordTableDto caregiverResponseFromPasswordTableDto = this.caregiverServiceImpl.getCaregiverFromPasswordTable(caregiver.token()).orElseThrow(() -> new UsernameNotFoundException("Caregiver from password table not found"));
        return new org.springframework.security.core.userdetails.User(caregiver.email(), caregiverResponseFromPasswordTableDto.caregiver_password(), new ArrayList<>());
    }
}
