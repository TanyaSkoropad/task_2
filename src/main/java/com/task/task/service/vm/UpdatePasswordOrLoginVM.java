package com.task.task.service.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordOrLoginVM {

    private String currentPassword;

    private String newPassword;

    private String newEmail;

}
