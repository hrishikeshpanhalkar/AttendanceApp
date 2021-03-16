package com.example.mynewapp.Model;

public class Subject {
    String Code,Name,Course,Semester;

    public Subject(String code, String name, String course, String semester) {
        Code=code;
        Name = name;
        Course = course;
        Semester = semester;
    }

    public Subject() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getSemester() {
        return Semester;
    }

    public void setSemester(String semester) {
        Semester = semester;
    }
}
