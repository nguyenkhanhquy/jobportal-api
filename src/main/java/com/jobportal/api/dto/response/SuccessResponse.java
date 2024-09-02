package com.jobportal.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuccessResponse <T> {

    private final boolean success = true;
    private String message;
    private T result;
    private final int statusCode = 200;
}
