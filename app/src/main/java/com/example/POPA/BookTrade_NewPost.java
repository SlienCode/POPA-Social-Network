package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BookTrade_NewPost extends AppCompatActivity {

    ImageButton backButton;
    RadioButton radio_one;
    RadioButton radio_two;
    RadioButton radio_three;
    TextView textView;
    EditText offerInput;
    EditText wantInput;
    TextView submitButton;
    ImageView euroImage;

    //type of trade: deal, sell, buy
    String type;

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trade_new_post);

        textView = findViewById(R.id.textView);
        offerInput = findViewById(R.id.offerInput);
        wantInput = findViewById(R.id.wantInput);
        euroImage = findViewById(R.id.euroImage);

        type = "deal";

        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        textParam.setMargins(20,20,20, 0);

        wantInput.setLayoutParams(textParam);

        radio_one = findViewById(R.id.radio_one);
        radio_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setVisibility(View.VISIBLE);
                offerInput.setVisibility(View.VISIBLE);
                euroImage.setVisibility(View.GONE);

                LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                textParam.setMargins(20,20,20, 0);

                wantInput.setLayoutParams(textParam);

                offerInput.setHint("Σύγγραμμα που δίνετε");
                wantInput.setHint("Σύγγραμμα που παίρνετε");

                //empty the editTexts
                offerInput.getText().clear();
                offerInput.clearFocus();
                offerInput.setError(null);
                wantInput.getText().clear();
                wantInput.clearFocus();
                wantInput.setError(null);

                type = "deal";
            }
        });

        radio_two= findViewById(R.id.radio_two);
        radio_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setVisibility(View.GONE);
                offerInput.setVisibility(View.GONE);
                euroImage.setVisibility(View.GONE);

                LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                textParam.setMargins(20,20,20, 0);

                wantInput.setLayoutParams(textParam);

                wantInput.setHint("Σύγγραμμα που αναζητάτε");

                //empty the editTexts
                offerInput.getText().clear();
                offerInput.clearFocus();
                offerInput.setError(null);
                wantInput.getText().clear();
                wantInput.clearFocus();
                wantInput.setError(null);

                type = "buy";
            }
        });

        radio_three= findViewById(R.id.radio_three);
        radio_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setVisibility(View.VISIBLE);
                offerInput.setVisibility(View.VISIBLE);
                euroImage.setVisibility(View.VISIBLE);
                wantInput.setWidth(200);

                offerInput.setHint("Σύγγραμμα που πουλάτε");
                wantInput.setHint("Τιμή σε ευρώ");

                //empty the editTexts
                offerInput.getText().clear();
                offerInput.clearFocus();
                offerInput.setError(null);
                wantInput.getText().clear();
                wantInput.clearFocus();
                wantInput.setError(null);

                type = "sell";
            }
        });

        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equals("deal")) {

                    if (offerInput.getText().toString().equals(""))
                        offerInput.setError("Παρακαλώ δώστε όνομα συγγράμματος.");
                    else if (wantInput.getText().toString().equals(""))
                        wantInput.setError("Παρακαλώ δώστε όνομα συγγράμματος.");
                    else {

                        increasePostCount();

                        Trades trade = new Trades(Key.key, type, offerInput.getText().toString(), wantInput.getText().toString());
                        Trades.getRef().push().setValue(trade);
                        Toast.makeText(getApplicationContext(), "Επτιτυχής δημιουργία αγγελίας.", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }
                else if (type.equals("buy")) {
                    if (wantInput.getText().toString().equals(""))
                        wantInput.setError("Παρακαλώ δώστε όνομα συγγράμματος.");
                    else {

                        increasePostCount();

                        Trades trade = new Trades(Key.key, type, "", wantInput.getText().toString());
                        Trades.getRef().push().setValue(trade);
                        Toast.makeText(getApplicationContext(), "Επτιτυχής δημιουργία αγγελίας.", Toast.LENGTH_SHORT).show();

                        finish();
                    }
                }
                else {
                    try {
                        if (offerInput.getText().toString().equals(""))
                            offerInput.setError("Παρακαλώ δώστε όνομα συγγράμματος.");
                        else if (wantInput.getText().toString().equals(""))
                            wantInput.setError("Παρακαλώ δώστε μία τιμή.");
                        else {

                            String priceStr = wantInput.getText().toString();
                            priceStr = priceStr.replaceAll("\\s+","");
                            priceStr = priceStr.replaceAll(",", ".");
                            double price = Double.parseDouble(priceStr);

                            //round price to 2 decimal places
                            price = Math.round(price * 100.0) / 100.0;

                            increasePostCount();

                            Trades trade = new Trades(Key.key, type, offerInput.getText().toString(), String.valueOf(df.format(price)));
                            Trades.getRef().push().setValue(trade);
                            Toast.makeText(getApplicationContext(), "Επτιτυχής δημιουργία αγγελίας.", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    }
                    catch (Exception e) {
                        wantInput.setError("Παρακαλώ δώστε μία έγκυρη τιμή.");
                    }

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

    protected void increasePostCount() {

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
    }
}