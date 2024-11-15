package com.jobportal.api.util;

import com.jobportal.api.exception.CustomException;
import com.jobportal.api.exception.EnumException;
import com.jobportal.api.model.user.User;
import com.jobportal.api.repository.UserRepository;
import org.springframework.security.core.Authentication;

public class AuthUtil {

    private AuthUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static User getAuthenticatedUser(UserRepository userRepository) {
        Authentication authentication = SecurityUtil.getAuthenticatedUser();

        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(EnumException.USER_NOT_FOUND);
        }

        return user;
    }
}
