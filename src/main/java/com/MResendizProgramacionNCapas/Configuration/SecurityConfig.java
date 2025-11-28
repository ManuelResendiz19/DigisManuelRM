
package com.MResendizProgramacionNCapas.Configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import com.MResendizProgramacionNCapas.Service.UsuarioDetailsJPAService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
        

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioDetailsJPAService usuarioDetailsJPAService;
    
    public SecurityConfig(UsuarioDetailsJPAService usuarioDetailsJPAService1){
        this.usuarioDetailsJPAService = usuarioDetailsJPAService1; 
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers("/usuario/**")
//                .anyRequest()
                .authenticated())
                .formLogin(form -> form
                        .defaultSuccessUrl("/usuario", true)
                 ).userDetailsService(usuarioDetailsJPAService);
        
        return http.build();
}
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
