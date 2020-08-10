package org.tensorflow.lite.examples.detection;

public class GoalItem {
    int goalCount;
    int currentCount;
    String date;

    public GoalItem(int goalCount, int currentCount, String date) {
        this.goalCount = goalCount;
        this.currentCount = currentCount;
        this.date = date;
    }

    public int getGoalCount() {
        return goalCount;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public String getDate() {
        return date;
    }

    public void setGoalCount(int goalCount) {
        this.goalCount = goalCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
