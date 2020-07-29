package com.example.moodpredictor;

public class onTimeMoodObject {

    private int onTime;
    private String date;
    private int mood;

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public onTimeMoodObject(int onTime, String date, int mood) {
        this.onTime = onTime;
        this.date = date;
        this.mood = mood;
    }

    public int getOnTime() {
        return onTime;
    }

    public void setOnTime(int onTime) {
        this.onTime = onTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "onTimeMoodObject{" +
                "onTime=" + onTime +
                ", date='" + date + '\'' +
                ", mood=" + mood +
                '}';
    }
}
