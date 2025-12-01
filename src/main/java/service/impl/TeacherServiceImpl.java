package main.java.service.impl;

import main.java.model.Teacher;
import main.java.repository.TeacherRepository;
import main.java.service.TeacherService;

import java.util.List;

public class TeacherServiceImpl implements TeacherService {
    private static TeacherServiceImpl instance;
    private TeacherRepository teacherRepository;

    private TeacherServiceImpl() {
        this.teacherRepository = new TeacherRepository();
    }

    public static TeacherServiceImpl getInstance() {
        if (instance == null) {
            instance = new TeacherServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(String teacherId) {
        if (teacherId == null || teacherId.trim().isEmpty()) {
            return null;
        }
        return teacherRepository.findById(teacherId);
    }

    @Override
    public boolean createTeacher(Teacher teacher) {
        if (teacher == null || teacher.getTeacherId() == null || teacher.getTeacherId().trim().isEmpty()) {
            return false;
        }

        // Check if teacher already exists
        if (teacherRepository.findById(teacher.getTeacherId()) != null) {
            System.err.println("Teacher ID already exists: " + teacher.getTeacherId());
            return false;
        }

        // Validate email format
        if (teacher.getEmail() != null && !teacher.getEmail().trim().isEmpty()) {
            if (!isValidEmail(teacher.getEmail())) {
                System.err.println("Invalid email format: " + teacher.getEmail());
                return false;
            }
        }

        return teacherRepository.create(teacher);
    }

    @Override
    public boolean updateTeacher(Teacher teacher) {
        if (teacher == null || teacher.getTeacherId() == null || teacher.getTeacherId().trim().isEmpty()) {
            return false;
        }

        // Validate email format
        if (teacher.getEmail() != null && !teacher.getEmail().trim().isEmpty()) {
            if (!isValidEmail(teacher.getEmail())) {
                System.err.println("Invalid email format: " + teacher.getEmail());
                return false;
            }
        }

        return teacherRepository.update(teacher);
    }

    @Override
    public boolean deleteTeacher(String teacherId) {
        if (teacherId == null || teacherId.trim().isEmpty()) {
            return false;
        }
        return teacherRepository.delete(teacherId);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
}

