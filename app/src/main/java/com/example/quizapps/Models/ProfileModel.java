package com.example.quizapps.Models;

public class ProfileModel {
    private String name;
    private String email;
    private String phone;
    private int bookmarsCount;

    public int getBookmarsCount() {
        return bookmarsCount;
    }

    public void setBookmarsCount(int bookmarsCount) {
        this.bookmarsCount = bookmarsCount;
    }

    public ProfileModel(String name, String email, String phone, int bookmarsCount) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bookmarsCount=bookmarsCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
