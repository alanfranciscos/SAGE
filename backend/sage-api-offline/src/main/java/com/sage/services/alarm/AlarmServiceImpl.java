package com.sage.services.alarm;

import com.sage.dto.v1.alarm.request.CreateAlarmRequestDto;
import com.sage.dto.v1.alarm.request.UpdateAlarmPortRequestDto;
import com.sage.dto.v1.alarm.request.UpdateAlarmRequestDto;
import com.sage.exception.NotFoundException;
import com.sage.model.alarm.Alarm;
import com.sage.port.dao.alarm.AlarmDao;
import com.sage.port.services.alarm.AlarmService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AlarmServiceImpl implements AlarmService {

    private final AlarmDao alarmDao;

    public AlarmServiceImpl(AlarmDao alarmDao) {
        this.alarmDao = alarmDao;
    }

    @Override
    public Alarm create(CreateAlarmRequestDto request) {
        Alarm alarm = new Alarm();
        alarm.setId(UUID.randomUUID());
        alarm.setModel(request.model());
        alarm.setStatus(request.status());
        alarm.setIpAddress(request.ipAddress());
        alarm.setMacAddress(request.macAddress());
        alarm.setAccount(request.account());
        alarm.setSerialNumber(request.serialNumber());
        alarm.setPort(request.port());
        alarmDao.save(alarm);
        return alarm;
    }

    @Override
    public Alarm update(UUID id, UpdateAlarmRequestDto request) {
        return alarmDao.findById(id).map(existingAlarm -> {
            updateAlarmFields(existingAlarm, request);
            alarmDao.update(existingAlarm);
            return existingAlarm;
        }).orElseThrow(() -> new NotFoundException("Alarm with id " + id + " not found"));
    }

    @Override
    public Alarm updateBySerialNumber(String serialNumber, UpdateAlarmRequestDto request) {
        return alarmDao.findBySerialNumber(serialNumber).map(existingAlarm -> {
            updateAlarmFields(existingAlarm, request);
            alarmDao.update(existingAlarm);
            return existingAlarm;
        }).orElseThrow(() -> new NotFoundException("Alarm with serial number " + serialNumber + " not found"));
    }

    @Override
    public void updatePortBySerialNumber(String serialNumber, UpdateAlarmPortRequestDto request) {
        Alarm alarm = alarmDao.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new NotFoundException("Alarm with serial number " + serialNumber + " not found"));
        alarm.setPort(request.port());
        alarmDao.updatePort(alarm);
    }

    @Override
    public Optional<Alarm> getBySerialNumber(String serialNumber) {
        return alarmDao.findBySerialNumber(serialNumber);
    }

    @Override
    public void delete(UUID id) {
        if (alarmDao.findById(id).isEmpty()) {
            throw new NotFoundException("Alarm with id " + id + " not found");
        }
        alarmDao.delete(id);
    }

    private void updateAlarmFields(Alarm alarm, UpdateAlarmRequestDto request) {
        alarm.setModel(request.model());
        alarm.setStatus(request.status());
        alarm.setIpAddress(request.ipAddress());
        alarm.setMacAddress(request.macAddress());
        alarm.setAccount(request.account());
        alarm.setSerialNumber(request.serialNumber());
    }
}
