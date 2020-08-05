package com.example.moodpredictor;

import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;

public class BayesHelper {

    //MultiBayes

    public static int predictMoodShakeOnTime(int[][] stepsMatrix, int[][] shakeMatrix, int[][] onTimeMatrix, int shakes, int steps, int onTime, LocMoodCalObject locMoodCalObject ){
        double tVS = matrixTheta(stepsMatrix,shakeMatrix,onTimeMatrix,shakes,steps,onTime,0,locMoodCalObject.getLocMoodVs());
        double tS = matrixTheta(stepsMatrix,shakeMatrix,onTimeMatrix,shakes,steps,onTime,1,locMoodCalObject.getLocMoodS());
        double tN = matrixTheta(stepsMatrix,shakeMatrix,onTimeMatrix,shakes,steps,onTime,2,locMoodCalObject.getLocMoodN());
        double tH = matrixTheta(stepsMatrix,shakeMatrix,onTimeMatrix,shakes,steps,onTime,3,locMoodCalObject.getLocMoodH());
        double tVH = matrixTheta(stepsMatrix,shakeMatrix,onTimeMatrix,shakes,steps,onTime,4,locMoodCalObject.getLocMoodVh());

        double totalVSCombined = totalMatrixRow(stepsMatrix,10,0) + totalMatrixRow(shakeMatrix, 6,0) + totalMatrixRow(onTimeMatrix,16,0) + locMoodCalObject.getRowVs();
        double totalSCombined = totalMatrixRow(stepsMatrix,10,1) + totalMatrixRow(shakeMatrix, 6,1) + totalMatrixRow(onTimeMatrix,16,1) + locMoodCalObject.getRowS();
        double totalNCombined = totalMatrixRow(stepsMatrix,10,2) + totalMatrixRow(shakeMatrix, 6,2) + totalMatrixRow(onTimeMatrix,16,2)+ locMoodCalObject.getRowN();
        double totalHCombined = totalMatrixRow(stepsMatrix,10,3) + totalMatrixRow(shakeMatrix, 6,3) + totalMatrixRow(onTimeMatrix,16,3)+ locMoodCalObject.getRowH();
        double totalVHCombined = totalMatrixRow(stepsMatrix,10,4) + totalMatrixRow(shakeMatrix, 6,4) + totalMatrixRow(onTimeMatrix,16,4) + locMoodCalObject.getRowVh();

        double totalVsShake = totalMatrixRow(shakeMatrix,6,0);
        double totalSShake = totalMatrixRow(shakeMatrix,6,1);
        double totalNShake = totalMatrixRow(shakeMatrix,6,2);
        double totalHShake = totalMatrixRow(shakeMatrix,6,3);
        double totalVhShake = totalMatrixRow(shakeMatrix,6,4);

        double totalVsSteps = totalMatrixRow(stepsMatrix,10,0);
        double totalSSteps = totalMatrixRow(stepsMatrix,10,1);
        double totalNSteps = totalMatrixRow(stepsMatrix,10,2);
        double totalHSteps = totalMatrixRow(stepsMatrix,10,3);
        double totalVhSteps = totalMatrixRow(stepsMatrix,10,4);

        double totalVsonTime = totalMatrixRow(onTimeMatrix,16,0);
        double totalSonTime = totalMatrixRow(onTimeMatrix,16,1);
        double totalNonTime = totalMatrixRow(onTimeMatrix,16,2);
        double totalHonTime = totalMatrixRow(onTimeMatrix,16,3);
        double totalVhonTime = totalMatrixRow(onTimeMatrix,16,4);

        double theta = tVS + tS + tN + tH + tVH;
        double totalCombined = totalMatrix(stepsMatrix,10,5) + totalMatrix(shakeMatrix, 6, 5) + totalMatrix(onTimeMatrix,16,5) + locMoodCalObject.getTotal();

        double predictVS = (((shakeMatrix[0][shakeBucket(shakes)]/totalVsShake) * (stepsMatrix[0][stepBucket(steps)]/totalVsSteps) * (onTimeMatrix[0][onTimeBucket(onTime)]/totalVsonTime) * locMoodCalObject.getLocMoodVs())/theta)*(totalVSCombined/totalCombined);
        double predictS = (((shakeMatrix[1][shakeBucket(shakes)]/totalSShake) * (stepsMatrix[1][stepBucket(steps)]/totalSSteps) * (onTimeMatrix[1][onTimeBucket(onTime)]/totalSonTime) * locMoodCalObject.getLocMoodS())/theta)*(totalSCombined/totalCombined);
        double predictN = (((shakeMatrix[2][shakeBucket(shakes)]/totalNShake) * (stepsMatrix[2][stepBucket(steps)]/totalNSteps) * (onTimeMatrix[2][onTimeBucket(onTime)]/totalNonTime) * locMoodCalObject.getLocMoodN())/theta)*(totalNCombined/totalCombined);
        double predictH = (((shakeMatrix[3][shakeBucket(shakes)]/totalHShake) * (stepsMatrix[3][stepBucket(steps)]/totalHSteps) * (onTimeMatrix[3][onTimeBucket(onTime)]/totalHonTime) * locMoodCalObject.getLocMoodH())/theta)*(totalHCombined/totalCombined);
        double predictVH = (((shakeMatrix[4][shakeBucket(shakes)]/totalVhShake) * (stepsMatrix[4][stepBucket(steps)]/totalVhSteps) * (onTimeMatrix[4][onTimeBucket(onTime)]/totalVhonTime) * locMoodCalObject.getLocMoodVh())/theta)*(totalVHCombined/totalCombined);

        double[] predict = new double[]{predictVS, predictS, predictN, predictH, predictVH};
        System.out.println("Final predict " + Arrays.toString(predict));

        int highest = 0;
        double currentTop = 0;
        for (int i = 0; i < predict.length; i++) {
            if (predict[i] > currentTop) {
                highest = i + 1;
                currentTop = predict[i];
            }
            System.out.println("Current Top: " + highest);

        }

        System.out.println("Highest percentage mood: " + highest);
        return highest;

    }


    public static double matrixTheta(int[][] stepsMatrix, int[][] shakeMatrix, int[][] onTimeMatrix, int shake, int steps, int onTime, int mood, double locMood){

        double totalMoodShake = totalMatrixRow(shakeMatrix,6,mood);
        double totalMoodSteps = totalMatrixRow(stepsMatrix,10,mood);
        double totalOnTime = totalMatrixRow(onTimeMatrix,16,mood);

        double totalMoodCombined = totalMoodShake + totalMoodSteps + totalOnTime;
        double totalCombined = totalMatrix(stepsMatrix,10,5) + totalMatrix(shakeMatrix, 6, 5) + totalMatrix(onTimeMatrix,16,5);

        return ((shakeMatrix[mood][shakeBucket(shake)]/totalMoodShake) * (stepsMatrix[mood][stepBucket(steps)]/totalMoodSteps) * (onTimeMatrix[mood][onTimeBucket(onTime)]/totalOnTime) * (locMood)) * (totalMoodCombined/totalCombined);
    }





    public static double locMoodCal(int[][] locMatrix, int mood, int locTime){
        double totalMood = totalMatrixRow(locMatrix,10,mood);
        int locBucket = dayBucket(locTime);
        int value = locMatrix[mood][locBucket];
        return value/totalMood;
    }

    //Returns the total sum of values in a step matrix
    public static int totalMatrix(int[][] matrix, int columns, int row) {
        int total = 0;

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < row; j++) {
                total = total + matrix[j][i];
            }
        }
        System.out.println("Total Matrix: " + total);
        return total;
    }

    //Returns to the total value of a row in a matrix
    public static int totalMatrixRow(int[][] matrix, int columns, int row) {

        int total = 0;

        for (int i = 0; i < columns; i++) {
            total = total + matrix[row][i];
        }
        System.out.println("Total Matrix Row: " + total);
        return total;
    }

    //Return to the total value
    public static int totalMatrixColumn(int[][] matrix, int column, int row) {

        int total = 0;

        for (int i = 0; i < row; i++) {
            total = total + matrix[i][column];
        }
        System.out.println("Total Matrix Column: " + total);
        return total;
    }

//Puts steps into bucket values for processing
    public static int stepBucket(int steps) {

        int stepbucket = 0;


        if (steps > 1000 && steps <= 2000) {
            stepbucket = 1;
        }
        if (steps > 2000 && steps <= 3000) {
            stepbucket = 2;
        }
        if (steps > 3000 && steps <= 4000) {
            stepbucket = 3;
        }
        if (steps > 4000 && steps <= 5000) {
            stepbucket = 4;
        }
        if (steps > 5000 && steps <= 6000) {
            stepbucket = 5;
        }
        if (steps > 6000 && steps <= 7000) {
            stepbucket = 6;
        }
        if (steps > 7000 && steps <= 8000) {
            stepbucket = 7;
        }
        if (steps > 8000 && steps <= 9000) {
            stepbucket = 8;
        }
        if (steps > 9000) {
            stepbucket = 9;
        }

        return stepbucket;
    }

    //Puts the Shakes into a bucket
    public static int shakeBucket(int shakes){

        int shakebucket = 0;

        if (shakes>2000 && shakes<=4000){
            shakebucket =1;
        }
        if (shakes>4000 && shakes<=6000){
            shakebucket =2;
        }
        if (shakes>6000 && shakes<=8000){
            shakebucket =3;
        }
        if (shakes>8000 && shakes<=10000){
            shakebucket =4;
        }
        if (shakes>10000){
            shakebucket =5;
        }

        return shakebucket;
    }

    public static int onTimeBucket(int onTime){

        int onTimebucket = 0;

        if (onTime>=3600 && onTime<7200)
            onTimebucket =1;
        if (onTime>=7200 && onTime<10800)
            onTimebucket =2;
        if (onTime>=10800 && onTime<14400)
            onTimebucket =3;
        if (onTime>=14400 && onTime<18000)
            onTimebucket =4;
        if (onTime>=18000 && onTime<21600)
            onTimebucket =5;
        if (onTime>=21600 && onTime<25200)
            onTimebucket =6;
        if (onTime>=25200 && onTime<28800)
            onTimebucket =7;
        if (onTime>=28800 && onTime<32400)
            onTimebucket =8;
        if (onTime>=32400 && onTime<36000)
            onTimebucket =9;
        if (onTime>=36000 && onTime<39600)
            onTimebucket =10;
        if (onTime>=39600 && onTime<43200)
            onTimebucket =11;
        if (onTime>=43200 && onTime<46800)
            onTimebucket =12;
        if (onTime>=46800 && onTime<50400)
            onTimebucket =13;
        if (onTime>=50400 && onTime<54000)
            onTimebucket =14;
        if (onTime>=54000 && onTime<57600)
            onTimebucket =15;

        return onTimebucket;
    }

    public static int dayBucket(int visit){

        int dayBucket = 0;

        if (visit>=8640 && visit<17280){
            dayBucket = 1;
        }
        if (visit>=17280 && visit<25920){
            dayBucket = 2;
        }
        if (visit>=25920 && visit<34560){
            dayBucket = 3;
        }
        if (visit>=34560 && visit<43200){
            dayBucket = 4;
        }
        if (visit>=43200 && visit<51840){
            dayBucket = 5;
        }
        if (visit>=51840 && visit<60480){
            dayBucket = 6;
        }
        if (visit>=60480 && visit<69120){
            dayBucket = 7;
        }
        if (visit>=69120 && visit<77760){
            dayBucket = 8;
        }
        if (visit>=77760){
            dayBucket = 9;
        }
        return dayBucket;
    }
}
