package com.sage.dto.v1.reports.response;

import java.util.Map;

public record WeekdayCallsResponseDto(Map<String, Double> weekdayAverage) {
}
