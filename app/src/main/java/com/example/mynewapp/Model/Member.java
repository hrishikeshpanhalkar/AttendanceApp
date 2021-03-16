package com.example.mynewapp.Model;


public class Member {
    private String Name;
    private Integer Duration;
    private Integer Seat;
    private String Sem;

    public Member() {
    }

    public String getSem() {
        return Sem;
    }

    public void setSem(String sem) {
        Sem = sem;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getDuration() {
        return Duration;
    }

    public void setDuration(Integer duration) {
        Duration = duration;
    }

    public Integer getSeat() {
        return Seat;
    }

    public void setSeat(Integer seat) {
        Seat = seat;
    }

}
