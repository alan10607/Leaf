package com.alan10607.leaf.config;

import com.alan10607.leaf.model.LeafUser;
import com.alan10607.leaf.util.SecurityUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String email = request.getParameter("username");
        String pw = request.getParameter("password");

//        email = request.getHeader("username");
//        pw = request.getHeader("password");
        log.info("authenticationToken username as email={}", email);//ndeeeeddddd fi xxxxxxxxxxxx
        log.info("authenticationToken username as password={}", pw);

        //to UserDetailService
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, pw));
//        response.setHeader(HttpHeaders.AUTHORIZATION, email);

        LeafUser leafUser = (LeafUser) authentication.getPrincipal();//獲得user資訊, 已經從UserServiceImpl.loadUserByUsername()獲得
        Algorithm algorithm = SecurityUtil.JWT_ALGORITHM;//加密算法

        String accessToken = JWT.create()
                .withSubject(leafUser.getUsername())//紀錄username作為識別
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))//10 mins
                .withIssuer(request.getRequestURL().toString())
                .withClaim(SecurityUtil.ROLES, leafUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        Map<String,String> tokens = Map.of(SecurityUtil.ACCESS_TOKEN_KEY, accessToken);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);//convert tokens to response.getOutputStream()
//        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);


    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        //只有login post時要進filter
        String method = request.getMethod();
        String uri = request.getRequestURI();
        boolean isLogin = HttpMethod.POST.matches(method) && uri.startsWith("/login");
        log.info("LoginFilter:" + request.getRequestURI() + " is " + !isLogin);
        return !isLogin;
    }
}
