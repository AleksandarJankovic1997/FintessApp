package com.example.myapplication.myapplication.Models;
public class Training  {
    private int id;
    private String date;
    private String totalsteps;
    private String totaldistance;
    private String totalduration;
    private String totalCalories;
    public Training(){

    }
    public Training(String a, String b, String c, String d,String e){
        this.date=a;
        this.totalsteps=b;
        this.totaldistance=c;
        this.totalduration=d;
        this.totalCalories=e;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTotalsteps() { return totalsteps; }
    public void setTotalsteps(String totalsteps) { this.totalsteps = totalsteps; }
    public String getTotaldistance() { return totaldistance; }
    public void setTotaldistance(String totaldistance) { this.totaldistance = totaldistance; }
    public String getTotalduration() { return totalduration; }
    public void setTotalduration(String totalduration) { this.totalduration = totalduration; }
    public String getTotalCalories() { return totalCalories; }
    public void setTotalCalories(String totalCalories) { this.totalCalories = totalCalories; }
}
