package com.example.to_be_decided;

public class Pills {
    private String name;
    private String barcode;
    private String owner;
    private String time;
    private String week;
    private String id;

    public Pills(String id,String name, String barcode, String owner, String time, String week) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.owner = owner;

        this.time = time;
        this.week = week;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
