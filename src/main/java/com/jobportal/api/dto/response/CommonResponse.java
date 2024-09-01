package com.jobportal.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse {

    private boolean error;
    private String message;
}
