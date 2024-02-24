package com.example.filmview.Security.Config;


import com.example.filmview.Application.ApplicationException;
import com.example.filmview.Security.Filters.CorsFilter;
import com.example.filmview.Security.JWT.JWTAuthenticationFilter;
import com.example.filmview.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collection;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final JWTAuthenticationFilter authenticationFilter;

    private final UserService user_service;
    private final PasswordEncoder passwordEncoder;
    private final CorsFilter corsFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/filmstar/create","/api/film/create","/api/filmstar/*/add/picture")
                                .hasAnyAuthority("ADMIN","HEAD_ADMIN")
                                .requestMatchers("/api/ratings/add", "/api/user/rated/*").authenticated()
//                                .requestMatchers("/api/auth/**","/api/film/*/display","/api/film/top/*",
//                                        "/api/filmstar/all","/api/filmstar/*/display"
//                                        ,"/api/film/all").permitAll()
                                .anyRequest().permitAll()
                )

                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(getAuthenticationProvider())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(corsFilter-> corsFilter.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()));
        http.addFilterBefore(corsFilter, WebAsyncManagerIntegrationFilter.class);


        return http.build();
    }

    @Bean
    public AuthenticationProvider getAuthenticationProvider(){
        DaoAuthenticationProvider auth_provider = new DaoAuthenticationProvider();
        auth_provider.setUserDetailsService(user_service);
        auth_provider.setPasswordEncoder(passwordEncoder);
        return auth_provider;

    }
    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration config) throws Exception{
        //return config.getAuthenticationManager();
        return new AuthenticationManager() {


            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();
                UserDetails user = user_service.loadUserByUsername(username);
                if(user==null){
                    throw new ApplicationException("Invalid login",403);
                }

                if(!user.isEnabled()){
                    throw new ApplicationException("Account is not activated",403);
                }

                if(!passwordEncoder.matches(password, user.getPassword())){
                    throw new ApplicationException("Invalid password",403);
                }

                Collection<GrantedAuthority> authorities = user.getAuthorities().stream()
                        .map(auth->new SimpleGrantedAuthority(auth.getAuthority()))
                        .collect(Collectors.toList());
                return new UsernamePasswordAuthenticationToken(username,null,authorities);
            }
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000");
            }
        };
    }


}
