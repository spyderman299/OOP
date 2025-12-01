package main.java.model;

import java.time.LocalDate;

public class Student {
    private String studentId;
    private String name;
    private LocalDate dob;
    private String phone;

    public Student() {
    }

    public Student(String studentId, String name, LocalDate dob, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        if (dob == null) {
            return 0;
        }
        return LocalDate.now().getYear() - dob.getYear();
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", phone='" + phone + '\'' +
                '}';
    }
}

