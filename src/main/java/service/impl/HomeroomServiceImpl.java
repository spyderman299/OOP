package main.java.service.impl;

import main.java.model.Homeroom;
import main.java.repository.HomeroomRepository;
import main.java.service.HomeroomService;

import java.util.List;

public class HomeroomServiceImpl implements HomeroomService {
    private static HomeroomServiceImpl instance;
    private HomeroomRepository homeroomRepository;

    private HomeroomServiceImpl() {
        this.homeroomRepository = new HomeroomRepository();
    }

    public static HomeroomServiceImpl getInstance() {
        if (instance == null) {
            instance = new HomeroomServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Homeroom> getAllHomerooms() {
        return homeroomRepository.findAll();
    }

    @Override
    public Homeroom getHomeroomById(String classId) {
        if (classId == null || classId.trim().isEmpty()) {
            return null;
        }
        return homeroomRepository.findById(classId);
    }

    @Override
    public List<Homeroom> getHomeroomsBySubjectId(String subjectId) {
        if (subjectId == null || subjectId.trim().isEmpty()) {
            return getAllHomerooms();
        }
        return homeroomRepository.findBySubjectId(subjectId);
    }

    @Override
    public List<Homeroom> getHomeroomsByTeacherId(String teacherId) {
        if (teacherId == null || teacherId.trim().isEmpty()) {
            return getAllHomerooms();
        }
        return homeroomRepository.findByTeacherId(teacherId);
    }

    @Override
    public boolean createHomeroom(Homeroom homeroom) {
        if (homeroom == null || homeroom.getClassId() == null || homeroom.getClassId().trim().isEmpty()) {
            return false;
        }

        // Check if homeroom already exists
        if (homeroomRepository.findById(homeroom.getClassId()) != null) {
            System.err.println("Class ID already exists: " + homeroom.getClassId());
            return false;
        }

        // Validate dates: endDate >= startDate
        if (homeroom.getStartDate() != null && homeroom.getEndDate() != null) {
            if (homeroom.getEndDate().isBefore(homeroom.getStartDate())) {
                System.err.println("End date must be after or equal to start date");
                return false;
            }
        }

        return homeroomRepository.create(homeroom);
    }

    @Override
    public boolean updateHomeroom(Homeroom homeroom) {
        if (homeroom == null || homeroom.getClassId() == null || homeroom.getClassId().trim().isEmpty()) {
            return false;
        }

        // Validate dates: endDate >= startDate
        if (homeroom.getStartDate() != null && homeroom.getEndDate() != null) {
            if (homeroom.getEndDate().isBefore(homeroom.getStartDate())) {
                System.err.println("End date must be after or equal to start date");
                return false;
            }
        }

        return homeroomRepository.update(homeroom);
    }

    @Override
    public boolean deleteHomeroom(String classId) {
        if (classId == null || classId.trim().isEmpty()) {
            return false;
        }
        return homeroomRepository.delete(classId);
    }
}

