package com.studentmanagement.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Subject - Môn học
 */
public class Subject {
    // Encapsulation: private fields
    private String subjectId;
    private String name;
    private int totalSessions;
    private List<Homeroom> homerooms; // Quan hệ 1-n với Homeroom
    
    // Constructor
    public Subject() {
        this.homerooms = new ArrayList<>();
    }
    
    public Subject(String subjectId, String name, int totalSessions) {
        this.subjectId = subjectId;
        this.name = name;
        this.totalSessions = totalSessions;
        this.homerooms = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getSubjectId() {
        return subjectId;
    }
    
    public void setSubjectId(String subjectId) {
        if (subjectId == null || subjectId.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject ID cannot be null or empty");
        }
        this.subjectId = subjectId.trim().toUpperCase();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject name cannot be null or empty");
        }
        this.name = name.trim();
    }
    
    public int getTotalSessions() {
        return totalSessions;
    }
    
    public void setTotalSessions(int totalSessions) {
        if (totalSessions < 0) {
            throw new IllegalArgumentException("Total sessions cannot be negative");
        }
        this.totalSessions = totalSessions;
    }
    
    public List<Homeroom> getHomerooms() {
        return homerooms;
    }
    
    public void setHomerooms(List<Homeroom> homerooms) {
        this.homerooms = homerooms != null ? homerooms : new ArrayList<>();
    }
    
    /**
     * Thêm lớp học cho môn học
     * @param homeroom đối tượng homeroom
     */
    public void addHomeroom(Homeroom homeroom) {
        if (homeroom != null && !homerooms.contains(homeroom)) {
            homerooms.add(homeroom);
            homeroom.setSubject(this);
        }
    }
    
    @Override
    public String toString() {
        return "Subject{" +
                "subjectId='" + subjectId + '\'' +
                ", name='" + name + '\'' +
                ", totalSessions=" + totalSessions +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subject subject = (Subject) obj;
        return subjectId != null ? subjectId.equals(subject.subjectId) : subject.subjectId == null;
    }
    
    @Override
    public int hashCode() {
        return subjectId != null ? subjectId.hashCode() : 0;
    }
}

