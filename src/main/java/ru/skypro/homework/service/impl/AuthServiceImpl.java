package ru.skypro.homework.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.service.AuthService;

/** Сервис для работы с авторизацией пользователей */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserService userService;

    public AuthServiceImpl(UserDetailsManager manager,
                           PasswordEncoder passwordEncoder, UserService userService) {
        this.manager = manager;
        this.encoder = passwordEncoder;
        this.userService = userService;
    }


    /** Метод для входа в приложение */
    @Override
    public boolean login(String userName, String password) {
        if (!manager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }


    /** Метод для регистрации */
    @Override
    public boolean register(Register register, Role role) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUsername())
                        .roles(role.name())
                        .build());

        userService.saveUser(register, register.getRole(), this.encoder.encode(register.getPassword()));
        return true;
    }

    /** Метод для изменения пароля */
    public boolean setPassword(String login, NewPassword newPassword) {
        if (!encoder.matches(newPassword.getCurrentPassword(), manager.loadUserByUsername(login).getPassword())) {
            return false;
        }

        ru.skypro.homework.model.User user = userService.setPasswordInDb(login, this.encoder.encode(newPassword.getNewPassword()));
        if (user == null) {
            return false;
        }

        manager.updateUser(
                org.springframework.security.core.userdetails.User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(newPassword.getNewPassword())
                        .username(login)
                        .roles(user.getRole().name())
                        .build());
        return true;
    }

}
