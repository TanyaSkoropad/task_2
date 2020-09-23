package com.task.task.service.vm;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserVM {

    @NotNull
    private String email;

    @NotNull
    private String lastName;

    @NotNull
    private String firstName;

    @NotNull
    private String password;

}
