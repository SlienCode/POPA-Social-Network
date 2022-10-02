package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ViewProfile extends AppCompatActivity {

    ImageButton backButton;
    ImageView pic;
    TextView username;
    TextView created_on;
    TextView posts;
    TextView credits;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        backButton = findViewById(R.id.backButton);
        pic = findViewById((R.id.pic));
        username = findViewById(R.id.username);
        created_on = findViewById(R.id.created_on);
        posts = findViewById(R.id.posts);
        credits = findViewById(R.id.credits);

        key = getIntent().getStringExtra("key");

        //user's details
        ValueEventListener userValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pic.setImageResource(getResources().getIdentifier("icon" + dataSnapshot.child("pic").getValue(), "drawable", getPackageName()));
                username.setText(dataSnapshot.child("username").getValue().toString());
                created_on.setText("Εγγράφηκε: " + dataSnapshot.child("created_on").getValue().toString());
                posts.setText("Αναρτήσεις: " + dataSnapshot.child("posts").getValue().toString());
                credits.setText("Credits: " + dataSnapshot.child("credits").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Users.getRef().child(key).addListenerForSingleValueEvent(userValueEventListener);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

}