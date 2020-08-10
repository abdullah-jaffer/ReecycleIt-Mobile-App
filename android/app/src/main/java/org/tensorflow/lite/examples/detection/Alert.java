package org.tensorflow.lite.examples.detection;

import com.google.gson.annotations.SerializedName;

public class Alert {
    private int id;
    private String date;
    private String type;
    @SerializedName("item_list")
    private String itemList;



    public Alert() {
        this.id = 0 ;
        this.date = null;
        this.type = null;
        this.itemList = null;
    }

    public Alert(int id, String date, String type, String itemList) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.itemList = itemList;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getItemList() {
        return itemList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }
}
