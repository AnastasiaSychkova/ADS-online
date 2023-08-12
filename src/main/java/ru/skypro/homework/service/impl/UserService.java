package ru.skypro.homework.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.user.NewPassword;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;

/** Сервис для работы с сущностью User */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final ImageService imageService;

    public UserService(UserRepository userRepository, UserDetailsManager manager, PasswordEncoder encoder, UserMapper userMapper, ImageService imageService) {
        this.userRepository = userRepository;
        this.manager = manager;
        this.encoder = encoder;
        this.userMapper = userMapper;
        this.imageService = imageService;
    }

    /** Метод для изменения пароля в бд */
    private User setPasswordInDb(String login, String newPassword) {
        User user = userRepository.findUserByEmailIgnoreCase(login);
        if (user == null) {
            return null;
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return user;
    }


    /** Метод для изменения пароля */
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


    /** Метод для сохранения пользователя в бд */
    public void saveUser(Register register, Role role, String encoderPassword) {
        userRepository.save(new User(register.getUsername(), register.getFirstName(), register.getLastName(), encoderPassword, register.getPhone(), LocalDate.now(), role));
    }


    /** Метод для получения UserDto */
    public UserDto getUserDto(String login) {
        User user = userRepository.findUserByEmailIgnoreCase(login);
        if (user == null) {
            return null;
        }
        return userMapper.userIntoUserDto(user);
    }


    /** Метод для поиска пользователя в бд */
    public User getUserByLogin(String login) {
        return userRepository.findUserByEmailIgnoreCase(login);
    }


    /** Метод для изменения пользователя */
    public UpdateUser updateUser(UpdateUser updateUser, String login) {
        User user = userRepository.findUserByEmailIgnoreCase(login);
        if (user == null) {
            return null;
        }

        userRepository.save(userMapper.updateUserIntoUser(user, updateUser));
        return userMapper.userIntoUpdateUser(user);
    }


    /** Метод для обновления аватара */
    public void updateUserImageInDb(MultipartFile file, String login) {
        try {
            Image image = null;
            User user = userRepository.findUserByEmailIgnoreCase(login);
            if(user.getImage() == null){
                 image = imageService.updateImage(file);
            }else {
                image = imageService.updateImageWithoutSaveInDb(file);
                image.setId(user.getId());
                imageService.save(image);
            }

            user.setImage(image);
            userRepository.save(user);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
