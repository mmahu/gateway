package com.mmahu.gateway.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mmahu.gateway.dto.JwtDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${spring.security.oauth2.resource.jwt.key-value}")
    private String key;

    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> exchange.getPrincipal()
                .filter(UsernamePasswordAuthenticationToken.class::isInstance)
                .map(UsernamePasswordAuthenticationToken.class::cast)
                .map(principal -> {
                    String token = convert(principal);
                    exchange.getRequest().mutate().header("Authorization", token).build();
                    return exchange;
                }).flatMap(chain::filter);
    }

    private String convert(UsernamePasswordAuthenticationToken principal) {
        String authorities = toAuthorities(principal.getAuthorities());
        JwtDto dto = new JwtDto()
                .setSub(principal.getName())
                .setScp(authorities)
                .setIat(Instant.now())
                .setExp(Instant.now().plusSeconds(60 * 30));
        String content = toJson(dto);
        String token = JwtHelper.encode(content, new MacSigner(key)).getEncoded();
        return "Bearer " + token;
    }

    private String toAuthorities(Collection<GrantedAuthority> authorities) {
        if (nonNull(authorities)) {
            return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        }
        return null;
    }

    private String toJson(JwtDto dto) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot convert JwtDto to Json");
        }
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("seller")
                .password("seller")
                .roles("SELLER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
                .httpBasic(withDefaults())
                .formLogin(withDefaults());
        return http.build();
    }

}
