package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainMenu extends AppCompatActivity {

    ImageButton settingsButton;
    Button option0Button;
    Button option1Button;
    Button option2Button;
    Button option3Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //top right image depending on the user
        loadImage();

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to settings
                Intent settingsIntent = new Intent(MainMenu.this, Settings.class);
                startActivity(settingsIntent);
            }

        });

        option0Button = findViewById((R.id.option0Button));
        option0Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to record
                Intent recordIntent = new Intent(MainMenu.this, QnA.class);
                startActivity(recordIntent);
            }

        });

        option1Button = findViewById((R.id.option1Button));
        option1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to record
                Intent recordIntent = new Intent(MainMenu.this, BookTrade.class);
                startActivity(recordIntent);
            }

        });

        option2Button = findViewById((R.id.option2Button));
        option2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to record
                Intent recordIntent = new Intent(MainMenu.this, ScheduleMaker.class);
                startActivity(recordIntent);
            }

        });

        option3Button = findViewById((R.id.option3Button));
        option3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to record
                Intent recordIntent = new Intent(MainMenu.this, Caldendar_Notes.class);
                startActivity(recordIntent);
            }

        });
    }

    @Override
    protected void onRestart() {

        //load top right image every time you go back to the main menu
        loadImage();

        super.onRestart();
    }

    protected void loadImage() {

        ValueEventListener picValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                settingsButton.setImageResource(getResources().getIdentifier("icon" + dataSnapshot.child(Key.key).child("pic").getValue(), "drawable", getPackageName()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Users.getRef().addListenerForSingleValueEvent(picValueEventListener);
    }
}