package com.example.POPA;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Trades {

    //reference to the question table on firebase
    private static DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("BookTrade");

    public String author;
    public String type;
    public String offer;
    public String want;

    public Trades(String author, String type, String offer, String want) {
        this.author = author;
        this.type = type;
        this.offer = offer;
        this.want = want;

    }

    public static DatabaseReference getRef() {
        return ref;
    }
}
