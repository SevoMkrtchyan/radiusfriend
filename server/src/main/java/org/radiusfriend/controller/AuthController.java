package org.radiusfriend.controller;

import lombok.RequiredArgsConstructor;
import org.radiusfriend.auth.UserService;
import org.radiusfriend.controller.request.AuthRequest;
import org.radiusfriend.controller.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private final UserService userService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        userService.login(request, response);
        return ResponseEntity.ok(new BaseResponse());
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logout(request, response);
        return ResponseEntity.ok(new BaseResponse());
    }
}