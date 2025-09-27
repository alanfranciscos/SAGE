package com.sage.controller.v1.alarm;

import com.sage.dto.v1.alarm.request.CreateAlarmRequestDto;
import com.sage.dto.v1.alarm.request.UpdateAlarmRequestDto;
import com.sage.dto.v1.alarm.response.AlarmResponseDto;
import com.sage.model.alarm.Alarm;
import com.sage.port.services.alarm.AlarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @PostMapping
    public ResponseEntity<AlarmResponseDto> create(@RequestBody CreateAlarmRequestDto request) {
        Alarm createdAlarm = alarmService.create(request);
        return ResponseEntity.status(201).body(createdAlarm.toResponseDto());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlarmResponseDto> update(@PathVariable UUID id, @RequestBody UpdateAlarmRequestDto request) {
        Alarm updatedAlarm = alarmService.update(id, request);
        return ResponseEntity.ok(updatedAlarm.toResponseDto());
    }

    @PutMapping("/serial/{serialNumber}")
    public ResponseEntity<AlarmResponseDto> updateBySerialNumber(@PathVariable String serialNumber, @RequestBody UpdateAlarmRequestDto request) {
        Alarm updatedAlarm = alarmService.updateBySerialNumber(serialNumber, request);
        return ResponseEntity.ok(updatedAlarm.toResponseDto());
    }

    @GetMapping("/serial/{serialNumber}")
    public ResponseEntity<AlarmResponseDto> getBySerialNumber(@PathVariable String serialNumber) {
        return alarmService.getBySerialNumber(serialNumber)
                .map(alarm -> ResponseEntity.ok(alarm.toResponseDto()))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        alarmService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
