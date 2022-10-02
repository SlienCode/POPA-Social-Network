package com.example.POPA;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Notes {

    //reference to the question table on firebase
    private static DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CalendarNotes");

    public String author;
    public String date;
    public String text;
    public int likes;

    public Notes(String author, String date, String text, int likes) {
        this.author = author;
        this.date = date;
        this.text = text;
        this.likes = likes;

    }

    public static DatabaseReference getRef() {
        return ref;
    }
}