package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Signup extends AppCompatActivity {

    ImageButton backButton;
    EditText usernameInput;
    EditText passwordInput;
    EditText passwordConfirmInput;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        backButton = findViewById(R.id.backButton);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordConfirmInput = findViewById(R.id.passwordConfirmInput);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String passwordConfirm = passwordConfirmInput.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date = sdf.format(new Date());

                if (username.equals(""))
                    usernameInput.setError("Παρακαλώ δώστε ένα ψευδώνυμο.");
                else if (password.equals(""))
                    passwordInput.setError("Παρακαλώ δώστε έναν κωδικό πρόσβασης.");

                else {
                    if (password.equals(passwordConfirm)) {

                        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                        final Query usernameReference = mDatabaseReference.orderByChild("nameID").equalTo(username.toUpperCase());

                        ValueEventListener usernameValueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    usernameInput.setError("Το ψευδώνυμο δεν είναι διαθέσιμο.");
                                } else {
                                    Users user = new Users(username.toUpperCase(), username, password, date,0, 0, 0);
                                    Users.getRef().push().setValue(user);
                                    Toast.makeText(getApplicationContext(), "Επτιτυχής δημιουργία λογαριασμού.", Toast.LENGTH_SHORT).show();

                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        };

                        usernameReference.addListenerForSingleValueEvent(usernameValueEventListener);
                    }
                    else
                        passwordConfirmInput.setError("Οι κωδικοί πρόσβασης δεν ταιριάζουν.");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}