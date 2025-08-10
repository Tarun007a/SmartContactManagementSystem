package com.scm.projectSCM.forms;

import com.scm.projectSCM.helper.validators.ValidFile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid Email address")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
    private String phoneNumber;

    private String address;

    private String description;

    private boolean favorite;

    private String websiteLink;

    private String linkedInLink;

    private String picture;

    @ValidFile
    private MultipartFile profilePic;
}





