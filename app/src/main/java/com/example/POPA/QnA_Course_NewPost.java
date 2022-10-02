package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QnA_Course_NewPost extends AppCompatActivity {

    ImageButton backButton;
    EditText titleInput;
    EditText textInput;
    TextView submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_course_new_post);

        titleInput = findViewById(R.id.titleInput);
        textInput = findViewById(R.id.textInput);

        String id = getIntent().getStringExtra("id");

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleInput.getText().toString();
                String text = textInput.getText().toString();

                if (title.equals(""))
                    titleInput.setError("Παρακαλώ δώστε έναν τίτλο.");
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

                    Questions question = new Questions(Key.key, title, text, 0);
                    Questions.getRef().child("CID" + id).push().setValue(question);
                    Toast.makeText(getApplicationContext(), "Επτιτυχής δημιουργία ανάρτησης.", Toast.LENGTH_SHORT).show();

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
}