package ru.use.marathon.models;

/**
 * Created by ilyas on 16-Jul-18.
 */

public class Statistics {

    double averageTime;
    double wrongPercentage;
    int totalTasks;

    public Statistics(double averageTime, double wrongPercentage, int totalTasks) {
        this.averageTime = averageTime;
        this.wrongPercentage = wrongPercentage;
        this.totalTasks = totalTasks;
    }

    public int size(){
        return 3;
    }

    public double getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(double averageTime) {
        this.averageTime = averageTime;
    }

    public double getWrongPercentage() {
        return wrongPercentage;
    }

    public void setWrongPercentage(double wrongPercentage) {
        this.wrongPercentage = wrongPercentage;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }
}
