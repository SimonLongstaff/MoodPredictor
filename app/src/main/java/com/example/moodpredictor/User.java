package com.example.moodpredictor;

public class User {

    private int UID;
    private String Name;

    public User(int UID, String name){
        this.UID = UID;
        this.Name = name;
    }

    public User(String name){
        this.Name = name;

    }

    @Override
    public String toString(){
        return "UID :" + UID +
                "\nName: " + Name;
    }

    public int getUID() {
        return UID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
