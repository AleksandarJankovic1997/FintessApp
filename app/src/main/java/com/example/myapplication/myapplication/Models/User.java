package com.example.myapplication.myapplication.Models;

public class User {
    private String firstname;
    private String surname;
    private String nickname;
    private String country;
    private String city;
    private Integer age;
    private String gender;
    private Integer weight;
    private Integer height;
    private boolean guest;
    private String photoadress;
    private int color;


    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }


    public User() {

    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public User(String firstname, String surname, String nickname, String country, String city, Integer age, boolean guest, String photoadress, int color) {
        this.firstname=firstname;
        this.surname=surname;
        this.nickname=nickname;
        this.country=country;
        this.city=city;
        this.age=age;
        this.guest=guest;
        this.photoadress=photoadress;
        this.color=color;

    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getFirstname() {
        return firstname;
    }
    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public String getPhotoadress() {
        return photoadress;
    }

    public void setPhotoadress(String photoadress) {
        this.photoadress = photoadress;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
