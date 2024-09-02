package com.jobportal.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ErrorResponse {

    private final boolean success = false;
    private String message;
    private int statusCode;
}
