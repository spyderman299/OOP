package main.java.service.impl;

import main.java.model.Subject;
import main.java.repository.SubjectRepository;
import main.java.service.SubjectService;

import java.util.List;

public class SubjectServiceImpl implements SubjectService {
    private static SubjectServiceImpl instance;
    private SubjectRepository subjectRepository;

    private SubjectServiceImpl() {
        this.subjectRepository = new SubjectRepository();
    }

    public static SubjectServiceImpl getInstance() {
        if (instance == null) {
            instance = new SubjectServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject getSubjectById(String subjectId) {
        if (subjectId == null || subjectId.trim().isEmpty()) {
            return null;
        }
        return subjectRepository.findById(subjectId);
    }

    @Override
    public boolean createSubject(Subject subject) {
        if (subject == null || subject.getSubjectId() == null || subject.getSubjectId().trim().isEmpty()) {
            return false;
        }

        // Check if subject already exists
        if (subjectRepository.findById(subject.getSubjectId()) != null) {
            System.err.println("Subject ID already exists: " + subject.getSubjectId());
            return false;
        }

        // Validate totalSessions >= 0
        if (subject.getTotalSessions() < 0) {
            System.err.println("Total sessions must be >= 0");
            return false;
        }

        return subjectRepository.create(subject);
    }

    @Override
    public boolean updateSubject(Subject subject) {
        if (subject == null || subject.getSubjectId() == null || subject.getSubjectId().trim().isEmpty()) {
            return false;
        }

        // Validate totalSessions >= 0
        if (subject.getTotalSessions() < 0) {
            System.err.println("Total sessions must be >= 0");
            return false;
        }

        return subjectRepository.update(subject);
    }

    @Override
    public boolean deleteSubject(String subjectId) {
        if (subjectId == null || subjectId.trim().isEmpty()) {
            return false;
        }
        return subjectRepository.delete(subjectId);
    }
}

