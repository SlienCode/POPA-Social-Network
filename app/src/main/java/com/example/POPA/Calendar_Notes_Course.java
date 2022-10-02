package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Calendar_Notes_Course extends AppCompatActivity {

    ImageButton backButton;
    ImageButton plusButton;
    TextView text;
    ListView listView;
    ArrayList<String> notes;
    ArrayList<String> noteKeys;

    String course_title;

    //course id (from 0 to 56)
    String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_notes_course);

        courseID = getIntent().getStringExtra("id");
        text = findViewById(R.id.text);

        course_title = getIntent().getStringExtra("course");
        text.setText(course_title);

        loadNotes();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        plusButton = findViewById(R.id.plusButton);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //go to NewPost
                Intent intent = new Intent(Calendar_Notes_Course.this, Calendar_Notes_Course_NewPost.class);
                intent.putExtra("id", courseID);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {

        //load questions every time you come back to this tab
        loadNotes();

        super.onRestart();
    }

    protected void loadNotes() {

        notes = new ArrayList<String>();
        noteKeys = new ArrayList<String>();
        listView = findViewById(R.id.listView);

        ValueEventListener noteValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    notes.add("Σημειώσεις από " + child.child("date").getValue().toString() + ".");
                    noteKeys.add(child.getKey());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Calendar_Notes_Course.this, R.layout.list_view, R.id.textView, notes);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(Calendar_Notes_Course.this, Calendar_Notes_Course_ViewPost.class);
                        intent.putExtra("key", noteKeys.get(position));
                        intent.putExtra("id", courseID);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Notes.getRef().child("CID" + courseID).addListenerForSingleValueEvent(noteValueEventListener);
    }
}