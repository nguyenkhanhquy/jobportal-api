package com.jobportal.api.config;

import com.jobportal.api.dto.request.auth.IntrospectRequest;
import com.jobportal.api.exception.CustomException;
import com.jobportal.api.service.AuthService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerkey}")
    private String jwtSignerKey;

    private NimbusJwtDecoder nimbusJwtDecoder;

    private final AuthService authService;

    @Autowired
    public CustomJwtDecoder(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            IntrospectRequest introspectRequest = new IntrospectRequest();
            introspectRequest.setToken(token);
            authService.introspect(introspectRequest);
        } catch (CustomException | ParseException | JOSEException e) {
            throw new BadJwtException(e.getMessage());
        }

        if (nimbusJwtDecoder == null) {
            initializeDecoder();
        }

        return nimbusJwtDecoder.decode(token);
    }

    private void initializeDecoder() {
        SecretKey secretKey = new SecretKeySpec(jwtSignerKey.getBytes(), "HS512");
        nimbusJwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
