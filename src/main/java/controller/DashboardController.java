package main.java.controller;

import main.java.service.ServiceFactory;
import main.java.service.*;
import main.java.model.User;
import main.java.model.Homeroom;
import main.java.model.Enrollment;
import main.java.view.DashboardView;
import main.java.view.StudentView;
import main.java.view.TeacherView;
import main.java.view.SubjectView;
import main.java.view.HomeroomView;
import main.java.view.EnrollmentView;
import main.java.view.UserView;
import main.java.component.SidebarPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.CardLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardController {
    private DashboardView dashboardView;
    private UserService userService;
    private StudentService studentService;
    private TeacherService teacherService;
    private SubjectService subjectService;
    private HomeroomService homeroomService;
    private EnrollmentService enrollmentService;

    public DashboardController(DashboardView dashboardView) {
        this.dashboardView = dashboardView;
        this.userService = ServiceFactory.getUserService();
        this.studentService = ServiceFactory.getStudentService();
        this.teacherService = ServiceFactory.getTeacherService();
        this.subjectService = ServiceFactory.getSubjectService();
        this.homeroomService = ServiceFactory.getHomeroomService();
        this.enrollmentService = ServiceFactory.getEnrollmentService();

        initialize();
    }

    private void initialize() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(dashboardView,
                    "Không tìm thấy thông tin người dùng!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
            return;
        }

        // Set user info in header and sidebar
        dashboardView.getHeaderPanel().setUserInfo(currentUser.getUsername(), currentUser.getRole());
        dashboardView.getSidebarPanel().setUserInfo(currentUser.getUsername(), currentUser.getRole());

        // Setup logout handler
        dashboardView.getHeaderPanel().setOnLogout(() -> {
            userService.logout();
            dashboardView.dispose();
            main.java.view.LoginView loginView = new main.java.view.LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
        });

        // Setup sidebar menu listener
        dashboardView.getSidebarPanel().setMenuItemListener(new SidebarPanel.MenuItemListener() {
            @Override
            public void onMenuItemSelected(String menuId) {
                handleMenuSelection(menuId);
            }
        });

        // Load statistics
        loadStatistics();

        // Show dashboard by default
        dashboardView.getCardLayout().show(dashboardView.getContentPanel(), "DASHBOARD");
        dashboardView.getSidebarPanel().selectMenuItemById("HOME");
    }

    private void loadStatistics() {
        try {
            User currentUser = userService.getCurrentUser();
            boolean isTeacher = currentUser != null && "TEACHER".equals(currentUser.getRole());
            String teacherId = currentUser != null ? currentUser.getTeacherId() : null;
            
            int studentCount;
            int teacherCount;
            int subjectCount;
            int classCount;
            
            if (isTeacher && teacherId != null) {
                // For TEACHER: only count their data
                List<Homeroom> teacherHomerooms = homeroomService.getHomeroomsByTeacherId(teacherId);
                classCount = teacherHomerooms.size();
                
                // Count unique students in teacher's classes
                Set<String> studentIds = new HashSet<>();
                Set<String> subjectIds = new HashSet<>();
                for (Homeroom homeroom : teacherHomerooms) {
                    subjectIds.add(homeroom.getSubjectId());
                    List<Enrollment> enrollments = enrollmentService.getEnrollmentsByClassId(homeroom.getClassId());
                    for (Enrollment enrollment : enrollments) {
                        studentIds.add(enrollment.getStudentId());
                    }
                }
                studentCount = studentIds.size();
                subjectCount = subjectIds.size();
                teacherCount = 1; // Just themselves
            } else {
                // For ADMIN: count all data
                studentCount = studentService.getAllStudents().size();
                teacherCount = teacherService.getAllTeachers().size();
                subjectCount = subjectService.getAllSubjects().size();
                classCount = homeroomService.getAllHomerooms().size();
            }

            dashboardView.updateStatistic("Số lượng sinh viên", String.valueOf(studentCount));
            dashboardView.updateStatistic("Số lượng giáo viên", String.valueOf(teacherCount));
            dashboardView.updateStatistic("Số lượng môn học", String.valueOf(subjectCount));
            dashboardView.updateStatistic("Số lượng lớp học", String.valueOf(classCount));
        } catch (Exception e) {
            System.err.println("Error loading statistics: " + e.getMessage());
        }
    }

    private void handleMenuSelection(String menuId) {
        CardLayout cardLayout = dashboardView.getCardLayout();
        JPanel contentPanel = dashboardView.getContentPanel();

        switch (menuId) {
            case "HOME":
                cardLayout.show(contentPanel, "DASHBOARD");
                loadStatistics();
                break;
            case "STUDENTS":
                showView("STUDENTS", () -> {
                    StudentView view = new StudentView();
                    new StudentController(view);
                    return view;
                });
                break;
            case "TEACHERS":
                if (checkPermission("ADMIN")) {
                    showView("TEACHERS", () -> {
                        TeacherView view = new TeacherView();
                        new TeacherController(view);
                        return view;
                    });
                }
                break;
            case "SUBJECTS":
                if (checkPermission("ADMIN", "TEACHER")) {
                    showView("SUBJECTS", () -> {
                        SubjectView view = new SubjectView();
                        new SubjectController(view);
                        return view;
                    });
                }
                break;
            case "CLASSES":
                if (checkPermission("ADMIN", "TEACHER")) {
                    showView("CLASSES", () -> {
                        HomeroomView view = new HomeroomView();
                        new HomeroomController(view);
                        return view;
                    });
                }
                break;
            case "ENROLLMENTS":
                User currentUserForEnrollment = userService.getCurrentUser();
                showView("ENROLLMENTS", () -> {
                    boolean isStudent = currentUserForEnrollment != null && "STUDENT".equals(currentUserForEnrollment.getRole());
                    EnrollmentView view = new EnrollmentView(isStudent);
                    new EnrollmentController(view);
                    return view;
                });
                break;
            case "USERS":
                if (checkPermission("ADMIN")) {
                    showView("USERS", () -> {
                        UserView view = new UserView();
                        new UserController(view);
                        return view;
                    });
                }
                break;
        }
    }

    private void showView(String viewId, ViewFactory factory) {
        CardLayout cardLayout = dashboardView.getCardLayout();
        JPanel contentPanel = dashboardView.getContentPanel();

        // Check if view already exists
        Component existingView = null;
        for (Component comp : contentPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(viewId)) {
                existingView = comp;
                break;
            }
        }

        if (existingView == null) {
            JPanel view = factory.create();
            view.setName(viewId);
            contentPanel.add(view, viewId);
        }

        cardLayout.show(contentPanel, viewId);
    }

    private boolean checkPermission(String... allowedRoles) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return false;
        }

        String userRole = currentUser.getRole();
        for (String role : allowedRoles) {
            if (role.equals(userRole)) {
                return true;
            }
        }

        JOptionPane.showMessageDialog(dashboardView,
                "Bạn không có quyền truy cập chức năng này!",
                "Không có quyền",
                JOptionPane.WARNING_MESSAGE);
        return false;
    }

    @FunctionalInterface
    private interface ViewFactory {
        JPanel create();
    }
}

