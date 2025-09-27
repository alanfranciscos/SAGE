package com.sage.port.services.alarm;

import com.sage.dto.v1.alarm.request.CreateAlarmRequestDto;
import com.sage.dto.v1.alarm.request.UpdateAlarmRequestDto;
import com.sage.model.alarm.Alarm;

import java.util.Optional;
import java.util.UUID;

public interface AlarmService {
    Alarm create(CreateAlarmRequestDto request);
    Alarm update(UUID id, UpdateAlarmRequestDto request);
    Alarm updateBySerialNumber(String serialNumber, UpdateAlarmRequestDto request);
    Optional<Alarm> getBySerialNumber(String serialNumber);
    void delete(UUID id);
}
