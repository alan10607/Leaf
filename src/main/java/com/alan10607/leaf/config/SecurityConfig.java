package com.alan10607.leaf.config;

import com.alan10607.leaf.constant.LeafRoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 用於用戶密碼的加密
     * @return
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 設定驗證方式
     * @param userDetailsService
     * @param passwordEncoder
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder){
        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(passwordEncoder);//給予應對之加密方式, 否則會There is no PasswordEncoder mapped for the id "null"
        return new ProviderManager(dao);
    }

    /**
     * Web security, 代替WebSecurityConfigurerAdapter.configure(HttpSecurity http)
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();//跨域請求偽造, 測試時禁用
        //http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//不使用session
        http.formLogin().loginPage("/login").loginProcessingUrl("/loginProcessing").defaultSuccessUrl("/admin").failureForwardUrl("/login?error");
        http.logout().logoutUrl("/logoutProcessing").logoutSuccessUrl("/login?logout");
        http.authorizeRequests().antMatchers("/index/**", "/css/**", "/js/**", "/pic/**", "/view/**", "/login").permitAll();//公開葉面
        http.authorizeRequests().anyRequest().hasAuthority(LeafRoleType.ADMIN.name());//限制為admin權限訪問
        http.exceptionHandling().accessDeniedPage("/err");
        return http.build();
    }

    /**
     * 設定自訂錯誤頁面
     * @return
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        String errorPage = "/err";//所有錯誤頁面都導到這裡
        return factory -> {
            factory.addErrorPages(
                    new ErrorPage(HttpStatus.NOT_FOUND, errorPage),//404
                    new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, errorPage));//500
        };
    }

}