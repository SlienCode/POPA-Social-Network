package com.example.POPA;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Questions {

    //reference to the question table on firebase
    private static DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("QnA");

    public String author;
    public String title;
    public String text;
    public int likes;

    public Questions(String author, String title, String text, int likes) {
        this.author = author;
        this.title = title;
        this.text = text;
        this.likes = likes;

    }

    public static DatabaseReference getRef() {
        return ref;
    }
}