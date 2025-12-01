package main.java.service;

import main.java.model.Homeroom;

import java.util.List;

public interface HomeroomService {
    List<Homeroom> getAllHomerooms();
    Homeroom getHomeroomById(String classId);
    List<Homeroom> getHomeroomsBySubjectId(String subjectId);
    List<Homeroom> getHomeroomsByTeacherId(String teacherId);
    boolean createHomeroom(Homeroom homeroom);
    boolean updateHomeroom(Homeroom homeroom);
    boolean deleteHomeroom(String classId);
}

