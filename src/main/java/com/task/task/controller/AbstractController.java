package com.task.task.controller;

import javax.servlet.http.HttpServletRequest;

import com.task.task.Roles;
import com.task.task.errors.PermissionException;
import com.task.task.security.UserPrinciple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class AbstractController {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    protected Long getAuthUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    authentication.getAuthorities().forEach(o -> {
       if (o.toString().equals(Roles.ROLE_ANONYMOUS.getValue())) {
           throw new PermissionException("");
       }
    });

        UserPrinciple jwtUser = (UserPrinciple) authentication.getPrincipal();
        return jwtUser.getId();
    }

    protected String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
