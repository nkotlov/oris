package ru.itis.services;

import org.mindrot.jbcrypt.BCrypt;
import ru.itis.models.User;
import ru.itis.repositories.UserRepository;
import ru.itis.repositories.impl.UserRepositoryJdbcImpl;

import javax.sql.DataSource;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(DataSource dataSource) {
        this.userRepository = new UserRepositoryJdbcImpl(dataSource);
    }

    @Override
    public String signInByEmail(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> BCrypt.checkpw(password, user.getPassword()))
                .map(User::getRole)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

    @Override
    public String signInByUsername(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> BCrypt.checkpw(password, user.getPassword()))
                .map(User::getRole)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
    }

    @Override
    public void signUp(String username, String email, String password) {
        if (isEmailExists(email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = User.builder()
                .username(username)
                .email(email)
                .password(hashedPassword)
                .role("USER")
                .build();
        userRepository.save(user);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public String getUsernameByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public String getRoleByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getRole)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким email не найден"));
    }

    @Override
    public String getRoleByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getRole)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким username не найден"));
    }
}
