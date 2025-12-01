package main.java.model;

import java.time.LocalDate;

public class Teacher {
    private String teacherId;
    private String name;
    private LocalDate dob;
    private String phone;
    private String email;

    public Teacher() {
    }

    public Teacher(String teacherId, String name, LocalDate dob, String phone, String email) {
        this.teacherId = teacherId;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.email = email;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId='" + teacherId + '\'' +
                ", name='" + name + '\'' +
                ", dob=" + dob +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

