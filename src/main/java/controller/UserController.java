package main.java.controller;

import main.java.service.ServiceFactory;
import main.java.service.*;
import main.java.model.User;
import main.java.view.UserView;
import main.java.component.UserFormDialog;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {
    private UserView userView;
    private UserService userService;
    private TeacherService teacherService;
    private StudentService studentService;

    public UserController(UserView userView) {
        this.userView = userView;
        this.userService = ServiceFactory.getUserService();
        this.teacherService = ServiceFactory.getTeacherService();
        this.studentService = ServiceFactory.getStudentService();
        setupEventHandlers();
        loadUsers();
    }

    private void setupEventHandlers() {
        userView.getAddButton().addActionListener(e -> handleAdd());
        userView.getDetailButton().addActionListener(e -> handleDetail());
        userView.getEditButton().addActionListener(e -> handleEdit());
        userView.getDeleteButton().addActionListener(e -> handleDelete());
    }

    private void loadUsers() {
        try {
            List<User> users = userService.getAllUsers();
            Map<String, String> teacherNames = new HashMap<>();
            teacherService.getAllTeachers().forEach(t -> teacherNames.put(t.getTeacherId(), t.getName()));
            
            Map<String, String> studentNames = new HashMap<>();
            studentService.getAllStudents().forEach(s -> studentNames.put(s.getStudentId(), s.getName()));
            
            userView.loadUsers(users, teacherNames, studentNames);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(userView,
                    "Lỗi khi tải danh sách người dùng: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        UserFormDialog dialog = new UserFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(userView),
                false,
                null,
                teacherService.getAllTeachers(),
                studentService.getAllStudents());
        dialog.getSaveButton().addActionListener(e -> {
            User user = dialog.getUser();
            if (validateUser(user, false)) {
                if (userService.createUser(user)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm người dùng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadUsers();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        dialog.setVisible(true);
    }

    private void handleDetail() {
        String username = userView.getSelectedUsername();
        if (username == null) {
            JOptionPane.showMessageDialog(userView, "Vui lòng chọn một người dùng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userService.getUserById(getUserIdByUsername(username));
        if (user != null) {
            StringBuilder details = new StringBuilder();
            details.append("Username: ").append(user.getUsername()).append("\n");
            details.append("Role: ").append(user.getRole()).append("\n");
            if (user.getTeacherId() != null) {
                details.append("Liên kết: Giáo viên - ").append(user.getTeacherId()).append("\n");
            } else if (user.getStudentId() != null) {
                details.append("Liên kết: Sinh viên - ").append(user.getStudentId()).append("\n");
            }

            JOptionPane.showMessageDialog(userView, details.toString(), "Chi tiết người dùng", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleEdit() {
        String username = userView.getSelectedUsername();
        if (username == null) {
            JOptionPane.showMessageDialog(userView, "Vui lòng chọn một người dùng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userService.getUserById(getUserIdByUsername(username));
        if (user != null) {
            UserFormDialog dialog = new UserFormDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(userView),
                    true,
                    user,
                    teacherService.getAllTeachers(),
                    studentService.getAllStudents());
            dialog.getSaveButton().addActionListener(e -> {
                User updatedUser = dialog.getUser();
                if (validateUser(updatedUser, true)) {
                    if (userService.updateUser(updatedUser)) {
                        JOptionPane.showMessageDialog(dialog, "Sửa người dùng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi sửa người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            dialog.setVisible(true);
        }
    }

    private void handleDelete() {
        String username = userView.getSelectedUsername();
        if (username == null) {
            JOptionPane.showMessageDialog(userView, "Vui lòng chọn một người dùng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = getUserIdByUsername(username);
        User currentUser = userService.getCurrentUser();
        if (currentUser != null && currentUser.getUserId() == userId) {
            JOptionPane.showMessageDialog(userView, "Bạn không thể xóa chính mình!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(userView,
                "Bạn có chắc chắn muốn xóa người dùng này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (userService.deleteUser(userId)) {
                JOptionPane.showMessageDialog(userView, "Xóa người dùng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(userView, "Lỗi khi xóa người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int getUserIdByUsername(String username) {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user.getUserId();
            }
        }
        return -1;
    }

    private boolean validateUser(User user, boolean isEdit) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            JOptionPane.showMessageDialog(userView, "Username không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!isEdit && (user.getPasswordPlain() == null || user.getPasswordPlain().trim().isEmpty())) {
            JOptionPane.showMessageDialog(userView, "Mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}

