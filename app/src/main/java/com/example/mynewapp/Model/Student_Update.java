package com.example.mynewapp.Model;

import java.io.Serializable;

public class Student_Update implements Serializable {
    public String name;
    public String email;
    public String rollno;

    public Student_Update(String rollno, String name, String email) {
        this.rollno = rollno;
        this.name = name;
        this.email = email;
    }

    public Student_Update() {
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
