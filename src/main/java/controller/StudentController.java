package main.java.controller;

import main.java.service.ServiceFactory;
import main.java.service.*;
import main.java.model.Student;
import main.java.model.User;
import main.java.model.Homeroom;
import main.java.model.Enrollment;
import main.java.view.StudentView;
import main.java.component.StudentFormDialog;
import main.java.utility.Helper;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentController {
    private StudentView studentView;
    private StudentService studentService;
    private UserService userService;
    private HomeroomService homeroomService;
    private EnrollmentService enrollmentService;
    private boolean isTeacher;
    private String currentTeacherId;
    private Set<String> allowedStudentIds; // Students in teacher's classes

    public StudentController(StudentView studentView) {
        this.studentView = studentView;
        this.studentService = ServiceFactory.getStudentService();
        this.userService = ServiceFactory.getUserService();
        this.homeroomService = ServiceFactory.getHomeroomService();
        this.enrollmentService = ServiceFactory.getEnrollmentService();
        
        // Check if current user is TEACHER
        User currentUser = userService.getCurrentUser();
        this.isTeacher = currentUser != null && "TEACHER".equals(currentUser.getRole());
        this.currentTeacherId = currentUser != null ? currentUser.getTeacherId() : null;
        
        // Load allowed student IDs for teacher
        loadAllowedStudentIds();
        
        setupEventHandlers();
        loadStudents();
    }

    private void loadAllowedStudentIds() {
        allowedStudentIds = new HashSet<>();
        if (isTeacher && currentTeacherId != null) {
            // Get all homerooms for this teacher
            List<Homeroom> teacherHomerooms = homeroomService.getHomeroomsByTeacherId(currentTeacherId);
            // Get all students enrolled in these homerooms
            for (Homeroom homeroom : teacherHomerooms) {
                List<Enrollment> enrollments = enrollmentService.getEnrollmentsByClassId(homeroom.getClassId());
                for (Enrollment enrollment : enrollments) {
                    allowedStudentIds.add(enrollment.getStudentId());
                }
            }
        }
    }

    private void setupEventHandlers() {
        // Search
        studentView.getSearchField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });

        // Only ADMIN can add/delete students
        if (isTeacher) {
            studentView.getAddButton().setVisible(false);
            studentView.getDeleteButton().setVisible(false);
        } else {
            studentView.getAddButton().addActionListener(e -> handleAdd());
            studentView.getDeleteButton().addActionListener(e -> handleDelete());
        }

        // Detail button
        studentView.getDetailButton().addActionListener(e -> handleDetail());

        // Edit button
        studentView.getEditButton().addActionListener(e -> handleEdit());
    }

    private void loadStudents() {
        try {
            List<Student> students;
            if (isTeacher && currentTeacherId != null) {
                // Only load students in teacher's classes
                students = studentService.getStudentsByIds(new ArrayList<>(allowedStudentIds));
            } else {
                students = studentService.getAllStudents();
            }
            studentView.loadStudents(students);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(studentView,
                    "Lỗi khi tải danh sách sinh viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch() {
        String searchText = studentView.getSearchText();
        try {
            if (searchText.isEmpty()) {
                loadStudents();
            } else {
                List<Student> searchResults = studentService.searchStudentsByName(searchText);
                // If teacher, filter to only allowed students
                if (isTeacher && currentTeacherId != null) {
                    searchResults = searchResults.stream()
                            .filter(s -> allowedStudentIds.contains(s.getStudentId()))
                            .collect(java.util.stream.Collectors.toList());
                }
                studentView.loadStudents(searchResults);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(studentView,
                    "Lỗi khi tìm kiếm: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        StudentFormDialog dialog = new StudentFormDialog((JFrame) SwingUtilities.getWindowAncestor(studentView), false, null);
        dialog.getSaveButton().addActionListener(e -> {
            Student student = dialog.getStudent();
            if (validateStudent(student, false)) {
                if (studentService.createStudent(student)) {
                    JOptionPane.showMessageDialog(dialog,
                            "Thêm sinh viên thành công!",
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadStudents();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Lỗi khi thêm sinh viên!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        dialog.setVisible(true);
    }

    private void handleDetail() {
        String studentId = studentView.getSelectedStudentId();
        if (studentId == null) {
            JOptionPane.showMessageDialog(studentView,
                    "Vui lòng chọn một sinh viên!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            showDetailDialog(student);
        }
    }

    private void handleEdit() {
        String studentId = studentView.getSelectedStudentId();
        if (studentId == null) {
            JOptionPane.showMessageDialog(studentView,
                    "Vui lòng chọn một sinh viên!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            StudentFormDialog dialog = new StudentFormDialog((JFrame) SwingUtilities.getWindowAncestor(studentView), true, student);
            dialog.getSaveButton().addActionListener(e -> {
                Student updatedStudent = dialog.getStudent();
                if (validateStudent(updatedStudent, true)) {
                    if (studentService.updateStudent(updatedStudent)) {
                        JOptionPane.showMessageDialog(dialog,
                                "Sửa sinh viên thành công!",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadStudents();
                    } else {
                        JOptionPane.showMessageDialog(dialog,
                                "Lỗi khi sửa sinh viên!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            dialog.setVisible(true);
        }
    }

    private void handleDelete() {
        String studentId = studentView.getSelectedStudentId();
        if (studentId == null) {
            JOptionPane.showMessageDialog(studentView,
                    "Vui lòng chọn một sinh viên!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(studentView,
                "Bạn có chắc chắn muốn xóa sinh viên này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (studentService.deleteStudent(studentId)) {
                JOptionPane.showMessageDialog(studentView,
                        "Xóa sinh viên thành công!",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(studentView,
                        "Lỗi khi xóa sinh viên!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateStudent(Student student, boolean isEdit) {
        if (student.getStudentId() == null || student.getStudentId().trim().isEmpty()) {
            JOptionPane.showMessageDialog(studentView,
                    "Mã sinh viên không được để trống!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            JOptionPane.showMessageDialog(studentView,
                    "Họ tên không được để trống!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (student.getPhone() != null && !student.getPhone().trim().isEmpty()) {
            if (!Helper.isValidPhone(student.getPhone())) {
                JOptionPane.showMessageDialog(studentView,
                        "Số điện thoại không hợp lệ! (Định dạng: 0xxxxxxxxx)",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    private void showDetailDialog(Student student) {
        StringBuilder details = new StringBuilder();
        details.append("Mã SV: ").append(student.getStudentId()).append("\n");
        details.append("Họ tên: ").append(student.getName()).append("\n");
        details.append("Ngày sinh: ").append(student.getDob() != null ? Helper.formatDate(student.getDob()) : "").append("\n");
        details.append("SĐT: ").append(student.getPhone() != null ? student.getPhone() : "").append("\n");
        details.append("Tuổi: ").append(student.getAge()).append("\n");

        JOptionPane.showMessageDialog(studentView,
                details.toString(),
                "Chi tiết sinh viên",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

