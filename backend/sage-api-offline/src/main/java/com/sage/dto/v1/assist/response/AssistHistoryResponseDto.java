package com.sage.dto.v1.assist.response;

import java.util.UUID;

import com.sage.model.assist.SeverityLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssistHistoryResponseDto {

    private UUID assistId;
    private String fullName;
    private Integer age;
    private String residentialUnit;
    private String elapsedTime;
    private SeverityLevel severityLevel;
    private String status;
    private String details;
    private String imageData;
}
