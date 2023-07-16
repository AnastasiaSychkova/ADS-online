package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.User;

public class UserMapper {

    public static UserDto userIntoUserDto(User user){
        return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhone(), null);
    }
    public static User updateUserIntoUser(User user, UpdateUser updateUser){
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(user.getPhone());
        return user;
    }
    public static UpdateUser userIntoUpdateUser(User user){
        return new UpdateUser(user.getFirstName(), user.getLastName(), user.getPhone());
    }
}
