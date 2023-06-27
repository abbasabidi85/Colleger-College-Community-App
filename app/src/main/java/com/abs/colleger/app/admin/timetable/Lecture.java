package com.abs.colleger.app.admin.timetable;

public class Lecture {
    private String startTime;
    private String subject;
    private String teacher;
    private String roomNumber;
    private String key;

    public Lecture() {
    }

    public Lecture(String startTime, String subject, String teacher, String roomNumber, String key) {
        this.startTime = startTime;
        this.subject = subject;
        this.teacher = teacher;
        this.roomNumber = roomNumber;
        this.key=key;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) { this.key = key; }
}
