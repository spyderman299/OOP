package main.java.service;

import main.java.model.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> getAllSubjects();
    Subject getSubjectById(String subjectId);
    boolean createSubject(Subject subject);
    boolean updateSubject(Subject subject);
    boolean deleteSubject(String subjectId);
}

