package ru.itis.services;

public interface UserService {
    String signInByEmail(String email, String password);
    String signInByUsername(String username, String password);
    void signUp(String username, String email, String password);
    boolean isEmailExists(String email);
    String getUsernameByEmail(String email);
    String getRoleByEmail(String email);
    String getRoleByUsername(String username);
}
