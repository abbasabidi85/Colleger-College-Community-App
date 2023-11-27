package com.abs.colleger.app.admin.timetable;

public class Lecture {
    private String hourOfDay;
    private String minute;
    private String subject;
    private String teacher;
    private String roomNumber;
    private String key;

    public Lecture() {
    }

    public Lecture(String hourOfDay, String minute, String subject, String teacher, String roomNumber, String key) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
        this.subject = subject;
        this.teacher = teacher;
        this.roomNumber = roomNumber;
        this.key=key;
    }

    public String getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(String hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
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
