package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    EditText usernameInput;
    EditText passwordInput;
    Button submitButton;
    TextView signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        submitButton = findViewById(R.id.submitButton);
        signUpButton = findViewById(R.id.signUpButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (username.equals(""))
                    usernameInput.setError("Παρακαλώ συμπληρώστε τα κενά.");
                else {

                    final Query usernameReference = Users.getRef().orderByChild("nameID").equalTo(username.toUpperCase());

                    ValueEventListener usernameValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {

                                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                                for (DataSnapshot child : children) {

                                    if (password.equals(child.child("password").getValue().toString())) {

                                        //getting the firebase key
                                        Key.key = child.getKey();
                                        submitButton.setEnabled(false);
                                        startActivity(new Intent(Login.this, MainMenu.class));
                                        finish();
                                    } else
                                        usernameInput.setError("Το ψευδώνυμο και ο κωδικός πρόσβασης δεν αντιστοιχούν σε κάποιο χρήστη.");
                                }

                            } else {
                                usernameInput.setError("Το ψευδώνυμο και ο κωδικός πρόσβασης δεν αντιστοιχούν σε κάποιο χρήστη.");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    usernameReference.addListenerForSingleValueEvent(usernameValueEventListener);
                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this, Signup.class));
            }
        });
    }
}