package com.vitalysukhinin.financial_system;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@Service
public class AuthUtil {

    @Value("${test.email}")
    private String emailValue;
    @Value("${test.password}")
    private String passwordValue;

    public static String email;
    public static String password;

    @PostConstruct
    public void init() {
        email = emailValue;
        password = passwordValue;
    }

    public static RequestPostProcessor auth() {
        return httpBasic(email, password);
    }
}
