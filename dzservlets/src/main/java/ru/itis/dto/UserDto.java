package ru.itis.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private String username;
    private String email;
    private String role;

    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(this.role);
    }
}