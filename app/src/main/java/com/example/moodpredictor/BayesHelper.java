package com.example.moodpredictor;

import java.util.Arrays;

public class BayesHelper {

    //Takes a matrix of steps and moods as input, compares them to current steps to predict mood.
    public static int predictMood(int[][] matrix, int steps) {


        int stepbucket = stepBucket(steps);

        double totalVS = totalMatrixRow(matrix, 0);
        double totalS = totalMatrixRow(matrix, 1);
        double totalN = totalMatrixRow(matrix, 2);
        double totalH = totalMatrixRow(matrix, 3);
        double totalVH = totalMatrixRow(matrix, 4);

        double stepBucketTotal = totalMatrixCollumn(matrix, stepbucket);
        double total = totalMatrix(matrix);


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

    //Returns the total sum of values in a step matrix
    public static int totalMatrix(int[][] matrix) {
        int total = 0;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                total = total + matrix[j][i];
            }
        }
        System.out.println("Total Matrix: " + total);
        return total;
    }

    //Returns to the total value of a row in a matrix
    public static int totalMatrixRow(int[][] matrix, int row) {

        int total = 0;

        for (int i = 0; i < 10; i++) {
            total = total + matrix[row][i];
        }
        System.out.println("Total Matrix Row: " + total);
        return total;
    }

    //Return to the total value
    public static int totalMatrixCollumn(int[][] matrix, int collumn) {

        int total = 0;

        for (int i = 0; i < 5; i++) {
            total = total + matrix[i][collumn];
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
}
