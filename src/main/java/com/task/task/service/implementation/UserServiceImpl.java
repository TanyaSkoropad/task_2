package com.task.task.service.implementation;

import com.task.task.entities.BlackListsJWT;
import com.task.task.entities.User;
import com.task.task.errors.NotFoundException;
import com.task.task.errors.PermissionException;
import com.task.task.repository.BlackListsJWTRepository;
import com.task.task.repository.UserRepository;
import com.task.task.security.TokenProvider;
import com.task.task.service.UserService;
import com.task.task.service.dto.JWTTokenDTO;
import com.task.task.service.dto.UserDto;
import com.task.task.service.vm.LoginVM;
import com.task.task.service.vm.RegisterUserVM;
import com.task.task.service.vm.UpdatePasswordOrLoginVM;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String PERMISSION_DENIED = "Permission denied";

    public static final String USER_IS_NOT_FOUND = "User is not found";

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlackListsJWTRepository blackListsJWTRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    @Transactional
    public JWTTokenDTO registerUser(RegisterUserVM userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new PermissionException(PERMISSION_DENIED);
        }
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = userRepository.save(user);
        return getJwtTokenDTO(user, user.getEmail(), userDto.getPassword());
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, Long authUserId) {
        User user = userRepository.findById(authUserId).orElseThrow(() -> new NotFoundException(USER_IS_NOT_FOUND));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user = userRepository.save(user);
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    @Override
    public UserDto getUserDto(Long authUserId) {
        User user = userRepository.findById(authUserId).orElseThrow(() -> new NotFoundException(USER_IS_NOT_FOUND));
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    @Override
    @Transactional
    public JWTTokenDTO updatePasswordOrLogin(UpdatePasswordOrLoginVM updatePasswordOrLoginVM, Long authUserId, String jwt) throws NotFoundException {
        User user = userRepository.findById(authUserId).orElseThrow(() -> new NotFoundException(USER_IS_NOT_FOUND));

        if (Objects.isNull(updatePasswordOrLoginVM.getNewPassword())) {
            updatePasswordOrLoginVM.setNewPassword(updatePasswordOrLoginVM.getCurrentPassword());
        }
        String encodedCurrentPassword = passwordEncoder.encode(updatePasswordOrLoginVM.getCurrentPassword());
        if (!passwordEncoder.matches(updatePasswordOrLoginVM.getCurrentPassword(),encodedCurrentPassword)) {
            throw new PermissionException(PERMISSION_DENIED);
        }

        if (Objects.nonNull(updatePasswordOrLoginVM.getNewEmail()) && !user.getEmail().equals(updatePasswordOrLoginVM.getNewEmail())) {
            if (userRepository.existsByEmail(updatePasswordOrLoginVM.getNewEmail())) {
                throw new PermissionException(PERMISSION_DENIED);
            } else {
                user.setEmail(updatePasswordOrLoginVM.getNewEmail());
            }
        }

        user.setPassword(passwordEncoder.encode(updatePasswordOrLoginVM.getNewPassword()));
        user = userRepository.save(user);
        logout(jwt, authUserId);
        return getJwtTokenDTO(user, user.getEmail(), updatePasswordOrLoginVM.getNewPassword());
    }

    @Override
    public JWTTokenDTO authorize(LoginVM loginVM) {
        User user = userRepository.findByEmail(loginVM.getLogin()).orElseThrow(() -> new NotFoundException(USER_IS_NOT_FOUND));
        if (!passwordEncoder.matches(loginVM.getPassword(),user.getPassword())) {
            throw new NotFoundException("");
        }
        return getJwtTokenDTO(user, loginVM.getLogin(), loginVM.getPassword());
    }

    @Override
    public void deleteUser(Long userId, String jwt) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_IS_NOT_FOUND));
        user.setRemoved(true);
        user = userRepository.save(user);
        logout(jwt, userId);
    }

    @Override
    public void logout(String jwt, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(USER_IS_NOT_FOUND);
        }
        blackListsJWTRepository.save(new BlackListsJWT(jwt));
    }

    private JWTTokenDTO getJwtTokenDTO(User user, String email, String newPassword) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, newPassword);
        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String jwt = tokenProvider.generateToken(authentication);
        UserDto dto = new JWTTokenDTO();
        BeanUtils.copyProperties(user, dto);
        return new JWTTokenDTO(jwt, dto);
    }

}
