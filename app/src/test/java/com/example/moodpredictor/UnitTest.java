package com.example.moodpredictor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests
 */
public class UnitTest {


    @Test
    public void bucketStep(){
        assertEquals(0,BayesHelper.stepBucket(0));
        assertEquals(0,BayesHelper.stepBucket(500));
        assertEquals(1,BayesHelper.stepBucket(1500));
        assertEquals(2,BayesHelper.stepBucket(2500));
        assertEquals(3,BayesHelper.stepBucket(3500));
        assertEquals(4,BayesHelper.stepBucket(4500));
        assertEquals(5,BayesHelper.stepBucket(5500));
        assertEquals(6,BayesHelper.stepBucket(6500));
        assertEquals(7,BayesHelper.stepBucket(7500));
        assertEquals(8,BayesHelper.stepBucket(8500));
        assertEquals(9,BayesHelper.stepBucket(9500));
        assertEquals(9,BayesHelper.stepBucket(9500000));
    }

    @Test
    public void bucketShake(){
        assertEquals(0,BayesHelper.shakeBucket(0));
        assertEquals(0,BayesHelper.shakeBucket(2000));
        assertEquals(1,BayesHelper.shakeBucket(2001));
        assertEquals(1,BayesHelper.shakeBucket(4000));
        assertEquals(2,BayesHelper.shakeBucket(4001));
        assertEquals(2,BayesHelper.shakeBucket(6000));
        assertEquals(3,BayesHelper.shakeBucket(6001));
        assertEquals(3,BayesHelper.shakeBucket(8000));
        assertEquals(4,BayesHelper.shakeBucket(8001));
        assertEquals(4,BayesHelper.shakeBucket(10000));
        assertEquals(5,BayesHelper.shakeBucket(10001));

    }

    @Test
    public void bucketTime(){
        assertEquals(0,BayesHelper.onTimeBucket(0));
        assertEquals(0,BayesHelper.onTimeBucket(3599));
        assertEquals(1,BayesHelper.onTimeBucket(3600));
        assertEquals(1,BayesHelper.onTimeBucket(7199));
        assertEquals(2,BayesHelper.onTimeBucket(7200));
        assertEquals(2,BayesHelper.onTimeBucket(10799));
        assertEquals(3,BayesHelper.onTimeBucket(10800));
        assertEquals(3,BayesHelper.onTimeBucket(14399));
        assertEquals(4,BayesHelper.onTimeBucket(14400));
        assertEquals(4,BayesHelper.onTimeBucket(17999));
        assertEquals(5,BayesHelper.onTimeBucket(18000));
        assertEquals(5,BayesHelper.onTimeBucket(21599));
        assertEquals(6,BayesHelper.onTimeBucket(21600));
        assertEquals(6,BayesHelper.onTimeBucket(25199));
        assertEquals(7,BayesHelper.onTimeBucket(25200));
        assertEquals(7,BayesHelper.onTimeBucket(28799));
        assertEquals(8,BayesHelper.onTimeBucket(28800));
        assertEquals(8,BayesHelper.onTimeBucket(32399));
        assertEquals(9,BayesHelper.onTimeBucket(32400));
        assertEquals(9,BayesHelper.onTimeBucket(35999));
        assertEquals(10,BayesHelper.onTimeBucket(36000));
        assertEquals(10,BayesHelper.onTimeBucket(39599));
        assertEquals(11,BayesHelper.onTimeBucket(39600));
        assertEquals(11,BayesHelper.onTimeBucket(43199));
        assertEquals(12,BayesHelper.onTimeBucket(43200));
        assertEquals(12,BayesHelper.onTimeBucket(46799));
        assertEquals(13,BayesHelper.onTimeBucket(46800));
        assertEquals(13,BayesHelper.onTimeBucket(50399));
        assertEquals(14,BayesHelper.onTimeBucket(50400));
        assertEquals(14,BayesHelper.onTimeBucket(57599));
        assertEquals(15,BayesHelper.onTimeBucket(57600));
        assertEquals(15,BayesHelper.onTimeBucket(80000));

    }

    @Test
    public void bucketVisit(){
        assertEquals(0,BayesHelper.dayBucket(0));
        assertEquals(0,BayesHelper.dayBucket(8639));
        assertEquals(1,BayesHelper.dayBucket(8640));
        assertEquals(1,BayesHelper.dayBucket(17279));
        assertEquals(2,BayesHelper.dayBucket(17280));
        assertEquals(2,BayesHelper.dayBucket(25919));
        assertEquals(3,BayesHelper.dayBucket(25920));
        assertEquals(3,BayesHelper.dayBucket(34559));
        assertEquals(4,BayesHelper.dayBucket(34560));
        assertEquals(4,BayesHelper.dayBucket(43199));
        assertEquals(5,BayesHelper.dayBucket(43200));
        assertEquals(5,BayesHelper.dayBucket(51839));
        assertEquals(6,BayesHelper.dayBucket(51840));
        assertEquals(6,BayesHelper.dayBucket(60479));
        assertEquals(7,BayesHelper.dayBucket(60480));
        assertEquals(7,BayesHelper.dayBucket(69119));
        assertEquals(8,BayesHelper.dayBucket(69120));
        assertEquals(8,BayesHelper.dayBucket(77759));
        assertEquals(9,BayesHelper.dayBucket(77760));
        assertEquals(9,BayesHelper.dayBucket(100000));
    }

    @Test
    public void matrixTests() {

        int[][] testmatrix =
                {{1, 2, 3, 4, 5, 6},
                {7, 8, 9, 10, 11, 12},
                {6, 5, 4, 3, 2, 1},
                {12, 11, 10, 9, 8, 7},
                {1, 1, 1, 1, 1, 1},};

        //Total count
        assertEquals(162,BayesHelper.totalMatrix(testmatrix,6,5));
        //Column count
        assertEquals(27,BayesHelper.totalMatrixColumn(testmatrix,0,5));
        assertEquals(27,BayesHelper.totalMatrixColumn(testmatrix,1,5));
        assertEquals(27,BayesHelper.totalMatrixColumn(testmatrix,2,5));
        assertEquals(27,BayesHelper.totalMatrixColumn(testmatrix,3,5));
        assertEquals(27,BayesHelper.totalMatrixColumn(testmatrix,4,5));
        assertEquals(27,BayesHelper.totalMatrixColumn(testmatrix,5,5));
        //Row Count
        assertEquals(21,BayesHelper.totalMatrixRow(testmatrix,6,0));
        assertEquals(57,BayesHelper.totalMatrixRow(testmatrix,6,1));
        assertEquals(21,BayesHelper.totalMatrixRow(testmatrix,6,2));
        assertEquals(57,BayesHelper.totalMatrixRow(testmatrix,6,3));
        assertEquals(6,BayesHelper.totalMatrixRow(testmatrix,6,4));


    }

    @Test
    public void LocMoodCalTest(){
        int[][] testmatrix = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 50, 1, 1, 1, 1, 100, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {10, 1, 1, 1, 1, 1, 1, 1, 5, 1}};

        assertEquals(0.434, BayesHelper.locMoodCal(testmatrix,4,100),0.001);
        assertEquals(0.316, BayesHelper.locMoodCal(testmatrix,1,26000),0.001);
        assertEquals(0.632, BayesHelper.locMoodCal(testmatrix,1,70000),0.001);
    }


    @Test
    public void predictionTest() {
        int[][] loc1 = {{0, 0, 0, 0, 0, 10, 10, 10, 10, 10},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

        int[][] loc2 = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {10, 10, 10, 10, 10, 0, 0, 0, 0, 0}};

        double locMoodVs = 0;
        double locMoodS = 0;
        double locMoodN = 0;
        double locMoodH = 0;
        double locMoodVh = 0;

        double locMoodTotalVs = 0;
        double locMoodTotalS = 0;
        double locMoodTotalN = 0;
        double locMoodTotalH = 0;
        double locMoodTotalVh = 0;

        double locMoodVs2 = 0;
        double locMoodS2 = 0;
        double locMoodN2 = 0;
        double locMoodH2 = 0;
        double locMoodVh2 = 0;

        double locMoodTotalVs2 = 0;
        double locMoodTotalS2 = 0;
        double locMoodTotalN2 = 0;
        double locMoodTotalH2 = 0;
        double locMoodTotalVh2 = 0;

        double locMoodTotal = 0;

        for (int j = 0; j < 5; j++) {
            double rv = BayesHelper.locMoodCal(loc1, j, 100000);
            double rvTotal = BayesHelper.totalMatrixRow(loc1, 10, j);
            switch (j) {
                case 0:
                    locMoodVs = locMoodVs + rv;
                    locMoodTotalVs = locMoodTotalVs + rvTotal;
                    break;
                case 1:
                    locMoodS = locMoodS + rv;
                    locMoodTotalS = locMoodTotalS + rvTotal;
                    break;
                case 2:
                    locMoodN = locMoodN + rv;
                    locMoodTotalN = locMoodTotalN + rvTotal;
                    break;
                case 3:
                    locMoodH = locMoodH + rv;
                    locMoodTotalH = locMoodTotalH + rvTotal;
                    break;
                case 4:
                    locMoodVh = locMoodVh + rv;
                    locMoodTotalVh = locMoodTotalVh + rv;
                    break;
            }
        }


        for (int j = 0; j < 5; j++) {
            double rv = BayesHelper.locMoodCal(loc2, j, 10);
            double rvTotal = BayesHelper.totalMatrixRow(loc2, 10, j);
            switch (j) {
                case 0:
                    locMoodVs = locMoodVs + rv;
                    locMoodTotalVs = locMoodTotalVs + rvTotal;
                    break;
                case 1:
                    locMoodS = locMoodS + rv;
                    locMoodTotalS = locMoodTotalS + rvTotal;
                    break;
                case 2:
                    locMoodN = locMoodN + rv;
                    locMoodTotalN = locMoodTotalN + rvTotal;
                    break;
                case 3:
                    locMoodH = locMoodH + rv;
                    locMoodTotalH = locMoodTotalH + rvTotal;
                    break;
                case 4:
                    locMoodVh = locMoodVh + rv;
                    locMoodTotalVh = locMoodTotalVh + rv;
                    break;
            }
        }

        for (int j = 0; j < 5; j++) {
            double rv = BayesHelper.locMoodCal(loc1, j, 10);
            double rvTotal = BayesHelper.totalMatrixRow(loc1, 10, j);
            switch (j) {
                case 0:
                    locMoodVs2 = locMoodVs2 + rv;
                    locMoodTotalVs2 = locMoodTotalVs2 + rvTotal;
                    break;
                case 1:
                    locMoodS2 = locMoodS2 + rv;
                    locMoodTotalS2 = locMoodTotalS2 + rvTotal;
                    break;
                case 2:
                    locMoodN2 = locMoodN2 + rv;
                    locMoodTotalN2 = locMoodTotalN2 + rvTotal;
                    break;
                case 3:
                    locMoodH2 = locMoodH2 + rv;
                    locMoodTotalH2 = locMoodTotalH2 + rvTotal;
                    break;
                case 4:
                    locMoodVh2 = locMoodVh2 + rv;
                    locMoodTotalVh2 = locMoodTotalVh2 + rv;
                    break;
            }
        }


        for (int j = 0; j < 5; j++) {
            double rv = BayesHelper.locMoodCal(loc2, j, 100000);
            double rvTotal = BayesHelper.totalMatrixRow(loc2, 10, j);
            switch (j) {
                case 0:
                    locMoodVs2 = locMoodVs2 + rv;
                    locMoodTotalVs2 = locMoodTotalVs2 + rvTotal;
                    break;
                case 1:
                    locMoodS2 = locMoodS2 + rv;
                    locMoodTotalS2 = locMoodTotalS2 + rvTotal;
                    break;
                case 2:
                    locMoodN2 = locMoodN2 + rv;
                    locMoodTotalN2 = locMoodTotalN2 + rvTotal;
                    break;
                case 3:
                    locMoodH2 = locMoodH2 + rv;
                    locMoodTotalH2 = locMoodTotalH2 + rvTotal;
                    break;
                case 4:
                    locMoodVh2 = locMoodVh2 + rv;
                    locMoodTotalVh2 = locMoodTotalVh2 + rv;
                    break;
            }
        }

        locMoodTotal = locMoodTotal + BayesHelper.totalMatrix(loc1, 10, 5);
        locMoodTotal = locMoodTotal + BayesHelper.totalMatrix(loc2, 10, 5);

        LocMoodCalObject locMoodCalObject = new LocMoodCalObject(locMoodVs, locMoodS, locMoodN, locMoodH, locMoodVh,
                locMoodTotalVs, locMoodTotalS, locMoodTotalN, locMoodTotalH, locMoodTotalVh, locMoodTotal);

        LocMoodCalObject locMoodCalObject2 = new LocMoodCalObject(locMoodVs2, locMoodS2, locMoodN2, locMoodH2, locMoodVh2,
                locMoodTotalVs2, locMoodTotalS2, locMoodTotalN2, locMoodTotalH2, locMoodTotalVh2, locMoodTotal);


        int[][] steps = {{10, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 10}};

        int[][] shakes = {{10, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 10},};

        int[][] ontime = {{10, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 10},};

        assertTrue(BayesHelper.predictMoodShakeOnTime(steps,shakes,ontime,100,100,100,locMoodCalObject)<
                BayesHelper.predictMoodShakeOnTime(steps,shakes,ontime,10000,10000,100000,locMoodCalObject2));

    }



}