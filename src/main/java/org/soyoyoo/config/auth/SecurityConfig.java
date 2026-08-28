package org.soyoyoo.config.auth;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.soyoyoo.domain.user.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security를 활성화
@Configuration
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    // spring security 적용 하지 않을 URL 리스트
    private static final String[] AUTH_WHITELIST = {
            "/",
            "/css/**",
            "/image/**",
            "/js/**",
            "/h2-console/**"
    };
    // 인증 필요 리스트
    private static final String[] VERIFICATION_AUTH_LIST = {
            "/api/v1/**"
    };

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        // antMatcher(...)로 감싸서 명시적 지정!
                        .requestMatchers(antMatcher("/"), antMatcher("/css/**"), antMatcher("/images/**"), antMatcher("/js/**"), antMatcher("/h2-console/**"), antMatcher("/profile")).permitAll()
                        .requestMatchers(antMatcher("/api/v1/**")).hasRole(Role.USER.name())
                        .anyRequest().authenticated()
                )

                // 4. 로그아웃 성공 시 이동 경로
                .logout(logout -> logout.logoutSuccessUrl("/"))

                // 5. OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                );

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // /profile 및 정적 리소스는 Security 필터 체인 자체를 거치지 않고 바로 통과!
        return (web) -> web.ignoring().requestMatchers(
                AntPathRequestMatcher.antMatcher("/profile"),
                AntPathRequestMatcher.antMatcher("/css/**"),
                AntPathRequestMatcher.antMatcher("/js/**"),
                AntPathRequestMatcher.antMatcher("/images/**"),
                AntPathRequestMatcher.antMatcher("/h2-console/**")
        );
    }

}
