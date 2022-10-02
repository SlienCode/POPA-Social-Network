package com.example.POPA;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Users {

    //reference to the user table on firebase
    private static DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

    public String nameID;
    public String username;
    public String password;
    public String created_on;
    public int posts;
    public int credits;
    public int pic;

    public Users(String nameID, String username, String password, String created_on, int posts, int credits, int pic) {
        this.nameID = nameID;
        this.username = username;
        this.password = password;
        this.created_on = created_on;
        this.posts = posts;
        this.credits = credits;
        this.pic = pic;

    }

    public static DatabaseReference getRef() {
        return ref;
    }
}