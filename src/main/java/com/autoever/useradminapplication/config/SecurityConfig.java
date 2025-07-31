package com.autoever.useradminapplication.config;

import com.autoever.useradminapplication.constants.enums.RoleType;
import com.autoever.useradminapplication.service.admin.login.AdminLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private final AdminLoginService adminLoginService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     *  JWT 인증을 처리하는 OncePerRequestFilter를 만들고 Security Filter Chain에 등록함.
     *
     *  이 필터 프록시가 실행되어 JWT를 파싱하고, 토큰이 유효한지 확인함.
     * @param http
     * @return
     * @throws Exception
     * @see    : https://docs.spring.io/spring-security/reference/servlet/architecture.html
     */
    @Bean
    @Order(1)
    public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/admin/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 엔드포인트 별 인증/인가 정책 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole(RoleType.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()) // admin 용 basic auth 활성화
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(((request, response, authException) -> {
                            // 인증예외 (401 UnAuthorized Error)
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        }))
                        .accessDeniedHandler((request, response, authException) -> {
                            // 인가예외 (403 Forbidden Error)
                            log.info("====> [관리자] {}", authException.getMessage());
                            log.error(authException.getCause().getMessage(), authException.getCause());
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                        })
                )
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 엔드포인트 별 인증/인가 정책 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/api/sign-up").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .requestMatchers("/api/**").hasRole(RoleType.USER.name())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(((request, response, authException) -> {
                            // 인증예외 (401 UnAuthorized Error)
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        }))
                        .accessDeniedHandler((request, response, authException) -> {
                            // 인가예외 (403 Forbidden Error)
                            log.info("====> [사용자] {}", authException.getMessage());
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                        })
                )
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(adminLoginService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
