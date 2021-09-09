package org.radiusfriend.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.radiusfriend.dto.Role;
import org.radiusfriend.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.radiusfriend.consts.WebConsts.AUTH_COOKIE_NAME;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TokenAuthorizationFilter extends HttpFilter {
    private final UserService userService;
    private final Map<String, List<Role>> permissions;

    private Cookie getAuthCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTH_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        throw new AuthException();
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response,
                            FilterChain chain) throws IOException, ServletException {
        try {
            for (String rootUrl : permissions.keySet()) {
                if (request.getServletPath().startsWith(rootUrl)) {
                    val authCookie = getAuthCookie(request.getCookies());

                    String token = authCookie.getValue();

                    if (token != null) {
                        val user = userService.getUser(token);
                        if (permissions.get(rootUrl).contains(user.getRole())) {
                            chain.doFilter(request, response);
                            userService.refreshToken(request, response);
                            return;
                        } else {
                            throw new AuthException();
                        }
                    } else {
                        throw new AuthException();
                    }
                }
            }
            chain.doFilter(request, response);
        } catch (AuthException e) {
            log.error("Error while authorize request url " + request.getServletPath(), e);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }
}