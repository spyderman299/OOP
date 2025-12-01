package main.java.service;

import main.java.model.User;

public interface UserService {
    User authenticateUser(String username, String password);
    boolean createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    User getUserById(int userId);
    java.util.List<User> getAllUsers();
    User getCurrentUser();
    void setCurrentUser(User user);
    void logout();
}

