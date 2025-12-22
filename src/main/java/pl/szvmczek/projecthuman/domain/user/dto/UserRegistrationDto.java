package pl.szvmczek.projecthuman.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {
    @Email(message = "Email is not valid")
    private String email;
    @Size(min = 6,message = "Password must be at least 6 characters long")
    private String password;

    public UserRegistrationDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserRegistrationDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
