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

    //Returns the cross product of two arrays
    public static float[] cross(float[] arrayA, float[] arrayB){
        float[] retArray = new float[3];
        retArray[0] = arrayA[1] * arrayB[2] - arrayA[2] * arrayB[1];
        retArray[1] = arrayA[2] * arrayB[0] - arrayA[0] * arrayB[2];
        retArray[2] = arrayA[0] * arrayB[1] - arrayA[1] * arrayB[0];
        return retArray;
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

    //Returns a normalized array
    public static float[] normalize(float[] a){
        float[] retval = new float[a.length];
        float norm = norm(a);
        for (int i = 0; i<a.length; i++){
            retval[i] = a[i]/norm;
        }
        return retval;
    }







}
