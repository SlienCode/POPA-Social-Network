package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class BookTrade_ViewPost extends AppCompatActivity {

    ImageButton backButton;
    TextView username;
    ImageButton pic;
    TextView title;
    EditText answerInput;
    Button submitButton;

    //trade key
    String key;

    //post author key
    String postCreatorKey;

    //has the user liked this post or not
    boolean liked;

    LinearLayout mparent;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_trade_view_post);

        username = findViewById(R.id.username);
        pic = findViewById(R.id.pic);
        title = findViewById(R.id.title);
        answerInput = findViewById(R.id.answerInput);
        submitButton = findViewById(R.id.submitButton);

        key = getIntent().getStringExtra("key");

        loadComments();

        //trade's details
        ValueEventListener questionValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postCreatorKey = dataSnapshot.child("author").getValue().toString();

                if (dataSnapshot.child("type").getValue().equals("deal"))
                    title.setText("Ανταλλάσσω " + dataSnapshot.child("offer").getValue().toString() + " για " + dataSnapshot.child("want").getValue().toString() + ".");
                else if (dataSnapshot.child("type").getValue().equals("buy"))
                    title.setText("Αναζητώ " + dataSnapshot.child("want").getValue().toString() + ".");
                else
                    title.setText("Πουλάω " + dataSnapshot.child("offer").getValue().toString() + " για " + dataSnapshot.child("want").getValue().toString() + "€.");

                //postCreator's details
                ValueEventListener postCreatorValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        username.setText(dataSnapshot.child("username").getValue().toString());
                        pic.setImageResource(getResources().getIdentifier("icon" + dataSnapshot.child("pic").getValue(), "drawable", getPackageName()));

                        //view the postCreator's profile
                        username.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(BookTrade_ViewPost.this, ViewProfile.class);
                                intent.putExtra("key", postCreatorKey);
                                startActivity(intent);

                            }
                        });

                        //view the postCreator's profile
                        pic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(BookTrade_ViewPost.this, ViewProfile.class);
                                intent.putExtra("key", postCreatorKey);
                                startActivity(intent);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                Users.getRef().child(postCreatorKey).addListenerForSingleValueEvent(postCreatorValueEventListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Trades.getRef().child(key).addListenerForSingleValueEvent(questionValueEventListener);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String answer = answerInput.getText().toString();
                if (!answer.equals("")) {

                    BookTrade_Comments comment = new BookTrade_Comments(Key.key, answer);
                    DatabaseReference ref = Trades.getRef().child(key).child("answers").push();
                    ref.setValue(comment);
                    Toast.makeText(getApplicationContext(), "Επτιτυχής ανάρτηση απάντησης.", Toast.LENGTH_SHORT).show();

                    mparent = findViewById(R.id.linearLayout);
                    layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                    View myView = layoutInflater.inflate(R.layout.book_trade_comment, null, false);
                    TextView username = myView.findViewById(R.id.username);
                    ImageButton pic = myView.findViewById(R.id.pic);
                    TextView text = myView.findViewById(R.id.text);

                    //answerCreator's details
                    ValueEventListener answerCreatorValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            username.setText(dataSnapshot.child("username").getValue().toString());
                            pic.setImageResource(getResources().getIdentifier("icon" + dataSnapshot.child("pic").getValue(), "drawable", getPackageName()));

                            //increase answerCreator's post count
                            int i = Integer.parseInt(dataSnapshot.child("posts").getValue().toString());
                            i++;
                            dataSnapshot.child("posts").getRef().setValue(i);

                            //view the answerCreator's profile
                            username.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(BookTrade_ViewPost.this, ViewProfile.class);
                                    intent.putExtra("key", Key.key);
                                    startActivity(intent);

                                }
                            });

                            //view the answerCreator's profile
                            pic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(BookTrade_ViewPost.this, ViewProfile.class);
                                    intent.putExtra("key", Key.key);
                                    startActivity(intent);

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    Users.getRef().child(Key.key).addListenerForSingleValueEvent(answerCreatorValueEventListener);

                    text.setText(answer);

                    mparent.addView(myView);

                    //empty the answer editText
                    answerInput.getText().clear();
                    answerInput.clearFocus();
                }
            }
        });
    }

    //load the comments
    protected void loadComments() {

        mparent = findViewById(R.id.linearLayout);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        //load all the answers
        ValueEventListener answersLikedValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    View myView = layoutInflater.inflate(R.layout.book_trade_comment, null, false);
                    TextView username = myView.findViewById(R.id.username);
                    ImageButton pic = myView.findViewById(R.id.pic);
                    TextView text = myView.findViewById(R.id.text);

                    String answerCreatorKey = child.child("author").getValue().toString();

                    //answerCreator's details
                    ValueEventListener answerCreatorValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            username.setText(dataSnapshot.child("username").getValue().toString());
                            pic.setImageResource(getResources().getIdentifier("icon" + dataSnapshot.child("pic").getValue(), "drawable", getPackageName()));

                            //view the answerCreator's profile
                            username.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(BookTrade_ViewPost.this, ViewProfile.class);
                                    intent.putExtra("key", answerCreatorKey);
                                    startActivity(intent);

                                }
                            });

                            //view the answerCreator's profile
                            pic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(BookTrade_ViewPost.this, ViewProfile.class);
                                    intent.putExtra("key", answerCreatorKey);
                                    //intent.putExtra("loaded", false);
                                    startActivity(intent);

                                }
                            });

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    Users.getRef().child(answerCreatorKey).addListenerForSingleValueEvent(answerCreatorValueEventListener);

                    text.setText(child.child("text").getValue().toString());

                    mparent.addView(myView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Trades.getRef().child(key).child("answers").addListenerForSingleValueEvent(answersLikedValueEventListener);
    }
}