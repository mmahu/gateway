package com.mmahu.gateway.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Principal;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHENTICATION;
import static org.springframework.security.config.web.server.SecurityWebFiltersOrder.AUTHORIZATION;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${spring.security.oauth2.resource.jwt.key-value}")
    String key;

    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> exchange.getPrincipal()
                .map(Principal::getName)
                .map(userName -> {
                    exchange.getRequest().mutate().header("Authorization", toToken(userName)).build();
                    return exchange;
                }).flatMap(chain::filter);
    }

    private String toToken(String userId) {
        String token = JwtHelper.encode("{\"sub\": \"" + userId +  "\"}", new MacSigner(key)).getEncoded();
        return "Bearer " + token;
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

//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http.authorizeExchange(exchanges -> exchanges.anyExchange().authenticated())
//                .httpBasic(withDefaults())
//                .formLogin(withDefaults());
//        return http
//                .addFilterAfter(new WebFilter(){
//                    @Override
//                    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//                        exchange.getPrincipal()
//                                .map(Principal::getName)
//                                .map(userName -> {
//                                    exchange.getRequest().mutate().header("Authorization", toToken(userName)).build();
//                                    return exchange;
//                                }).flatMap(chain::filter);
//                        return chain.filter(exchange);
//                    }
//                }, AUTHORIZATION)
//                .build();
//    }

}
