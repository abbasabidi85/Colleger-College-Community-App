package com.abs.colleger.app.student.timetable;

public class UserLecture {
    private String startTime;
    private String subject;
    private String teacher;
    private String roomNumber;

    public UserLecture() {
    }

    public UserLecture(String startTime, String subject, String teacher, String roomNumber) {
        this.startTime = startTime;
        this.subject = subject;
        this.teacher = teacher;
        this.roomNumber = roomNumber;
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
}
