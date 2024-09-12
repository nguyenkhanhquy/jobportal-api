package com.jobportal.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuccessResponse <T> {

    private final boolean success = true;
    private String message;
    private T result;
    private final int statusCode = 200;
}
