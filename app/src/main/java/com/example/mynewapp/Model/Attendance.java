package com.example.mynewapp.Model;

import java.util.Date;

public class Attendance {
    private String name,position,attendance,rollno;

    public Attendance() {
    }

    public Attendance(String name, String position, String attendance, String rollno) {
        this.name = name;
        this.position = position;
        this.attendance = attendance;
        this.rollno = rollno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }
}

