package com.jobportal.api.dto.request.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntrospectRequest {

    private String token;
}
