package com.alan10607.leaf.util;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
//   private final Key key;
//
//   private JwtUtil(){
//       key = Keys.
//   }
//
//   public String createToken(LeafUser leafUser, Map<String, Object> claim){
//               LeafUser leafUser = (LeafUser) authentication.getPrincipal();//獲得user資訊, 已經從UserServiceImpl.loadUserByUsername()獲得
//        Algorithm algorithm = SecurityUtil.JWT_ALGORITHM;//加密算法
//
//        String accessToken = JWT.create()
//                .withSubject(leafUser.getUsername())//紀錄username作為識別
//                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))//10 mins
//                .withIssuer(request.getRequestURL().toString())
//                .withClaim(SecurityUtil.ROLES, leafUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
//                .sign(algorithm);
//
//        Map<String,String> tokens = Map.of(SecurityUtil.ACCESS_TOKEN_KEY, accessToken);
//        response.setContentType(APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), tokens);//convert tokens to response.getOutputStream()
//
//   }
}