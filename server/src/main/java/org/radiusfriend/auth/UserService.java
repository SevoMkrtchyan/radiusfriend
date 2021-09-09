package org.radiusfriend.auth;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.radiusfriend.controller.request.AuthRequest;
import org.radiusfriend.dao.UserDaoService;
import org.radiusfriend.dto.User;
import org.radiusfriend.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.radiusfriend.consts.WebConsts.AUTH_COOKIE_NAME;

@Service
@Slf4j
public class UserService {
    private final UserDaoService userDaoService;
    private final Map<String, String> tokenStorage = new HashMap<>();
    private final int tokenLifeTime;

    @Autowired
    public UserService(UserDaoService userDaoService,
                       @Value("${auth.tokenLifeTime:1800}") int tokenLifeTime) {
        this.userDaoService = userDaoService;
        this.tokenLifeTime = tokenLifeTime;
    }

    public User loadUserByUsername(String username) {
        log.debug("Request to find user by username = {}", username);
        User user = userDaoService.findByUsername(username);
        log.debug("User found = {}", user);
        return user;
    }

    public String login(AuthRequest request, HttpServletResponse response) {
        val user = loadUserByUsername(request.getUsername());
        if (user.getPassword().equals(request.getPassword())) {
            val token = UUID.randomUUID().toString();
            tokenStorage.put(token, request.getUsername());
            val cookie = new Cookie(AUTH_COOKIE_NAME, token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setMaxAge(tokenLifeTime);
            response.addCookie(cookie);
            return token;
        }
        throw new AuthException(request.getUsername());
    }

    public User getUser(String token) {
        val userName = Optional.ofNullable(tokenStorage.get(token))
                .orElseThrow(AuthException::new);
        return userDaoService.findByUsername(userName);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        val authCookie = getAutCookie(request.getCookies());
        val token = authCookie.getValue();
        tokenStorage.remove(token);
        authCookie.setMaxAge(0);
        response.addCookie(authCookie);
    }

    private static Cookie getAutCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                return cookie;
            }
        }
        throw new AuthException();
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        val cookie = getAutCookie(request.getCookies());
        cookie.setMaxAge(tokenLifeTime);
        response.addCookie(cookie);
    }
}