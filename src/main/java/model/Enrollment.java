package main.java.model;

import java.math.BigDecimal;

public class Enrollment {
    private String classId;
    private String studentId;
    private BigDecimal attendance;
    private BigDecimal homework;
    private BigDecimal midTerm;
    private BigDecimal endTerm;
    private BigDecimal finalScore;
    private String result;

    public Enrollment() {
    }

    public Enrollment(String classId, String studentId, BigDecimal attendance, BigDecimal homework, 
                     BigDecimal midTerm, BigDecimal endTerm) {
        this.classId = classId;
        this.studentId = studentId;
        this.attendance = attendance;
        this.homework = homework;
        this.midTerm = midTerm;
        this.endTerm = endTerm;
        calculateFinalScore();
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public BigDecimal getAttendance() {
        return attendance;
    }

    public void setAttendance(BigDecimal attendance) {
        this.attendance = attendance;
        calculateFinalScore();
    }

    public BigDecimal getHomework() {
        return homework;
    }

    public void setHomework(BigDecimal homework) {
        this.homework = homework;
        calculateFinalScore();
    }

    public BigDecimal getMidTerm() {
        return midTerm;
    }

    public void setMidTerm(BigDecimal midTerm) {
        this.midTerm = midTerm;
        calculateFinalScore();
    }

    public BigDecimal getEndTerm() {
        return endTerm;
    }

    public void setEndTerm(BigDecimal endTerm) {
        this.endTerm = endTerm;
        calculateFinalScore();
    }

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        this.finalScore = finalScore;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private void calculateFinalScore() {
        if (attendance != null && homework != null && midTerm != null && endTerm != null) {
            // Final Score = 0.1×attendance + 0.2×homework + 0.3×midTerm + 0.4×endTerm
            double score = 0.1 * attendance.doubleValue() + 
                          0.2 * homework.doubleValue() + 
                          0.3 * midTerm.doubleValue() + 
                          0.4 * endTerm.doubleValue();
            this.finalScore = BigDecimal.valueOf(Math.round(score * 100.0) / 100.0);
            
            // Determine result: Passed (>=5.0) / Failed (<5.0)
            this.result = (score >= 5.0) ? "Passed" : "Failed";
        }
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "classId='" + classId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", attendance=" + attendance +
                ", homework=" + homework +
                ", midTerm=" + midTerm +
                ", endTerm=" + endTerm +
                ", finalScore=" + finalScore +
                ", result='" + result + '\'' +
                '}';
    }
}

