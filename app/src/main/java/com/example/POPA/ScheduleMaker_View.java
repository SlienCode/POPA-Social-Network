package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ScheduleMaker_View extends AppCompatActivity {

    LinearLayout mparent;
    LayoutInflater layoutInflater;
    ImageButton backButton;
    Button saveButton;
    HashMap<String, String[]> table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_maker_view);

        mparent = findViewById(R.id.linearLayout);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        if (getIntent().getBooleanExtra("loaded", false)) {

            DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ScheduleMaker").child(Key.key);
            ValueEventListener scheduleValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String hours[];

                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                    for (DataSnapshot course : children) {

                        View myView = layoutInflater.inflate(R.layout.schedule_maker_tab, null, false);
                        TextView courseName = myView.findViewById(R.id.courseName);
                        TextView monday = myView.findViewById(R.id.monday);
                        TextView tuesday = myView.findViewById(R.id.tuesday);
                        TextView wednesday = myView.findViewById(R.id.wednesday);
                        TextView thursday = myView.findViewById(R.id.thursday);
                        TextView friday = myView.findViewById(R.id.friday);

                        courseName.setText(course.child("name").getValue().toString());
                        monday.setText(course.child("monday").getValue().toString());
                        tuesday.setText(course.child("tuesday").getValue().toString());
                        wednesday.setText(course.child("wednesday").getValue().toString());
                        thursday.setText(course.child("thursday").getValue().toString());
                        friday.setText(course.child("friday").getValue().toString());

                        mparent.addView(myView);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabaseReference.addListenerForSingleValueEvent(scheduleValueEventListener);
        }
        else {

            table = (HashMap<String, String[]>)getIntent().getSerializableExtra("map");

            //set the table on layout
            for (String course: table.keySet()) {

                View myView = layoutInflater.inflate(R.layout.schedule_maker_tab, null, false);
                TextView courseName = myView.findViewById(R.id.courseName);
                TextView monday = myView.findViewById(R.id.monday);
                TextView tuesday = myView.findViewById(R.id.tuesday);
                TextView wednesday = myView.findViewById(R.id.wednesday);
                TextView thursday = myView.findViewById(R.id.thursday);
                TextView friday = myView.findViewById(R.id.friday);

                courseName.setText(course);
                monday.setText(table.get(course)[0]);
                tuesday.setText(table.get(course)[1]);
                wednesday.setText(table.get(course)[2]);
                thursday.setText(table.get(course)[3]);
                friday.setText(table.get(course)[4]);

                mparent.addView(myView);
            }
        }

        //save the schedule on the database
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!getIntent().getBooleanExtra("loaded", false)) {
                    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("ScheduleMaker").child(Key.key);

                    mDatabaseReference.removeValue();

                    //course id increment
                    int i = 0;

                    for (String course : table.keySet()) {
                        mDatabaseReference.child("CID" + i).child("name").getRef().setValue(course);
                        mDatabaseReference.child("CID" + i).child("monday").getRef().setValue(table.get(course)[0]);
                        mDatabaseReference.child("CID" + i).child("tuesday").getRef().setValue(table.get(course)[1]);
                        mDatabaseReference.child("CID" + i).child("wednesday").getRef().setValue(table.get(course)[2]);
                        mDatabaseReference.child("CID" + i).child("thursday").getRef().setValue(table.get(course)[3]);
                        mDatabaseReference.child("CID" + i).child("friday").getRef().setValue(table.get(course)[4]);

                        i++;
                    }
                }
            }
        });
    }
}