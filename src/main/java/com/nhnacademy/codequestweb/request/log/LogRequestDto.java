package com.nhnacademy.codequestweb.request.log;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogRequestDto {
    private String projectName;
    private String projectVersion;
    private String logVersion;
    private String body;
    private String logSource;
    private String logType;
    private String host;
    private Long sendTime;
    private String logLevel;
}