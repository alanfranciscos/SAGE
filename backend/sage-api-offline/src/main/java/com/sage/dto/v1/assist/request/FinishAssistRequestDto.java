package com.sage.dto.v1.assist.request;

import lombok.Data;

@Data
public class FinishAssistRequestDto {
    private String caregiverToken;
    private String details;
}
