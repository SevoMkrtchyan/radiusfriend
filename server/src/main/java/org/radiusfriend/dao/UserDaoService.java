package org.radiusfriend.dao;

import org.radiusfriend.dto.User;

public interface UserDaoService {
    User findByUsername(String username);
}
