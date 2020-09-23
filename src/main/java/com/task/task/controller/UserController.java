package com.task.task.controller;

import com.task.task.service.UserService;
import com.task.task.service.dto.JWTTokenDTO;
import com.task.task.service.dto.UserDto;
import com.task.task.service.vm.RegisterUserVM;
import com.task.task.service.vm.UpdatePasswordOrLoginVM;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController extends AbstractController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<JWTTokenDTO> registerUser(@RequestBody RegisterUserVM registerUserVM) {
        return ResponseEntity.ok().body(userService.registerUser(registerUserVM));
    }

    @PutMapping()
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok().body(userService.updateUser(userDto, getAuthUserId()));
    }

    @PutMapping("/change-password")
    public ResponseEntity<JWTTokenDTO> updatePasswordOrLogin(@RequestBody UpdatePasswordOrLoginVM updatePasswordOrLoginVM,
                                                             HttpServletRequest request) {
        return ResponseEntity.ok().body(userService.updatePasswordOrLogin(updatePasswordOrLoginVM, getAuthUserId(),resolveToken(request)));
    }

    @DeleteMapping()
    public void deleteUser(HttpServletRequest request) {
        userService.deleteUser(getAuthUserId(), resolveToken(request));
    }

    @GetMapping()
    public ResponseEntity<UserDto> getCurrentUser(){
        return ResponseEntity.ok().body(userService.getUserDto(getAuthUserId()));
    }
}
