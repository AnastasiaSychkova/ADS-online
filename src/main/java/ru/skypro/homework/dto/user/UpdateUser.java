package ru.skypro.homework.dto.user;

import lombok.Data;

@Data
public class UpdateUser {
    private String firstName;
    private String lastName;
    private String phone;

    public UpdateUser(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
