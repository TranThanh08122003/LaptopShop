package com.TCL.example.config;


import com.TCL.example.service.CustomUserDetailsService;
import com.TCL.example.service.UserService;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }
    @Bean
    public DaoAuthenticationProvider authProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
//        authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }


    @Bean
    public AuthenticationSuccessHandler customSuccessHandler(){
        return new CustomSuccessHandler();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }


   @Bean
SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
.csrf(csrf -> csrf
    .ignoringRequestMatchers("/forgot-password/**", "/api/**")
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
)
        .authorizeHttpRequests(authorize -> authorize
            .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE).permitAll()
            .requestMatchers(HttpMethod.GET, "/", "/login", "/client/**", "/css/**", "/js/**", "/product/**",
                    "/images/**", "/register", "/api/**", "/products/**", "/forgot-password/**", "/reports/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/register", "/forgot-password/**",
                    "/api/auth/login", "/api/add-product-to-cart", "/api/products/**","/admin/coupon/**").permitAll()
            .requestMatchers("/admin/**").hasRole("MANAGER")
            .requestMatchers("/admin").hasAnyRole("ADMIN","MANAGER","SELLER")
            .requestMatchers("/admin/product/**").hasRole("MANAGER")
            .requestMatchers("/admin/order/**").hasAnyRole("SELLER", "MANAGER")
            .requestMatchers("/admin/user/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .invalidSessionUrl("/logout?expired")
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
        )
        .logout(logout -> logout
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            .logoutUrl("/logout") 
            .logoutSuccessUrl("/login?logout")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true)
        )
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .failureUrl("/login?error")
            .successHandler(customSuccessHandler())
            .permitAll()
        )
        .exceptionHandling(ex -> ex
            .accessDeniedPage("/access-denied")
        );

    return http.build();
}


}
