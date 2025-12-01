package main.java.controller;

import main.java.service.ServiceFactory;
import main.java.service.*;
import main.java.model.Enrollment;
import main.java.model.User;
import main.java.model.Homeroom;
import main.java.view.EnrollmentView;
import main.java.component.EnrollmentFormDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EnrollmentController {
    private EnrollmentView enrollmentView;
    private EnrollmentService enrollmentService;
    private StudentService studentService;
    private HomeroomService homeroomService;
    private UserService userService;
    private boolean isStudentView;
    private boolean isTeacher;
    private String currentTeacherId;
    private List<String> teacherClassIds; // Classes that teacher teaches

    public EnrollmentController(EnrollmentView enrollmentView) {
        this.enrollmentView = enrollmentView;
        this.enrollmentService = ServiceFactory.getEnrollmentService();
        this.studentService = ServiceFactory.getStudentService();
        this.homeroomService = ServiceFactory.getHomeroomService();
        this.userService = ServiceFactory.getUserService();
        this.isStudentView = enrollmentView.isStudentView;
        
        // Check if current user is TEACHER
        User currentUser = userService.getCurrentUser();
        this.isTeacher = currentUser != null && "TEACHER".equals(currentUser.getRole());
        this.currentTeacherId = currentUser != null ? currentUser.getTeacherId() : null;
        
        // Load teacher's class IDs
        loadTeacherClassIds();
        
        setupEventHandlers();
        loadEnrollments();
        setupFilters();
    }

    private void loadTeacherClassIds() {
        teacherClassIds = new ArrayList<>();
        if (isTeacher && currentTeacherId != null) {
            List<Homeroom> teacherHomerooms = homeroomService.getHomeroomsByTeacherId(currentTeacherId);
            for (Homeroom homeroom : teacherHomerooms) {
                teacherClassIds.add(homeroom.getClassId());
            }
        }
    }

    private void setupEventHandlers() {
        if (!isStudentView) {
            if (enrollmentView.getClassFilterComboBox() != null) {
                enrollmentView.getClassFilterComboBox().addActionListener(e -> loadEnrollments());
            }
            if (enrollmentView.getStudentFilterComboBox() != null) {
                enrollmentView.getStudentFilterComboBox().addActionListener(e -> loadEnrollments());
            }
            
            enrollmentView.getAddButton().addActionListener(e -> handleAdd());
            enrollmentView.getDetailButton().addActionListener(e -> handleDetail());
            enrollmentView.getEditButton().addActionListener(e -> handleEdit());
            enrollmentView.getDeleteButton().addActionListener(e -> handleDelete());
        }
    }

    private void setupFilters() {
        if (!isStudentView) {
            List<String> classes = new ArrayList<>();
            
            // If TEACHER, only show their classes
            if (isTeacher && currentTeacherId != null) {
                classes.addAll(teacherClassIds);
            } else {
                homeroomService.getAllHomerooms().forEach(h -> classes.add(h.getClassId()));
            }
            enrollmentView.setClassFilterItems(classes);

            // Get students in teacher's classes or all students
            List<String> students = new ArrayList<>();
            if (isTeacher && currentTeacherId != null) {
                // Get unique students from teacher's classes
                Set<String> studentIds = new HashSet<>();
                for (String classId : teacherClassIds) {
                    enrollmentService.getEnrollmentsByClassId(classId).forEach(e -> studentIds.add(e.getStudentId()));
                }
                for (String studentId : studentIds) {
                    var student = studentService.getStudentById(studentId);
                    if (student != null) {
                        students.add(student.getStudentId() + " - " + student.getName());
                    }
                }
            } else {
                studentService.getAllStudents().forEach(s -> students.add(s.getStudentId() + " - " + s.getName()));
            }
            enrollmentView.setStudentFilterItems(students);
        }
    }

    private void loadEnrollments() {
        try {
            List<Enrollment> enrollments;
            
            if (isStudentView) {
                // For students, only show their own enrollments
                String studentId = userService.getCurrentUser().getStudentId();
                if (studentId != null) {
                    enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
                } else {
                    enrollments = new ArrayList<>();
                }
            } else if (isTeacher && currentTeacherId != null) {
                // For TEACHER, only show enrollments in their classes
                String classFilter = enrollmentView.getSelectedClassFilter();
                String studentFilter = enrollmentView.getSelectedStudentFilter();
                
                if (classFilter != null && teacherClassIds.contains(classFilter)) {
                    enrollments = enrollmentService.getEnrollmentsByClassId(classFilter);
                } else if (studentFilter != null) {
                    String studentId = studentFilter.split(" - ")[0];
                    enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
                    // Filter to only teacher's classes
                    enrollments = enrollments.stream()
                            .filter(e -> teacherClassIds.contains(e.getClassId()))
                            .collect(java.util.stream.Collectors.toList());
                } else {
                    // Load all enrollments for teacher's classes
                    enrollments = new ArrayList<>();
                    for (String classId : teacherClassIds) {
                        enrollments.addAll(enrollmentService.getEnrollmentsByClassId(classId));
                    }
                }
            } else {
                // For ADMIN, apply filters
                String classFilter = enrollmentView.getSelectedClassFilter();
                String studentFilter = enrollmentView.getSelectedStudentFilter();
                
                if (classFilter != null) {
                    enrollments = enrollmentService.getEnrollmentsByClassId(classFilter);
                } else if (studentFilter != null) {
                    String studentId = studentFilter.split(" - ")[0];
                    enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
                } else {
                    enrollments = enrollmentService.getAllEnrollments();
                }
            }

            Map<String, String> studentNames = new HashMap<>();
            studentService.getAllStudents().forEach(s -> studentNames.put(s.getStudentId(), s.getName()));

            enrollmentView.loadEnrollments(enrollments, studentNames);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(enrollmentView,
                    "Lỗi khi tải danh sách điểm số: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        List<String> classes = new ArrayList<>();
        // If teacher, only show their classes
        if (isTeacher && currentTeacherId != null) {
            classes.addAll(teacherClassIds);
        } else {
            homeroomService.getAllHomerooms().forEach(h -> classes.add(h.getClassId()));
        }
        
        List<String> students = new ArrayList<>();
        // If teacher, only show students in their classes
        if (isTeacher && currentTeacherId != null) {
            Set<String> studentIds = new HashSet<>();
            for (String classId : teacherClassIds) {
                enrollmentService.getEnrollmentsByClassId(classId).forEach(e -> studentIds.add(e.getStudentId()));
            }
            for (String studentId : studentIds) {
                var student = studentService.getStudentById(studentId);
                if (student != null) {
                    students.add(student.getStudentId() + " - " + student.getName());
                }
            }
            // Also include all students for new enrollment
            studentService.getAllStudents().forEach(s -> {
                String item = s.getStudentId() + " - " + s.getName();
                if (!students.contains(item)) {
                    students.add(item);
                }
            });
        } else {
            studentService.getAllStudents().forEach(s -> students.add(s.getStudentId() + " - " + s.getName()));
        }
        
        EnrollmentFormDialog dialog = new EnrollmentFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(enrollmentView),
                false,
                null,
                classes,
                students);
        dialog.getSaveButton().addActionListener(e -> {
            Enrollment enrollment = dialog.getEnrollment();
            if (validateEnrollment(enrollment, false)) {
                // Check if teacher can add to this class
                if (isTeacher && currentTeacherId != null && !teacherClassIds.contains(enrollment.getClassId())) {
                    JOptionPane.showMessageDialog(dialog, "Bạn không có quyền thêm sinh viên vào lớp này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (enrollmentService.createEnrollment(enrollment)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm đăng ký thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadEnrollments();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm đăng ký!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        dialog.setVisible(true);
    }

    private void handleDetail() {
        String classId = enrollmentView.getSelectedClassId();
        String studentId = enrollmentView.getSelectedStudentId();
        if (classId == null || studentId == null) {
            JOptionPane.showMessageDialog(enrollmentView, "Vui lòng chọn một đăng ký!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Enrollment enrollment = enrollmentService.getEnrollmentById(classId, studentId);
        if (enrollment != null) {
            StringBuilder details = new StringBuilder();
            details.append("Mã lớp: ").append(enrollment.getClassId()).append("\n");
            details.append("Mã SV: ").append(enrollment.getStudentId()).append("\n");
            details.append("Chuyên cần: ").append(enrollment.getAttendance()).append("\n");
            details.append("Bài tập: ").append(enrollment.getHomework()).append("\n");
            details.append("Giữa kỳ: ").append(enrollment.getMidTerm()).append("\n");
            details.append("Cuối kỳ: ").append(enrollment.getEndTerm()).append("\n");
            details.append("Tổng kết: ").append(enrollment.getFinalScore()).append("\n");
            details.append("Kết quả: ").append(enrollment.getResult()).append("\n");

            JOptionPane.showMessageDialog(enrollmentView, details.toString(), "Chi tiết điểm số", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleEdit() {
        String classId = enrollmentView.getSelectedClassId();
        String studentId = enrollmentView.getSelectedStudentId();
        if (classId == null || studentId == null) {
            JOptionPane.showMessageDialog(enrollmentView, "Vui lòng chọn một đăng ký!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if teacher can edit this enrollment
        if (isTeacher && currentTeacherId != null && !teacherClassIds.contains(classId)) {
            JOptionPane.showMessageDialog(enrollmentView, "Bạn không có quyền sửa điểm lớp này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Enrollment enrollment = enrollmentService.getEnrollmentById(classId, studentId);
        if (enrollment != null) {
            List<String> classes = new ArrayList<>();
            classes.add(enrollment.getClassId());
            
            List<String> students = new ArrayList<>();
            students.add(enrollment.getStudentId() + " - " + studentService.getStudentById(enrollment.getStudentId()).getName());
            
            EnrollmentFormDialog dialog = new EnrollmentFormDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(enrollmentView),
                    true,
                    enrollment,
                    classes,
                    students);
            dialog.getSaveButton().addActionListener(e -> {
                Enrollment updatedEnrollment = dialog.getEnrollment();
                if (validateEnrollment(updatedEnrollment, true)) {
                    if (enrollmentService.updateEnrollment(updatedEnrollment)) {
                        JOptionPane.showMessageDialog(dialog, "Sửa điểm số thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadEnrollments();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi sửa điểm số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            dialog.setVisible(true);
        }
    }

    private void handleDelete() {
        String classId = enrollmentView.getSelectedClassId();
        String studentId = enrollmentView.getSelectedStudentId();
        if (classId == null || studentId == null) {
            JOptionPane.showMessageDialog(enrollmentView, "Vui lòng chọn một đăng ký!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if teacher can delete this enrollment
        if (isTeacher && currentTeacherId != null && !teacherClassIds.contains(classId)) {
            JOptionPane.showMessageDialog(enrollmentView, "Bạn không có quyền xóa đăng ký lớp này!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(enrollmentView,
                "Bạn có chắc chắn muốn xóa đăng ký này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (enrollmentService.deleteEnrollment(classId, studentId)) {
                JOptionPane.showMessageDialog(enrollmentView, "Xóa đăng ký thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadEnrollments();
            } else {
                JOptionPane.showMessageDialog(enrollmentView, "Lỗi khi xóa đăng ký!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateEnrollment(Enrollment enrollment, boolean isEdit) {
        if (enrollment.getClassId() == null || enrollment.getStudentId() == null) {
            JOptionPane.showMessageDialog(enrollmentView, "Vui lòng chọn lớp học và sinh viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}

