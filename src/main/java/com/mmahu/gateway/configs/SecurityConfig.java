package com.mmahu.gateway.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails guest = User.withUsername("guest").password(encoder.encode("guest")).roles("GUEST").build();
        UserDetails buyer = User.withUsername("buyer").password(encoder.encode("buyer")).roles("BUYER").build();
        UserDetails seller = User.withUsername("seller").password(encoder.encode("seller")).roles("SELLER").build();
        return new MapReactiveUserDetailsService(guest, buyer, seller);
    }

}
