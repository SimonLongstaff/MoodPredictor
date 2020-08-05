package com.example.moodpredictor;

public class ShakeMoodObject {

    private int Shakes;
    private int mood;
    private String Date;
    private int uid;

    public ShakeMoodObject(int shakes, int mood, String date, int uid) {
        Shakes = shakes;
        this.mood = mood;
        Date = date;
        this.uid = uid;
    }

    public int getShakes() {
        return Shakes;
    }

    public void setShakes(int shakes) {
        Shakes = shakes;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
