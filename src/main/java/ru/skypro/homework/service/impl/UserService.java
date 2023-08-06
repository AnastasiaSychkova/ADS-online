package ru.skypro.homework.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;


@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserDetailsManager manager, PasswordEncoder encoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.manager = manager;
        this.encoder = encoder;
        this.userMapper = userMapper;
    }

    private User setPasswordInDb(String login, String newPassword) {
        User user = userRepository.findUserByEmailIgnoreCase(login);
        if (user == null) {
            return null;
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return user;
    }


    public boolean setPassword(String login, NewPassword newPassword) {
        if (!encoder.matches(newPassword.getCurrentPassword(), manager.loadUserByUsername(login).getPassword())) {
            return false;
        }

        User user = setPasswordInDb(login, this.encoder.encode(newPassword.getNewPassword()));
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

    public void saveUser(Register register, Role role, String encoderPassword) {
        userRepository.save(new User(register.getUsername(), register.getFirstName(), register.getLastName(), encoderPassword, register.getPhone(), LocalDate.now(), role));
    }


    public UserDto getUserDto(String login) {
        User user = userRepository.findUserByEmailIgnoreCase(login);
        if (user == null) {
            return null;
        }
        return userMapper.userIntoUserDto(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.findUserByEmailIgnoreCase(login);
    }

    public UpdateUser updateUser(UpdateUser updateUser, String login) {
        User user = userRepository.findUserByEmailIgnoreCase(login);
        if (user == null) {
            return null;
        }

        userRepository.save(userMapper.updateUserIntoUser(user, updateUser));
        return userMapper.userIntoUpdateUser(user);
    }

    public void updateUserImageDb(User user) {
        userRepository.save(user);
    }

}
