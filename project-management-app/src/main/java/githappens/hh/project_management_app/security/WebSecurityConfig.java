package githappens.hh.project_management_app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = new AuthEntryPointJwt();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .exceptionHandling(exceptionHandling ->
                    exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(sessionManagement -> 
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests -> 
                    authorizeRequests
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .requestMatchers("/login", "/register", "/h2-console/**", "/error").permitAll()
                            .requestMatchers("/actuator/health/**", "/actuator/info").permitAll()
                            .requestMatchers("/ws/**").permitAll()
                            .requestMatchers("/topic/**", "/app/**").permitAll()
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().authenticated()
                );
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://localhost:5173", "https://yellow-mud-05a9abf03.1.azurestaticapps.net")); // Set your allowed origins
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

//     public SecurityFilterChain configure(HttpSecurity http) throws Exception {
//         http
//                 .authorizeHttpRequests(authorize -> authorize
//                         .requestMatchers("/h2-console/**", "/api/**").permitAll()
//                         .anyRequest().authenticated())
//                 .csrf(csrf -> csrf.disable())
//                 .headers(headers -> headers
//                         .frameOptions(frame -> frame.disable()))
//                 .formLogin(formlogin -> formlogin.disable())
//                         //.defaultSuccessUrl("/", true)
//                         //.permitAll())
//                 .logout(logout -> logout
//                         .permitAll());
//         return http.build();
//     }

//     public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//         auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
//     }

