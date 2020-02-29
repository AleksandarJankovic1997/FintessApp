package com.example.myapplication.myapplication.Models;
public class UserModelSingleton {
    private User user=null;
    public static UserModelSingleton instance=null;
    private UserModelSingleton(){

    }
    public static UserModelSingleton getInstance(){
        if(instance==null){
            instance=new UserModelSingleton();
        }
        return instance;
    }
    public void setUser(User u){
        this.user=u;
    }
    public User getUser(){
        return this.user;
    }
}
