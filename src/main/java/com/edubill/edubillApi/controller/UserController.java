package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.user.response.UserProfileDto;
import com.edubill.edubillApi.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my-profile")
    public ResponseEntity<UserProfileDto> getUserProfile(final Principal principal) {
        final String userId = principal.getName();
        final User user = userService.findUser(userId);
        final UserProfileDto userProfileDto = UserProfileDto.of(user);

        return ResponseEntity.ok(userProfileDto);
    }


}
