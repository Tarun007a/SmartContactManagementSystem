package com.scm.projectSCM.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {
    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "minimum 3 characters is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "invalid Email Address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "minimum 6 characters is required")
    private String password;

    @NotBlank(message = "About is required")
    private String about;

    @NotBlank(message = "PheNone Number is required")
    @Size(min = 8, max = 12, message = "Invalid Phone Number")
    private String phoneNumber;
}
