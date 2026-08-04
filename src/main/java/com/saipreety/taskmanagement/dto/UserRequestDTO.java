package com.saipreety.taskmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank
    private String fullName;

    @NotBlank
    @Email(message = "It should be a valid email ID")
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password should be of minimum 8 characters")
    private String password;
}
