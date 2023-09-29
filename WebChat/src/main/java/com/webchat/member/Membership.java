package com.webchat.member;


public class Membership {
    private  int unique_id;
    private  String first_name;
    private String last_name;
    private String email;
    private String password;
    private String image;
    private String status;
    public Membership(int unique_id, String first_name, String last_name, String email, String password, String image,String status) {
        this.unique_id = unique_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.status=status;
    }

    @Override
    public String toString() {
        return "{\"unique_id\": " + unique_id  + ", \"first_name\": \"" + first_name + "\", \"last_name\": \"" + last_name + "\", \"image\": \"" + image + "\", \"status\": \"" + status + "\"}";
    }
}