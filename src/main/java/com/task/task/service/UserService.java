package com.task.task.service;

import com.task.task.service.dto.JWTTokenDTO;
import com.task.task.service.dto.UserDto;
import com.task.task.service.vm.LoginVM;
import com.task.task.service.vm.RegisterUserVM;
import com.task.task.service.vm.UpdatePasswordOrLoginVM;

import javassist.NotFoundException;

public interface UserService {

    JWTTokenDTO registerUser(RegisterUserVM userDto);

    UserDto updateUser(UserDto userDto, Long authUserId);

    UserDto getUserDto(Long authUserId);

    JWTTokenDTO updatePasswordOrLogin(UpdatePasswordOrLoginVM updatePasswordOrLoginVM, Long authUserId, String jwt);

    JWTTokenDTO authorize(LoginVM loginVM);

    void deleteUser(Long userId, String jwt);

    void logout(String jwt, Long userId);

}
