package com.sage.controller.v1.alarm;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sage.dto.v1.alarm.request.CreateAlarmRequestDto;
import com.sage.dto.v1.alarm.request.UpdateAlarmPortRequestDto;
import com.sage.dto.v1.alarm.request.UpdateAlarmRequestDto;
import com.sage.dto.v1.alarm.response.AlarmResponseDto;
import com.sage.model.alarm.Alarm;
import com.sage.port.services.alarm.AlarmService;

@RestController
@RequestMapping("/api/v1/alarms")
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
    public ResponseEntity<AlarmResponseDto> updateBySerialNumber(@PathVariable String serialNumber,
            @RequestBody UpdateAlarmRequestDto request) {
        Alarm updatedAlarm = alarmService.updateBySerialNumber(serialNumber, request);
        return ResponseEntity.ok(updatedAlarm.toResponseDto());
    }

    @PatchMapping("/serial/{serialNumber}/port")
    public ResponseEntity<Void> updatePortBySerialNumber(@PathVariable String serialNumber, @RequestBody UpdateAlarmPortRequestDto request) {
        alarmService.updatePortBySerialNumber(serialNumber, request);
        return ResponseEntity.noContent().build();
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
