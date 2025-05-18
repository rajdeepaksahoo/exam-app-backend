package com.online.exam.dto;

import com.online.exam.model.CustomAuthorities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import jakarta.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long userId;
    @NotBlank(message = "Email is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Confirm Password is required")
    private String confirmPassword;
    @NotBlank(message = "First Name is required")
    private String firstName;
    private String lastName;
    @NotBlank(message = "Gender is required")
    private String gender;
    private List<CustomAuthorities> roles;

    public @NotBlank(message = "Confirm Password is required") String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank(message = "Confirm Password is required") String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public @NotBlank(message = "First Name is required") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First Name is required") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Gender is required") String getGender() {
        return gender;
    }

    public void setGender(@NotBlank(message = "Gender is required") String gender) {
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank(message = "Password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is required") String password) {
        this.password = password;
    }

    public List<CustomAuthorities> getRoles() {
        return roles;
    }

    public void setRoles(List<CustomAuthorities> roles) {
        this.roles = roles;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public @NotBlank(message = "Email is required") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Email is required") String username) {
        this.username = username;
    }
}
