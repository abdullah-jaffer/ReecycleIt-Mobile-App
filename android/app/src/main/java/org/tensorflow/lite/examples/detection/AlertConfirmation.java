package org.tensorflow.lite.examples.detection;

import com.google.gson.annotations.SerializedName;

public class AlertConfirmation {

    private int id;
    @SerializedName("recycler_email")
    private String recyclerEmail;
    @SerializedName("org_email")
    private String orgEmail;
    private String date;
    @SerializedName("item_list")
    private String itemList;
    private String confirmation;
    private String response;

    public AlertConfirmation(int id, String recyclerEmail, String orgEmail, String date, String itemList, String confirmation, String response) {
        this.id = id;
        this.recyclerEmail = recyclerEmail;
        this.orgEmail = orgEmail;
        this.date = date;
        this.itemList = itemList;
        this.confirmation = confirmation;
        this.response = response;
    }

    public int getId() {
        return id;
    }

    public String getRecyclerEmail() {
        return recyclerEmail;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public String getDate() {
        return date;
    }

    public String getItemList() {
        return itemList;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public String getResponse() {
        return response;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecyclerEmail(String recyclerEmail) {
        this.recyclerEmail = recyclerEmail;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "AlertInformation{" +
                "id=" + id +
                ", recyclerEmail='" + recyclerEmail + '\'' +
                ", orgEmail='" + orgEmail + '\'' +
                ", date='" + date + '\'' +
                ", itemList='" + itemList + '\'' +
                ", confirmation='" + confirmation + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
