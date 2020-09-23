package com.task.task.controller;

import com.task.task.service.UserService;
import com.task.task.service.dto.JWTTokenDTO;
import com.task.task.service.vm.LoginVM;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController extends AbstractController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDTO> logIn(@RequestBody LoginVM loginVM) {
        return ResponseEntity.ok(userService.authorize(loginVM));
    }

    @DeleteMapping("/logout")
    public void logOut(HttpServletRequest request) {
        userService.logout(resolveToken(request), getAuthUserId());
    }

}
