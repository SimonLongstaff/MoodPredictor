package com.example.moodpredictor;

public class LocMoodCalObject {

    private double locMoodVs;
    private double locMoodS;
    private double locMoodN;
    private double locMoodH;
    private double locMoodVh;

    private double rowVs;
    private double rowS;
    private double rowN;
    private double rowH;
    private double rowVh;

    @Override
    public String toString() {
        return "LocMoodCalObject{" +
                "locMoodVs=" + locMoodVs +
                ", locMoodS=" + locMoodS +
                ", locMoodN=" + locMoodN +
                ", locMoodH=" + locMoodH +
                ", locMoodVh=" + locMoodVh +
                ", rowVs=" + rowVs +
                ", rowS=" + rowS +
                ", rowN=" + rowN +
                ", rowH=" + rowH +
                ", rowVh=" + rowVh +
                ", total=" + total +
                '}';
    }

    private double total;

    public LocMoodCalObject(double locMoodVs, double locMoodS, double locMoodN, double locMoodH, double locMoodVh, double rowVs, double rowS, double rowN, double rowH, double rowVh, double total) {
        this.locMoodVs = locMoodVs;
        this.locMoodS = locMoodS;
        this.locMoodN = locMoodN;
        this.locMoodH = locMoodH;
        this.locMoodVh = locMoodVh;
        this.rowVs = rowVs;
        this.rowS = rowS;
        this.rowN = rowN;
        this.rowH = rowH;
        this.rowVh = rowVh;
        this.total = total;
    }

    public double getLocMoodVs() {
        return locMoodVs;
    }

    public void setLocMoodVs(double locMoodVs) {
        this.locMoodVs = locMoodVs;
    }

    public double getLocMoodS() {
        return locMoodS;
    }

    public void setLocMoodS(double locMoodS) {
        this.locMoodS = locMoodS;
    }

    public double getLocMoodN() {
        return locMoodN;
    }

    public void setLocMoodN(double locMoodN) {
        this.locMoodN = locMoodN;
    }

    public double getLocMoodH() {
        return locMoodH;
    }

    public void setLocMoodH(double locMoodH) {
        this.locMoodH = locMoodH;
    }

    public double getLocMoodVh() {
        return locMoodVh;
    }

    public void setLocMoodVh(double locMoodVh) {
        this.locMoodVh = locMoodVh;
    }

    public double getRowVs() {
        return rowVs;
    }

    public void setRowVs(double rowVs) {
        this.rowVs = rowVs;
    }

    public double getRowS() {
        return rowS;
    }

    public void setRowS(double rowS) {
        this.rowS = rowS;
    }

    public double getRowN() {
        return rowN;
    }

    public void setRowN(double rowN) {
        this.rowN = rowN;
    }

    public double getRowH() {
        return rowH;
    }

    public void setRowH(double rowH) {
        this.rowH = rowH;
    }

    public double getRowVh() {
        return rowVh;
    }

    public void setRowVh(double rowVh) {
        this.rowVh = rowVh;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
