package main.java.model;

import java.time.LocalDate;

public class Homeroom {
    private String classId;
    private String subjectId;
    private String teacherId;
    private LocalDate startDate;
    private LocalDate endDate;

    public Homeroom() {
    }

    public Homeroom(String classId, String subjectId, String teacherId, LocalDate startDate, LocalDate endDate) {
        this.classId = classId;
        this.subjectId = subjectId;
        this.teacherId = teacherId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        LocalDate now = LocalDate.now();
        if (now.isBefore(startDate)) {
            return "Chưa bắt đầu";
        } else if (now.isAfter(endDate)) {
            return "Đã kết thúc";
        } else {
            return "Đang diễn ra";
        }
    }

    @Override
    public String toString() {
        return "Homeroom{" +
                "classId='" + classId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

