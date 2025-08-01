package com.restapi.restapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordsDTO {

    @NotBlank(message = "Name is required!")
    public String name;

    @NotBlank(message = "Password is required!")
    @Size(min = 8, message = "Password must be at least 8 characters long!")
    public String password;

    public String url;

    public String notes;
}
