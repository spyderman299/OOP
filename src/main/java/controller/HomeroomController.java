package main.java.controller;

import main.java.service.ServiceFactory;
import main.java.service.*;
import main.java.model.Homeroom;
import main.java.model.User;
import main.java.view.HomeroomView;
import main.java.component.HomeroomFormDialog;
import main.java.utility.Helper;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeroomController {
    private HomeroomView homeroomView;
    private HomeroomService homeroomService;
    private SubjectService subjectService;
    private TeacherService teacherService;
    private UserService userService;
    private boolean isTeacher;
    private String currentTeacherId;

    public HomeroomController(HomeroomView homeroomView) {
        this.homeroomView = homeroomView;
        this.homeroomService = ServiceFactory.getHomeroomService();
        this.subjectService = ServiceFactory.getSubjectService();
        this.teacherService = ServiceFactory.getTeacherService();
        this.userService = ServiceFactory.getUserService();
        
        // Check if current user is TEACHER
        User currentUser = userService.getCurrentUser();
        this.isTeacher = currentUser != null && "TEACHER".equals(currentUser.getRole());
        this.currentTeacherId = currentUser != null ? currentUser.getTeacherId() : null;
        
        setupEventHandlers();
        loadHomerooms();
    }

    private void setupEventHandlers() {
        // Only ADMIN can add/delete homerooms
        if (isTeacher) {
            homeroomView.getAddButton().setVisible(false);
            homeroomView.getDeleteButton().setVisible(false);
        } else {
            homeroomView.getAddButton().addActionListener(e -> handleAdd());
            homeroomView.getDeleteButton().addActionListener(e -> handleDelete());
        }
        homeroomView.getDetailButton().addActionListener(e -> handleDetail());
        homeroomView.getEditButton().addActionListener(e -> handleEdit());
    }

    private void loadHomerooms() {
        try {
            List<Homeroom> homerooms;
            
            // If TEACHER, only load homerooms they teach
            if (isTeacher && currentTeacherId != null) {
                homerooms = homeroomService.getHomeroomsByTeacherId(currentTeacherId);
            } else {
                homerooms = homeroomService.getAllHomerooms();
            }
            
            Map<String, String> subjectNames = new HashMap<>();
            subjectService.getAllSubjects().forEach(s -> subjectNames.put(s.getSubjectId(), s.getName()));
            
            Map<String, String> teacherNames = new HashMap<>();
            teacherService.getAllTeachers().forEach(t -> teacherNames.put(t.getTeacherId(), t.getName()));
            
            homeroomView.loadHomerooms(homerooms, subjectNames, teacherNames);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(homeroomView,
                    "Lỗi khi tải danh sách lớp học: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleAdd() {
        HomeroomFormDialog dialog = new HomeroomFormDialog(
                (JFrame) SwingUtilities.getWindowAncestor(homeroomView),
                false,
                null,
                subjectService.getAllSubjects(),
                teacherService.getAllTeachers());
        dialog.getSaveButton().addActionListener(e -> {
            Homeroom homeroom = dialog.getHomeroom();
            if (validateHomeroom(homeroom, false)) {
                if (homeroomService.createHomeroom(homeroom)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm lớp học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadHomerooms();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm lớp học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        dialog.setVisible(true);
    }

    private void handleDetail() {
        String classId = homeroomView.getSelectedClassId();
        if (classId == null) {
            JOptionPane.showMessageDialog(homeroomView, "Vui lòng chọn một lớp học!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Homeroom homeroom = homeroomService.getHomeroomById(classId);
        if (homeroom != null) {
            StringBuilder details = new StringBuilder();
            details.append("Mã lớp: ").append(homeroom.getClassId()).append("\n");
            details.append("Môn học: ").append(homeroom.getSubjectId()).append("\n");
            details.append("Giáo viên: ").append(homeroom.getTeacherId()).append("\n");
            details.append("Ngày bắt đầu: ").append(homeroom.getStartDate() != null ? Helper.formatDate(homeroom.getStartDate()) : "").append("\n");
            details.append("Ngày kết thúc: ").append(homeroom.getEndDate() != null ? Helper.formatDate(homeroom.getEndDate()) : "").append("\n");
            details.append("Trạng thái: ").append(homeroom.getStatus()).append("\n");

            JOptionPane.showMessageDialog(homeroomView, details.toString(), "Chi tiết lớp học", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleEdit() {
        String classId = homeroomView.getSelectedClassId();
        if (classId == null) {
            JOptionPane.showMessageDialog(homeroomView, "Vui lòng chọn một lớp học!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Homeroom homeroom = homeroomService.getHomeroomById(classId);
        if (homeroom != null) {
            HomeroomFormDialog dialog = new HomeroomFormDialog(
                    (JFrame) SwingUtilities.getWindowAncestor(homeroomView),
                    true,
                    homeroom,
                    subjectService.getAllSubjects(),
                    teacherService.getAllTeachers());
            dialog.getSaveButton().addActionListener(e -> {
                Homeroom updatedHomeroom = dialog.getHomeroom();
                if (validateHomeroom(updatedHomeroom, true)) {
                    if (homeroomService.updateHomeroom(updatedHomeroom)) {
                        JOptionPane.showMessageDialog(dialog, "Sửa lớp học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadHomerooms();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Lỗi khi sửa lớp học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            dialog.setVisible(true);
        }
    }

    private void handleDelete() {
        String classId = homeroomView.getSelectedClassId();
        if (classId == null) {
            JOptionPane.showMessageDialog(homeroomView, "Vui lòng chọn một lớp học!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(homeroomView,
                "Bạn có chắc chắn muốn xóa lớp học này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (homeroomService.deleteHomeroom(classId)) {
                JOptionPane.showMessageDialog(homeroomView, "Xóa lớp học thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadHomerooms();
            } else {
                JOptionPane.showMessageDialog(homeroomView, "Lỗi khi xóa lớp học!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateHomeroom(Homeroom homeroom, boolean isEdit) {
        if (homeroom.getClassId() == null || homeroom.getClassId().trim().isEmpty()) {
            JOptionPane.showMessageDialog(homeroomView, "Mã lớp học không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (homeroom.getStartDate() != null && homeroom.getEndDate() != null) {
            if (homeroom.getEndDate().isBefore(homeroom.getStartDate())) {
                JOptionPane.showMessageDialog(homeroomView, "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }
}

