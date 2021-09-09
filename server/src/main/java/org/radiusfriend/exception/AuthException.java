package org.radiusfriend.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AuthException extends RuntimeException {
    private String username;
}