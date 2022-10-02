package com.example.POPA;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Calendar_Notes_Course_NewPost extends AppCompatActivity {

    ImageButton backButton;
    Button dateButton;
    EditText textInput;
    TextView submitButton;

    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_notes_course_new_post);

        dateButton = findViewById(R.id.dateButton);
        textInput = findViewById(R.id.textInput);

        date = "";

        String id = getIntent().getStringExtra("id");

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Calendar_Notes_Course_NewPost.this, Calendar.class);
                someActivityResultLauncher.launch(intent);
            }
        });

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = textInput.getText().toString();

                if (date.equals(""));
                else if (text.equals(""))
                    textInput.setError("Παρακαλώ γράψτε την ερώτηση σας.");
                else {

                    //increase user's post count
                    ValueEventListener postCreatorValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int i = Integer.parseInt(dataSnapshot.child("posts").getValue().toString());
                            i++;
                            dataSnapshot.child("posts").getRef().setValue(i);
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    Users.getRef().child(Key.key).addListenerForSingleValueEvent(postCreatorValueEventListener);

                    Notes note = new Notes(Key.key, date, text, 0);
                    Notes.getRef().child("CID" + id).push().setValue(note);
                    Toast.makeText(getApplicationContext(), "Επτιτυχής δημιουργία σημείωσης.", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {

                Intent data = result.getData();
                date = data.getStringExtra("dayOfMonth") + "/" + data.getStringExtra("month") + "/" + data.getStringExtra("year");
            }
        }
    });
}