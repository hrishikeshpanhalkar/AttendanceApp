package com.example.mynewapp.Model;

public class Student {
    private String sRollno;
    private String sName;
    private String sEmail;
    private String sMobile;
    private String sGender;
    private String sCourse;
    private String sSemister;

    public Student() {
    }

    public String getRollno() {
        return sRollno;
    }

    public void setRollno(String rollno) {
        this.sRollno = rollno;
    }

    public String getName() {
        return sName;
    }

    public void setName(String name) {
        sName = name;
    }

    public String getEmail() {
        return sEmail;
    }

    public void setEmail(String email) {
        sEmail = email;
    }

    public String getMobile() {
        return sMobile;
    }

    public void setMobile(String mobile) {
        this.sMobile = mobile;
    }

    public String getGender() {
        return sGender;
    }

    public void setGender(String gender) {
        this.sGender = gender;
    }

    public String getCourse() {
        return sCourse;
    }

    public void setCourse(String course) {
        sCourse = course;
    }

    public String getSemister() {
        return sSemister;
    }

    public void setSemister(String semister) {
        sSemister = semister;
    }
}
