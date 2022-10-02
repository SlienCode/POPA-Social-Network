package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {

    ImageButton backButton;
    ImageView pic;
    TextView username;
    TextView created_on;
    TextView posts;
    TextView credits;
    ImageButton icon[] = new ImageButton[8];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        backButton = findViewById(R.id.backButton);
        pic = findViewById((R.id.pic));
        username = findViewById(R.id.username);
        created_on = findViewById(R.id.created_on);
        posts = findViewById(R.id.posts);
        credits = findViewById(R.id.credits);
        icon[0] = findViewById(R.id.icon0);
        icon[1] = findViewById(R.id.icon1);
        icon[2] = findViewById(R.id.icon2);
        icon[3] = findViewById(R.id.icon3);
        icon[4] = findViewById(R.id.icon4);
        icon[5] = findViewById(R.id.icon5);
        icon[6] = findViewById(R.id.icon6);
        icon[7] = findViewById(R.id.icon7);

        //user's details
        ValueEventListener userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pic.setImageResource(getResources().getIdentifier("icon" + dataSnapshot.child(Key.key).child("pic").getValue(), "drawable", getPackageName()));
                username.setText(dataSnapshot.child(Key.key).child("username").getValue().toString());
                created_on.setText("Εγγράφηκε: " + dataSnapshot.child(Key.key).child("created_on").getValue().toString());
                posts.setText("Αναρτήσεις: " + dataSnapshot.child(Key.key).child("posts").getValue().toString());
                credits.setText("Credits: " + dataSnapshot.child(Key.key).child("credits").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Users.getRef().addListenerForSingleValueEvent(userValueEventListener);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //for every button
        for (int i = 0; i < 8; i++) {

            int finalI = i;

            icon[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //change the pic
                    Users.getRef().child(Key.key).child("pic").setValue(finalI);
                    String file = "icon" + finalI;
                    int resID = getResources().getIdentifier(file, "drawable", getPackageName());
                    pic.setImageResource(resID);
                }

            });
        }

    }

}