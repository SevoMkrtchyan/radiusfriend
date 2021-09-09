package org.radiusfriend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("Admin");
    private final String name;

    public static Role getValueByName(String roleName) {
        for (Role role : Role.values()) {
            if (role.getName().equals(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException();
    }

    public String getAuthority() {
        return getName();
    }
}
