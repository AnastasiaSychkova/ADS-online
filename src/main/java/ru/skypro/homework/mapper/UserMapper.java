package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.user.UpdateUser;
import ru.skypro.homework.dto.user.UserDto;
import ru.skypro.homework.model.User;
@Component
public class UserMapper {

    public UserDto userIntoUserDto(User user){
        return new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhone(), user.getImage() != null? ("/images/" + user.getImage().getId()):null);
    }
    public User updateUserIntoUser(User user, UpdateUser updateUser){
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setPhone(user.getPhone());
        return user;
    }
    public UpdateUser userIntoUpdateUser(User user){
        return new UpdateUser(user.getFirstName(), user.getLastName(), user.getPhone());
    }
}
