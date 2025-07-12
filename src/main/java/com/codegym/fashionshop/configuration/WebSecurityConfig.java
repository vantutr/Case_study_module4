package com.codegym.fashionshop.configuration;

import com.codegym.fashionshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(IUserService userService, BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // trong file: src/main/java/com/codegym/fashionshop/config/WebSecurityConfig.java

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> auth
                        // PHẦN 1: CÁC TRANG CÔNG KHAI
                        .antMatchers(
                                "/", "/home", "/shop/**", "/products/**", // Các trang người dùng có thể xem
                                "/login", "/logout", "/register",                     // Trang đăng nhập/đăng xuất
                                "/static/**", "/uploads/**"              // Các tài nguyên tĩnh (css, js, ảnh)
                        ).permitAll()

                        // PHẦN 2: CÁC TRANG CỦA ADMIN
                        .antMatchers("/admin/**").hasRole("ADMIN")

                        // PHẦN 3: CÁC TRANG CẦN ĐĂNG NHẬP (Bất kỳ vai trò nào)
                        .antMatchers("/cart/**", "/checkout/**", "/my-orders/**").authenticated()

                        // Bất kỳ request nào khác cũng cần đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        // Luôn chuyển về trang chủ sau khi đăng nhập thành công
                        .defaultSuccessUrl("/home", true)
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/home")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(e -> e
                        .accessDeniedPage("/403")
                );

        return http.build();
    }
}