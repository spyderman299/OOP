package main.java.model;

public class Subject {
    private String subjectId;
    private String name;
    private int totalSessions;

    public Subject() {
    }

    public Subject(String subjectId, String name, int totalSessions) {
        this.subjectId = subjectId;
        this.name = name;
        this.totalSessions = totalSessions;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSessions() {
        return totalSessions;
    }

    public void setTotalSessions(int totalSessions) {
        this.totalSessions = totalSessions;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectId='" + subjectId + '\'' +
                ", name='" + name + '\'' +
                ", totalSessions=" + totalSessions +
                '}';
    }
}

