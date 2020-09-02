package com.example.moodpredictor;

public class SensorFilter {

    private SensorFilter(){
    }

    //Returns the sum of an array of float values
    public static float sum(float[] array){
        float retval = 0;
        for (float v : array) {
            retval += v;
        }
        return retval;
    }

    //Returns the normal of an array
    public static float norm(float[] array){
        float retVal = 0;
        for (float v : array) {
            retVal += v * v;
        }
        return (float) Math.sqrt(retVal);
    }

    public static float dot(float[] a, float[] b){
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }







}
