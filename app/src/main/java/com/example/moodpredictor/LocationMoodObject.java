package com.example.moodpredictor;

public class LocationMoodObject {

    int time;
    int mood;

    public LocationMoodObject(int time, int mood) {
        this.time = time;
        this.mood = mood;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    @Override
    public String toString() {
        return "LocationMoodObject{" +
                "time=" + time +
                ", mood=" + mood +
                '}';
    }
}
