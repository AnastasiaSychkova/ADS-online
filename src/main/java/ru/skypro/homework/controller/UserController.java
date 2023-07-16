package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.service.impl.UserService;

import java.io.IOException;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/setPassword")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword) {
        if (userService.setPassword(SecurityContextHolder.getContext().getAuthentication().getName(), newPassword)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser() {
        UserDto userDto = userService.getUserDto(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(userDto);
    }

    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        UpdateUser updateUser1 = userService.updateUser(updateUser, SecurityContextHolder.getContext().getAuthentication().getName());
        if (updateUser1 == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(updateUser1);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateUserImage(@RequestBody MultipartFile image) throws IOException {
        return ResponseEntity.ok().build();
    }
}
