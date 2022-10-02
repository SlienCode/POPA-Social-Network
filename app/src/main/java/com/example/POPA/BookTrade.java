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

public class BookTrade extends AppCompatActivity {

    ImageButton backButton;
    ImageButton plusButton;
    ListView listView;
    ArrayList<String> trades;
    ArrayList<String> tradeKeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trade);

        loadTrades();

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
                Intent intent = new Intent(BookTrade.this, BookTrade_NewPost.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {

        //load top right image every time you go back to the main menu
        loadTrades();

        super.onRestart();
    }

    protected void loadTrades() {

        trades = new ArrayList<>();
        tradeKeys = new ArrayList<>();

        listView = findViewById(R.id.listView);

        ValueEventListener tradeValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    tradeKeys.add(child.getKey());

                    if (child.child("type").getValue().equals("deal"))
                        trades.add("Ανταλλάσσω " + child.child("offer").getValue().toString() + " για " + child.child("want").getValue().toString() + ".");
                    else if (child.child("type").getValue().equals("buy"))
                        trades.add("Αναζητώ " + child.child("want").getValue().toString() + ".");
                    else
                        trades.add("Πουλάω " + child.child("offer").getValue().toString() + " για " + child.child("want").getValue().toString() + "€.");
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(BookTrade.this, R.layout.list_view, R.id.textView, trades);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Intent intent = new Intent(BookTrade.this, BookTrade_ViewPost.class);
                        intent.putExtra("key", tradeKeys.get(position));
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Trades.getRef().addListenerForSingleValueEvent(tradeValueEventListener);
    }
}