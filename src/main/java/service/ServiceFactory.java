package main.java.service;

import main.java.service.impl.*;

public class ServiceFactory {
    private static UserService userService;
    private static StudentService studentService;
    private static TeacherService teacherService;
    private static SubjectService subjectService;
    private static HomeroomService homeroomService;
    private static EnrollmentService enrollmentService;

    public static void init() {
        userService = UserServiceImpl.getInstance();
        studentService = StudentServiceImpl.getInstance();
        teacherService = TeacherServiceImpl.getInstance();
        subjectService = SubjectServiceImpl.getInstance();
        homeroomService = HomeroomServiceImpl.getInstance();
        enrollmentService = EnrollmentServiceImpl.getInstance();
    }

    public static UserService getUserService() {
        return userService;
    }

    public static StudentService getStudentService() {
        return studentService;
    }

    public static TeacherService getTeacherService() {
        return teacherService;
    }

    public static SubjectService getSubjectService() {
        return subjectService;
    }

    public static HomeroomService getHomeroomService() {
        return homeroomService;
    }

    public static EnrollmentService getEnrollmentService() {
        return enrollmentService;
    }
}

