package com.example.moodpredictor;

public class StepMoodObject {

    private int steps;
    private int mood;
    private String date;

    public StepMoodObject(int steps, int mood, String date) {
        this.steps = steps;
        this.mood = mood;
        this.date = date;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "StepMoodObject{" +
                "steps=" + steps +
                ", mood=" + mood +
                ", date='" + date + '\'' +
                '}';
    }
}
