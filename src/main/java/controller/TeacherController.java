package main.java.controller;

import main.java.service.ServiceFactory;
import main.java.service.TeacherService;
import main.java.model.Teacher;
import main.java.view.TeacherView;
import main.java.component.TeacherFormDialog;
import main.java.utility.Helper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeacherController {
    private TeacherView teacherView;
    private TeacherService teacherService;

    public TeacherController(TeacherView teacherView) {
        this.teacherView = teacherView;
        this.teacherService = ServiceFactory.getTeacherService();
        setupEventHandlers();
        loadTeachers();
    }

    private void setupEventHandlers() {
        teacherView.getAddButton().addActionListener(e -> handleAdd());
        teacherView.getDetailButton().addActionListener(e -> handleDetail());
        teacherView.getEditButton().addActionListener(e -> handleEdit());
        teacherView.getDeleteButton().addActionListener(e -> handleDelete());
    }

    private void loadTeachers() {
        try {
            teacherView.loadTeachers(teacherService.getAllTeachers());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(teacherView,
                    "Lỗi khi tải danh sách giáo viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        TeacherFormDialog dialog = new TeacherFormDialog((JFrame) SwingUtilities.getWindowAncestor(teacherView), false, null);
        dialog.getSaveButton().addActionListener(e -> {
            Teacher teacher = dialog.getTeacher();
            if (validateTeacher(teacher, false)) {
                if (teacherService.createTeacher(teacher)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm giáo viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadTeachers();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm giáo viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        dialog.setVisible(true);
    }

    private void handleDetail() {
        String teacherId = teacherView.getSelectedTeacherId();
        if (teacherId == null) {
            JOptionPane.showMessageDialog(teacherView, "Vui lòng chọn một giáo viên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Teacher teacher = teacherService.getTeacherById(teacherId);
        if (teacher != null) {
            StringBuilder details = new StringBuilder();
            details.append("Mã GV: ").append(teacher.getTeacherId()).append("\n");
            details.append("Họ tên: ").append(teacher.getName()).append("\n");
            details.append("Ngày sinh: ").append(teacher.getDob() != null ? Helper.formatDate(teacher.getDob()) : "").append("\n");
            details.append("SĐT: ").append(teacher.getPhone() != null ? teacher.getPhone() : "").append("\n");
            details.append("Email: ").append(teacher.getEmail() != null ? teacher.getEmail() : "").append("\n");

            JOptionPane.showMessageDialog(teacherView, details.toString(), "Chi tiết giáo viên", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleEdit() {
        String teacherId = teacherView.getSelectedTeacherId();
        if (teacherId == null) {
            JOptionPane.showMessageDialog(teacherView, "Vui lòng chọn một giáo viên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Teacher teacher = teacherService.getTeacherById(teacherId);
        if (teacher != null) {
            TeacherFormDialog dialog = new TeacherFormDialog((JFrame) SwingUtilities.getWindowAncestor(teacherView), true, teacher);
            dialog.getSaveButton().addActionListener(e -> {
                Teacher updatedTeacher = dialog.getTeacher();
                if (validateTeacher(updatedTeacher, true)) {
                    if (teacherService.updateTeacher(updatedTeacher)) {
                        JOptionPane.showMessageDialog(dialog, "Sửa giáo viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadTeachers();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi sửa giáo viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            dialog.setVisible(true);
        }
    }

    private void handleDelete() {
        String teacherId = teacherView.getSelectedTeacherId();
        if (teacherId == null) {
            JOptionPane.showMessageDialog(teacherView, "Vui lòng chọn một giáo viên!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(teacherView,
                "Bạn có chắc chắn muốn xóa giáo viên này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (teacherService.deleteTeacher(teacherId)) {
                JOptionPane.showMessageDialog(teacherView, "Xóa giáo viên thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadTeachers();
            } else {
                JOptionPane.showMessageDialog(teacherView, "Lỗi khi xóa giáo viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateTeacher(Teacher teacher, boolean isEdit) {
        if (teacher.getTeacherId() == null || teacher.getTeacherId().trim().isEmpty()) {
            JOptionPane.showMessageDialog(teacherView, "Mã giáo viên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            JOptionPane.showMessageDialog(teacherView, "Họ tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (teacher.getEmail() != null && !teacher.getEmail().trim().isEmpty()) {
            if (!Helper.isValidEmail(teacher.getEmail())) {
                JOptionPane.showMessageDialog(teacherView, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }
}

