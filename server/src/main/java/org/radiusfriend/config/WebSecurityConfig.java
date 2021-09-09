package org.radiusfriend.config;

import lombok.RequiredArgsConstructor;
import org.radiusfriend.auth.TokenAuthorizationFilter;
import org.radiusfriend.auth.UserService;
import org.radiusfriend.dto.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.radiusfriend.consts.WebConsts.ADMIN;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig {
    private final UserService userService;

    @Bean
    public TokenAuthorizationFilter tokenAuthorizationFilter() {
        Map<String, List<Role>> permissions = new HashMap<>();
        permissions.put(ADMIN, Collections.singletonList(Role.ADMIN));
        return new TokenAuthorizationFilter(userService, permissions);
    }
}