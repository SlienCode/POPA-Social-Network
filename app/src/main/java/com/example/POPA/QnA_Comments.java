package com.example.POPA;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QnA_Comments {

    public String author;
    public String text;
    public int likes;

    public QnA_Comments(String author, String text, int likes) {
        this.author = author;
        this.text = text;
        this.likes = likes;

    }
}


