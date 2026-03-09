// package githappens.hh.project_management_app;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// public class WebSecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             // kaikki pyynnöt sallitaan
//             .authorizeHttpRequests(authorize -> authorize
//                 .anyRequest().permitAll()
//             )
//             // CSRF pois H2 consolelle
//             .csrf(csrf -> csrf
//                 .ignoringRequestMatchers("/h2-console/**")
//             )
//             // iframe H2:lle
//             .headers(headers -> headers
//                 .frameOptions(frame -> frame.disable())
//             );

//         return http.build();
//     }
// }
