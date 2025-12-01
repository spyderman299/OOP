package main.java.controller;

import main.java.service.ServiceFactory;
import main.java.service.*;
import main.java.model.Subject;
import main.java.model.User;
import main.java.model.Homeroom;
import main.java.view.SubjectView;
import main.java.component.SubjectFormDialog;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubjectController {
    private SubjectView subjectView;
    private SubjectService subjectService;
    private UserService userService;
    private HomeroomService homeroomService;
    private boolean isTeacher;
    private String currentTeacherId;
    private Set<String> teacherSubjectIds; // Subjects that teacher teaches

    public SubjectController(SubjectView subjectView) {
        this.subjectView = subjectView;
        this.subjectService = ServiceFactory.getSubjectService();
        this.userService = ServiceFactory.getUserService();
        this.homeroomService = ServiceFactory.getHomeroomService();
        
        // Check if current user is TEACHER
        User currentUser = userService.getCurrentUser();
        this.isTeacher = currentUser != null && "TEACHER".equals(currentUser.getRole());
        this.currentTeacherId = currentUser != null ? currentUser.getTeacherId() : null;
        
        // Load teacher's subject IDs
        loadTeacherSubjectIds();
        
        setupEventHandlers();
        loadSubjects();
    }

    private void loadTeacherSubjectIds() {
        teacherSubjectIds = new HashSet<>();
        if (isTeacher && currentTeacherId != null) {
            List<Homeroom> teacherHomerooms = homeroomService.getHomeroomsByTeacherId(currentTeacherId);
            for (Homeroom homeroom : teacherHomerooms) {
                teacherSubjectIds.add(homeroom.getSubjectId());
            }
        }
    }

    private void setupEventHandlers() {
        // Only ADMIN can add/delete subjects
        if (isTeacher) {
            subjectView.getAddButton().setVisible(false);
            subjectView.getDeleteButton().setVisible(false);
            subjectView.getEditButton().setVisible(false); // Teacher cannot edit subjects
        } else {
            subjectView.getAddButton().addActionListener(e -> handleAdd());
            subjectView.getDeleteButton().addActionListener(e -> handleDelete());
            subjectView.getEditButton().addActionListener(e -> handleEdit());
        }
        subjectView.getDetailButton().addActionListener(e -> handleDetail());
    }

    private void loadSubjects() {
        try {
            List<Subject> subjects;
            if (isTeacher && currentTeacherId != null) {
                // Only load subjects that teacher teaches
                List<Subject> allSubjects = subjectService.getAllSubjects();
                subjects = new ArrayList<>();
                for (Subject subject : allSubjects) {
                    if (teacherSubjectIds.contains(subject.getSubjectId())) {
                        subjects.add(subject);
                    }
                }
            } else {
                subjects = subjectService.getAllSubjects();
            }
            subjectView.loadSubjects(subjects);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(subjectView,
                    "Lỗi khi tải danh sách môn học: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        SubjectFormDialog dialog = new SubjectFormDialog((JFrame) SwingUtilities.getWindowAncestor(subjectView), false, null);
        dialog.getSaveButton().addActionListener(e -> {
            Subject subject = dialog.getSubject();
            if (validateSubject(subject, false)) {
                if (subjectService.createSubject(subject)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm môn học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadSubjects();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        dialog.setVisible(true);
    }

    private void handleDetail() {
        String subjectId = subjectView.getSelectedSubjectId();
        if (subjectId == null) {
            JOptionPane.showMessageDialog(subjectView, "Vui lòng chọn một môn học!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Subject subject = subjectService.getSubjectById(subjectId);
        if (subject != null) {
            StringBuilder details = new StringBuilder();
            details.append("Mã môn: ").append(subject.getSubjectId()).append("\n");
            details.append("Tên môn: ").append(subject.getName()).append("\n");
            details.append("Tổng số buổi: ").append(subject.getTotalSessions()).append("\n");

            JOptionPane.showMessageDialog(subjectView, details.toString(), "Chi tiết môn học", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleEdit() {
        String subjectId = subjectView.getSelectedSubjectId();
        if (subjectId == null) {
            JOptionPane.showMessageDialog(subjectView, "Vui lòng chọn một môn học!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Subject subject = subjectService.getSubjectById(subjectId);
        if (subject != null) {
            SubjectFormDialog dialog = new SubjectFormDialog((JFrame) SwingUtilities.getWindowAncestor(subjectView), true, subject);
            dialog.getSaveButton().addActionListener(e -> {
                Subject updatedSubject = dialog.getSubject();
                if (validateSubject(updatedSubject, true)) {
                    if (subjectService.updateSubject(updatedSubject)) {
                        JOptionPane.showMessageDialog(dialog, "Sửa môn học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadSubjects();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi sửa môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            dialog.setVisible(true);
        }
    }

    private void handleDelete() {
        String subjectId = subjectView.getSelectedSubjectId();
        if (subjectId == null) {
            JOptionPane.showMessageDialog(subjectView, "Vui lòng chọn một môn học!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(subjectView,
                "Bạn có chắc chắn muốn xóa môn học này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (subjectService.deleteSubject(subjectId)) {
                JOptionPane.showMessageDialog(subjectView, "Xóa môn học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadSubjects();
            } else {
                JOptionPane.showMessageDialog(subjectView, "Lỗi khi xóa môn học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateSubject(Subject subject, boolean isEdit) {
        if (subject.getSubjectId() == null || subject.getSubjectId().trim().isEmpty()) {
            JOptionPane.showMessageDialog(subjectView, "Mã môn học không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (subject.getName() == null || subject.getName().trim().isEmpty()) {
            JOptionPane.showMessageDialog(subjectView, "Tên môn học không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (subject.getTotalSessions() < 0) {
            JOptionPane.showMessageDialog(subjectView, "Tổng số buổi phải >= 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}

