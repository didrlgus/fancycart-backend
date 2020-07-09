package com.shoppingmall.fancycart.auth;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserSecurityContextFactory.class)
public @interface WithUser {

    String value();

}
