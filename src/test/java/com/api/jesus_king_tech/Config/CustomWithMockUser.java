package com.api.jesus_king_tech.Config;


import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomWithMockUserSecurityContextFactory.class)
public @interface CustomWithMockUser {

    String username() default "user";
    String password() default "password";
    String[] authorities() default {"USER"};


}
