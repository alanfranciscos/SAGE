package com.sage.port.dao.alarm;

import com.sage.model.alarm.Alarm;
import java.util.Optional;
import java.util.UUID;

public interface AlarmDao {
    void save(Alarm alarm);
    void update(Alarm alarm);

    void updatePort(Alarm alarm);

    Optional<Alarm> findBySerialNumber(String serialNumber);
    Optional<Alarm> findById(UUID id);
    void delete(UUID id);
}
