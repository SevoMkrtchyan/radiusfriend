package org.radiusfriend.dto;

import lombok.*;

import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class User {
    private String username;
    @ToString.Exclude
    private String password;
    private Role role;

    public Collection<Role> getAuthorities() {
        return Collections.singletonList(getRole());
    }

}
