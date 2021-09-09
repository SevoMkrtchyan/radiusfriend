package org.radiusfriend.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.radiusfriend.dto.Role;
import org.radiusfriend.dto.User;
import org.radiusfriend.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserDaoServiceImpl implements UserDaoService {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User findByUsername(String username) {
        try {
            return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", new Object[]{username},
                    rs -> {
                        if (!rs.next()) {
                            throw new AuthException();
                        }
                        return User.builder()
                                .username(rs.getString("username"))
                                .password(rs.getString("password"))
                                .role(Role.getValueByName(rs.getString("role")))
                                .build();
                    });
        } catch (Exception t) {
            log.error("Database error", t);
            throw new AuthException();
        }
    }
}