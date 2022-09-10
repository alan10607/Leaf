package com.alan10607.leaf.util;

import com.auth0.jwt.algorithms.Algorithm;

import java.nio.charset.StandardCharsets;

public class SecurityUtil {
    public final static String[] OPEN_URL = {"/view/**", "/index", "/admin"};
    public final static String[] ADMIN_URL = {"/admin/**"};
    public final static String JWT_SALT = "Bearer ";
    public final static Algorithm JWT_ALGORITHM = Algorithm.HMAC256("leafsecret".getBytes(StandardCharsets.UTF_8));
    public final static String ROLES = "roles";
    public final static String ACCESS_TOKEN_KEY = "access_token";
    public final static String ERROR_KEY = "error";

}