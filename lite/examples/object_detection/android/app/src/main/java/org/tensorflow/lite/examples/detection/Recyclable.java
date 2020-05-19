package org.tensorflow.lite.examples.detection;

import java.io.Serializable;

public class Recyclable implements Serializable {

    private String show;
    private String category;
    private String howTo;
    private String energySaving;
    private int count;
    private boolean checked;

    public Recyclable(String show, String category, String howTo, String energySaving, int count) {
        this.show = show;
        this.category = category;
        this.howTo = howTo;
        this.energySaving = energySaving;
        this.count = count;
        this.checked = false;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setHowTo(String howTo) {
        this.howTo = howTo;
    }

    public void setEnergySaving(String energySaving) {
        this.energySaving = energySaving;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getShow() {
        return show;
    }

    public String getCategory() {
        return category;
    }

    public String getHowTo() {
        return howTo;
    }

    public String getEnergySaving() {
        return energySaving;
    }

    public int getCount() {
        return count;
    }

    public boolean isChecked() {
        return checked;
    }
}
