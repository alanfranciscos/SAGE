package com.sage.model.alarm;

import com.sage.dto.v1.alarm.response.AlarmResponseDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    private UUID id;
    private String model;
    private String status;
    private String ipAddress;
    private String macAddress;
    private String account;
    private String serialNumber;
    private int port;

    public AlarmResponseDto toResponseDto() {
        return new AlarmResponseDto(
            this.id,
            this.model,
            this.status,
            this.ipAddress,
            this.macAddress,
            this.account,
            this.serialNumber,
            this.port
        );
    }
}