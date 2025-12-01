package main.java.controller;

import main.java.service.ServiceFactory;
import main.java.service.UserService;
import main.java.model.User;
import main.java.view.LoginView;
import main.java.view.DashboardView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    private LoginView loginView;
    private UserService userService;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        this.userService = ServiceFactory.getUserService();
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        // Login button
        loginView.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Enter key on password field
        loginView.getPasswordField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = loginView.getUsernameField().getText().trim();
        String password = new String(loginView.getPasswordField().getPassword());

        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView,
                    "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Authenticate
        User user = userService.authenticateUser(username, password);
        if (user != null) {
            // Success - open dashboard
            loginView.dispose();
            DashboardView dashboardView = new DashboardView();
            new DashboardController(dashboardView);
            dashboardView.setVisible(true);
        } else {
            // Failed
            JOptionPane.showMessageDialog(loginView,
                    "Tên đăng nhập hoặc mật khẩu không đúng!",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
            loginView.getPasswordField().setText("");
        }
    }
}

