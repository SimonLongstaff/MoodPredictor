package com.example.moodpredictor;

import java.util.Arrays;

public class BayesHelper {

    //Takes a matrix of steps and moods as input, compares them to current steps to predict mood.
    public static int predictMoodSteps(int[][] matrix, int steps) {


        int stepbucket = stepBucket(steps);

        double totalVS = totalMatrixRow(matrix, 10, 0);
        double totalS = totalMatrixRow(matrix,10, 1);
        double totalN = totalMatrixRow(matrix,10, 2);
        double totalH = totalMatrixRow(matrix,10, 3);
        double totalVH = totalMatrixRow(matrix,10, 4);

        double stepBucketTotal = totalMatrixColumn(matrix,9, stepbucket);
        double total = totalMatrix(matrix,9,5);


        double predictVS = ((matrix[stepbucket][0] / totalVS) * (totalVS / total)) / (stepBucketTotal / total);
        double predictS = ((matrix[stepbucket][1] / totalS) * (totalS / total)) / (stepBucketTotal / total);
        double predictN = ((matrix[stepbucket][2] / totalN) * (totalN / total)) / (stepBucketTotal / total);
        double predictH = ((matrix[stepbucket][3] / totalH) * (totalH / total)) / (stepBucketTotal / total);
        double predictVH = (((matrix[stepbucket][4] / totalVH) * (totalVH / total)) / (stepBucketTotal / total));

        double[] predict = new double[]{predictVS, predictS, predictN, predictH, predictVH};
        System.out.println(Arrays.toString(predict));

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

    public static int predictMoodShake(int[][] matrix, int shakes){

        int shakeBucket = shakeBucket(shakes);

        double totalVS = totalMatrixRow(matrix, 5, 0);
        double totalS = totalMatrixRow(matrix,5, 1);
        double totalN = totalMatrixRow(matrix,5, 2);
        double totalH = totalMatrixRow(matrix,5, 3);
        double totalVH = totalMatrixRow(matrix,5, 4);

        double shakeBucketTotal = totalMatrixColumn(matrix, shakeBucket,5);
        double total = totalMatrix(matrix,5,5);

        double predictVS = ((matrix[shakeBucket][0] / totalVS) * (totalVS / total)) / (shakeBucketTotal / total);
        double predictS = ((matrix[shakeBucket][1] / totalS) * (totalS / total)) / (shakeBucketTotal / total);
        double predictN = ((matrix[shakeBucket][2] / totalN) * (totalN / total)) / (shakeBucketTotal / total);
        double predictH = ((matrix[shakeBucket][3] / totalH) * (totalH / total)) / (shakeBucketTotal / total);
        double predictVH = (((matrix[shakeBucket][4] / totalVH) * (totalVH / total)) / (shakeBucketTotal / total));

        double[] predict = new double[]{predictVS, predictS, predictN, predictH, predictVH};
        System.out.println(Arrays.toString(predict));


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

    //MultiBayes
    public static int predictMoodShakeStep(int[][] stepsMatrix, int[][] shakeMatrix, int steps, int shakes){

        System.out.print("MoodShake prediction tables");
        System.out.print(Arrays.deepToString(shakeMatrix));
        System.out.print(Arrays.deepToString(stepsMatrix));

        System.out.println("Thetas start");
        double tVS = matrixTheta(stepsMatrix,shakeMatrix,shakes,steps,0);
        double tS = matrixTheta(stepsMatrix,shakeMatrix,shakes,steps,1);
        double tN = matrixTheta(stepsMatrix,shakeMatrix,shakes,steps,2);
        double tH = matrixTheta(stepsMatrix,shakeMatrix,shakes,steps,3);
        double tVH = matrixTheta(stepsMatrix,shakeMatrix,shakes,steps,4);
        System.out.println("Thetas Finish");

        System.out.println("Total column Shake start");
        double totalVsShake = totalMatrixRow(shakeMatrix,6,0);
        double totalSShake = totalMatrixRow(shakeMatrix,6,1);
        double totalNShake = totalMatrixRow(shakeMatrix,6,2);
        double totalHShake = totalMatrixRow(shakeMatrix,6,3);
        double totalVhShake = totalMatrixRow(shakeMatrix,6,4);
        System.out.println("Total column Shake finish");

        System.out.println("Total column steps start");
        double totalVsSteps = totalMatrixRow(stepsMatrix,10,0);
        double totalSSteps = totalMatrixRow(stepsMatrix,10,1);
        double totalNSteps = totalMatrixRow(stepsMatrix,10,2);
        double totalHSteps = totalMatrixRow(stepsMatrix,10,3);
        double totalVhSteps = totalMatrixRow(stepsMatrix,10,4);
        System.out.println("Total column Shake Finish");

        System.out.println("Total row combined start");
        double totalVSCombined = totalMatrixRow(stepsMatrix,10,0) + totalMatrixRow(shakeMatrix, 6,0);
        double totalSCombined = totalMatrixRow(stepsMatrix,10,1) + totalMatrixRow(shakeMatrix, 6,1);
        double totalNCombined = totalMatrixRow(stepsMatrix,10,2) + totalMatrixRow(shakeMatrix, 6,2);
        double totalHCombined = totalMatrixRow(stepsMatrix,10,3) + totalMatrixRow(shakeMatrix, 6,3);
        double totalVHCombined = totalMatrixRow(stepsMatrix,10,4) + totalMatrixRow(shakeMatrix, 6,4);
        System.out.println("Total row combined finish");

        System.out.println("Total combined start");
        double totalCombined = totalMatrix(stepsMatrix,9,5) + totalMatrix(shakeMatrix, 5, 5);
        System.out.println("Total combined finish");

        double theta = tVS + tS + tN + tH + tVH;

        double predictVS = (((shakeBucket(shakes)/totalVsShake) * (stepBucket(steps)/totalVsSteps))/theta)*(totalVSCombined/totalCombined);
        double predictS = (((shakeBucket(shakes)/totalSShake) * (stepBucket(steps)/totalSSteps))/theta)*(totalSCombined/totalCombined);
        double predictN = (((shakeBucket(shakes)/totalNShake) * (stepBucket(steps)/totalNSteps))/theta)*(totalNCombined/totalCombined);
        double predictH = (((shakeBucket(shakes)/totalHShake) * (stepBucket(steps)/totalHSteps))/theta)*(totalHCombined/totalCombined);
        double predictVH = (((shakeBucket(shakes)/totalVhShake) * (stepBucket(steps)/totalVhSteps))/theta)*(totalVHCombined/totalCombined);

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

    public static double matrixTheta(int[][] stepsMatrix, int[][] shakeMatrix, int shake, int steps, int mood ){


                double totalMoodShake = totalMatrixRow(shakeMatrix,6,mood);
                double totalMoodSteps = totalMatrixRow(stepsMatrix,10,mood);
                double totalMoodCombined = totalMatrixRow(stepsMatrix,10,mood) + totalMatrixRow(shakeMatrix, 6,mood);
                double totalCombined = totalMatrix(stepsMatrix,10,5) + totalMatrix(shakeMatrix, 6, 5);

                return ((shakeBucket(shake)/totalMoodShake) * (stepBucket(steps)/totalMoodSteps)) * (totalMoodCombined/totalCombined);

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
}
