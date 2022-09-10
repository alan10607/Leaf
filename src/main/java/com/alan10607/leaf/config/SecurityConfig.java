package com.alan10607.leaf.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    @Lazy
    private LoginFilter loginFilter;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder);//There is no PasswordEncoder mapped for the id "null"
        return new ProviderManager(dao);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //代替WebSecurityConfigurerAdapter.configure(HttpSecurity http)
        //authenticationJWTFilter.setFilterProcessesUrl("/admin");
        http.csrf().disable();//跨域請求偽造, 測試時禁用
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//不使用ession
//        http.authorizeRequests().anyRequest().permitAll();
//        http.formLogin().loginPage("/login").defaultSuccessUrl("/admin/manager").failureForwardUrl("/index");
        //     http.formLogin().loginPage("/login").defaultSuccessUrl("/admin/manager").failureForwardUrl("/index");
//        http.logout().logoutUrl("/logout").logoutSuccessUrl("/index");

        http.authorizeRequests().anyRequest().permitAll();
//        http.authorizeRequests().antMatchers("/index", "/css/**", "/js/**", "/pic/**", "/view/**", "/login").permitAll();//公開葉面
//        http.authorizeRequests().antMatchers("/admin/**").hasAuthority(LeafRoleType.ADMIN.name());//限制為admin權限訪問
//        http.authorizeRequests().anyRequest().authenticated();


//        http.addFilter(jwtFilter);//驗證實例
        http.addFilterAt(loginFilter, BasicAuthenticationFilter.class);
        http.addFilterBefore(jwtFilter, BasicAuthenticationFilter.class);
//        http.httpBasic();
        return http.build();
    }

}
//
//
//
//
//public class SecurityConfig {
//    private final AuthenticationJWTFilter authenticationJWTFilter;
//    private final AuthorizationJWTFilter authorizationJWTFilter;
//    public final UserDetailsService userDetailsService;
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        //代替WebSecurityConfigurerAdapter.configure(HttpSecurity http)
//        authenticationJWTFilter.setFilterProcessesUrl("/admin");
//        http.csrf().disable();//跨域請求偽造, 測試時禁用
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//不使用ession
//        http.formLogin().loginPage("/admin").defaultSuccessUrl("/admin/manager").failureForwardUrl("/admin");
//        http.logout().logoutUrl("/logout").logoutSuccessUrl("/index");
//        http.authorizeRequests().antMatchers(HttpMethod.GET, SecurityUtil.OPEN_URL).permitAll();//公開葉面
//        http.authorizeRequests().antMatchers(HttpMethod.POST, SecurityUtil.ADMIN_URL).hasAuthority("admin");//限制為admin權限訪問
//        http.authorizeRequests().anyRequest().authenticated();
//        http.addFilter(authenticationJWTFilter);//驗證實例
//        http.addFilterBefore(authorizationJWTFilter, UsernamePasswordAuthenticationFilter.class);//設定授權在驗證之前
//        http.httpBasic();
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        //代替WebSecurityConfigurerAdapter.configure(AuthenticationManagerBuilder auth)
//        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
//        dao.setUserDetailsService(userDetailsService);
//        return new ProviderManager(dao);
//    }
//
////    @Override
////    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
////    }
//
//
////    https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
////    新功能，會忽略以下
////    @Bean
////    public WebSecurityCustomizer webSecurityCustomizer() {
////        return (web) -> web.ignoring().antMatchers("/ignore1", "/ignore2");
////    }
//
//
//
////    @Bean
////    public AuthenticationManager authenticationManagerBean() throws Exception {
////        return super.authenticationManagerBean();
////    }
//
//}
