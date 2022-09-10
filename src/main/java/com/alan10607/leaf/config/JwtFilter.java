package com.alan10607.leaf.config;

import com.alan10607.leaf.util.SecurityUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);//獲得header中的Authorization
        request.getParameter("bearer");
        if(authHeader == null || !authHeader.startsWith(SecurityUtil.JWT_SALT)){
            filterChain.doFilter(request, response);
            return;
        }
//            return;//無可檢核之JWT

        //開始檢核JWT
        try {
            //1 decoded JWT
            String token = authHeader.substring(SecurityUtil.JWT_SALT.length());//移除salt
            Algorithm algorithm = SecurityUtil.JWT_ALGORITHM;
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            //2 collect username and roles to set UsernamePasswordAuthenticationToken
            String userName = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim(SecurityUtil.ROLES).asArray(String.class);//獲取roles
            Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles)
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);//放入該user資訊供後續檢核

            filterChain.doFilter(request, response);
        }catch (Exception e){
            if(!(e instanceof TokenExpiredException))
                log.error("JWT authorization error: {}", e);

            response.setHeader(SecurityUtil.ERROR_KEY, e.getMessage());
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            Map<String,String> error = Map.of(SecurityUtil.ACCESS_TOKEN_KEY, e.getMessage());
            new ObjectMapper().writeValue(response.getOutputStream(), error);

            //這裡應該不能回去filterChain.doFilter的樣子，不過先擺這樣試試看搞不好可以
            //如果不行就回傳boolean拆開就好
        }
    }

    private String getToken(HttpServletRequest request){
return "";
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //只有admin開頭要進filter
        String uri = request.getRequestURI();
        boolean idAdmin = uri.startsWith("/admin");
        log.info("JwtFilter:" + request.getRequestURI() + " is " + !idAdmin);
        return !idAdmin;
    }



}
