package ru.skypro.homework.dto.user;

import lombok.Data;

@Data
public class NewPassword {
    private String currentPassword;
    private String newPassword;

    public NewPassword(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
