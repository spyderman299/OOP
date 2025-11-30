package com.studentmanagement.model;

import java.time.LocalDate;

/**
 * Abstract class Person - Thể hiện Abstraction và Encapsulation
 * Lớp trừu tượng cho các đối tượng Person (Student, Teacher)
 */
public abstract class Person {
    // Encapsulation: private fields
    private String name;
    private LocalDate dob;
    private String phone;
    
    // Constructors
    public Person() {
    }
    
    public Person(String name, LocalDate dob, String phone) {
        this.name = name;
        this.dob = dob;
        this.phone = phone;
    }
    
    // Getters and Setters - Encapsulation
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim();
    }
    
    public LocalDate getDob() {
        return dob;
    }
    
    public void setDob(LocalDate dob) {
        if (dob != null && dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
        this.dob = dob;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        if (phone != null && !phone.matches("^[0-9]{10,11}$")) {
            throw new IllegalArgumentException("Invalid phone number format");
        }
        this.phone = phone;
    }
    
    // Abstract method - Abstraction
    /**
     * Lấy thông tin của person (sẽ được override bởi subclass)
     * @return String thông tin của person
     */
    public abstract String getInfo();
    
    /**
     * Tính tuổi từ ngày sinh
     * @return tuổi hiện tại
     */
    public int getAge() {
        if (dob == null) {
            return 0;
        }
        return LocalDate.now().getYear() - dob.getYear();
    }
    
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", dob=" + dob +
                ", phone='" + phone + '\'' +
                '}';
    }
}

