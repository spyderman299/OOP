package main.java.service.impl;

import main.java.model.User;
import main.java.repository.UserRepository;
import main.java.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private static UserServiceImpl instance;
    private UserRepository userRepository;
    private User currentUser;

    private UserServiceImpl() {
        this.userRepository = new UserRepository();
    }

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public User authenticateUser(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null;
        }

        User user = userRepository.findByUsername(username);
        if (user != null && user.getPasswordPlain().equals(password)) {
            setCurrentUser(user);
            return user;
        }
        return null;
    }

    @Override
    public boolean createUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return false;
        }

        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            System.err.println("Username already exists: " + user.getUsername());
            return false;
        }

        return userRepository.create(user);
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getUserId() <= 0) {
            return false;
        }

        // If password is empty, keep the old password
        if (user.getPasswordPlain() == null || user.getPasswordPlain().trim().isEmpty()) {
            User existingUser = userRepository.findById(user.getUserId());
            if (existingUser != null) {
                user.setPasswordPlain(existingUser.getPasswordPlain());
            }
        }

        return userRepository.update(user);
    }

    @Override
    public boolean deleteUser(int userId) {
        if (userId <= 0) {
            return false;
        }
        return userRepository.delete(userId);
    }

    @Override
    public User getUserById(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void logout() {
        this.currentUser = null;
    }
}

