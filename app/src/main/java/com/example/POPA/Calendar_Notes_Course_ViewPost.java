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

public class Calendar_Notes_Course_ViewPost extends AppCompatActivity {

    ImageButton backButton;
    TextView username;
    ImageButton pic;
    TextView title;
    TextView text;
    TextView likes;
    ImageButton likeButton;
    EditText answerInput;
    Button submitButton;

    //question key
    String key;

    //post author key
    String noteCreatorKey;

    //course id
    String id;

    //has the user liked this post or not
    boolean liked;

    LinearLayout mparent;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_notes_course_view_post);

        username = findViewById(R.id.username);
        pic = findViewById(R.id.pic);
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        likes = findViewById(R.id.likes);
        likeButton = findViewById(R.id.likeButton);
        answerInput = findViewById(R.id.answerInput);
        submitButton = findViewById(R.id.submitButton);

        key = getIntent().getStringExtra("key");
        id = getIntent().getStringExtra("id");

        loadComments();

        //check if the user likes this post or not
        ValueEventListener usersLikedValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("usersLiked").child(Key.key).getValue() == null)
                    liked = false;
                else {
                    liked = true;
                    likeButton.setImageResource(getResources().getIdentifier("like", "drawable", getPackageName()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Notes.getRef().child("CID" + id).child(key).addListenerForSingleValueEvent(usersLikedValueEventListener);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        //note's details
        ValueEventListener noteValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                noteCreatorKey = dataSnapshot.child("author").getValue().toString();
                title.setText("Σημειώσεις από " + dataSnapshot.child("date").getValue().toString() + ".");
                text.setText(dataSnapshot.child("text").getValue().toString());
                if (dataSnapshot.child("likes").getValue().toString().equals("1"))
                    likes.setText("Αυτή η ανάρτηση αρέσει σε " + dataSnapshot.child("likes").getValue().toString() + " χρήστη.");
                else
                    likes.setText("Αυτή η ανάρτηση αρέσει σε " + dataSnapshot.child("likes").getValue().toString() + " χρήστες.");

                //noteCreator's details
                ValueEventListener noteCreatorValueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        username.setText(dataSnapshot.child("username").getValue().toString());
                        pic.setImageResource(getResources().getIdentifier("icon" + dataSnapshot.child("pic").getValue(), "drawable", getPackageName()));

                        //view the noteCreator's profile
                        username.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(Calendar_Notes_Course_ViewPost.this, ViewProfile.class);
                                intent.putExtra("key", noteCreatorKey);
                                startActivity(intent);

                            }
                        });

                        //view the noteCreator's profile
                        pic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(Calendar_Notes_Course_ViewPost.this, ViewProfile.class);
                                intent.putExtra("key", noteCreatorKey);
                                startActivity(intent);

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                Users.getRef().child(noteCreatorKey).addListenerForSingleValueEvent(noteCreatorValueEventListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Notes.getRef().child("CID" + id).child(key).addListenerForSingleValueEvent(noteValueEventListener);

        likeButton = findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (liked) {
                    likeButton.setImageResource(getResources().getIdentifier("unlike", "drawable", getPackageName()));
                    liked = false;

                    //decrease noteCreator's credit count
                    ValueEventListener noteCreatorValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int i = Integer.parseInt(dataSnapshot.child("credits").getValue().toString());
                            i--;
                            dataSnapshot.child("credits").getRef().setValue(i);
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    Users.getRef().child(noteCreatorKey).addListenerForSingleValueEvent(noteCreatorValueEventListener);

                    //remove user from the users that liked this post
                    ValueEventListener usersLikedValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.child("usersLiked").child(Key.key).getRef().removeValue();
                            int i = Integer.valueOf(dataSnapshot.child("likes").getValue().toString());
                            i--;
                            dataSnapshot.child("likes").getRef().setValue(i);
                            if (i == 1)
                                likes.setText("Αυτή η ανάρτηση αρέσει σε " + i + " χρήστη.");
                            else
                                likes.setText("Αυτή η ανάρτηση αρέσει σε " + i + " χρήστες.");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    Notes.getRef().child("CID" + id).child(key).addListenerForSingleValueEvent(usersLikedValueEventListener);
                }
                else {
                    likeButton.setImageResource(getResources().getIdentifier("like", "drawable", getPackageName()));
                    liked = true;

                    //increase noteCreator's credit count
                    ValueEventListener noteCreatorValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int i = Integer.parseInt(dataSnapshot.child("credits").getValue().toString());
                            i++;
                            dataSnapshot.child("credits").getRef().setValue(i);
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    Users.getRef().child(noteCreatorKey).addListenerForSingleValueEvent(noteCreatorValueEventListener);

                    //add user to the users that liked this post
                    ValueEventListener usersLikedValueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.child("usersLiked").child(Key.key).getRef().setValue("");
                            int i = Integer.valueOf(dataSnapshot.child("likes").getValue().toString());
                            i++;
                            dataSnapshot.child("likes").getRef().setValue(i);
                            if (i == 1)
                                likes.setText("Αυτή η ανάρτηση αρέσει σε " + i + " χρήστη.");
                            else
                                likes.setText("Αυτή η ανάρτηση αρέσει σε " + i + " χρήστες.");
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    Notes.getRef().child("CID" + id).child(key).addListenerForSingleValueEvent(usersLikedValueEventListener);
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String answer = answerInput.getText().toString();
                if (!answer.equals("")) {

                    QnA_Comments comment = new QnA_Comments(Key.key, answer, 0);
                    DatabaseReference ref = Notes.getRef().child("CID" + id).child(key).child("answers").push();
                    ref.setValue(comment);
                    Toast.makeText(getApplicationContext(), "Επτιτυχής ανάρτηση σχόλιου.", Toast.LENGTH_SHORT).show();

                    mparent = findViewById(R.id.linearLayout);
                    layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                    View myView = layoutInflater.inflate(R.layout.qna_comment, null, false);
                    TextView username = myView.findViewById(R.id.username);
                    ImageButton pic = myView.findViewById(R.id.pic);
                    TextView text = myView.findViewById(R.id.text);
                    TextView likes = myView.findViewById(R.id.likes);
                    ImageButton likeButton = myView.findViewById(R.id.likeButton);

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

                                    Intent intent = new Intent(Calendar_Notes_Course_ViewPost.this, ViewProfile.class);
                                    intent.putExtra("key", Key.key);
                                    startActivity(intent);

                                }
                            });

                            //view the answerCreator's profile
                            pic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(Calendar_Notes_Course_ViewPost.this, ViewProfile.class);
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
                    likes.setText("Αυτή η απάντηση αρέσει σε 0 χρήστες.");

                    mparent.addView(myView);

                    likeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ValueEventListener usersLikedValueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    //check if the user likes this post or not
                                    if (dataSnapshot.child("usersLiked").child(Key.key).getValue() == null) {

                                        //increase user's credit count
                                        ValueEventListener postCreatorValueEventListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                int i = Integer.parseInt(dataSnapshot.child("credits").getValue().toString());
                                                i++;
                                                dataSnapshot.child("credits").getRef().setValue(i);
                                            }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        };
                                        Users.getRef().child(Key.key).addListenerForSingleValueEvent(postCreatorValueEventListener);

                                        likeButton.setImageResource(getResources().getIdentifier("like", "drawable", getPackageName()));
                                        int i = Integer.valueOf(dataSnapshot.child("likes").getValue().toString());
                                        dataSnapshot.child("usersLiked").child(Key.key).getRef().setValue("");
                                        i++;
                                        dataSnapshot.child("likes").getRef().setValue(i);
                                        if (i == 1)
                                            likes.setText("Αυτή η απάντηση αρέσει σε " + i + " χρήστη.");
                                        else
                                            likes.setText("Αυτή η απάντηση αρέσει σε " + i + " χρήστες.");
                                    }
                                    else {

                                        //decrease user's credit count
                                        ValueEventListener postCreatorValueEventListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                int i = Integer.parseInt(dataSnapshot.child("credits").getValue().toString());
                                                i--;
                                                dataSnapshot.child("credits").getRef().setValue(i);
                                            }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        };
                                        Users.getRef().child(Key.key).addListenerForSingleValueEvent(postCreatorValueEventListener);

                                        likeButton.setImageResource(getResources().getIdentifier("nolike", "drawable", getPackageName()));
                                        int i = Integer.valueOf(dataSnapshot.child("likes").getValue().toString());
                                        dataSnapshot.child("usersLiked").child(Key.key).getRef().removeValue();
                                        i--;
                                        dataSnapshot.child("likes").getRef().setValue(i);
                                        if (i == 1)
                                            likes.setText("Αυτή η απάντηση αρέσει σε " + i + " χρήστη.");
                                        else
                                            likes.setText("Αυτή η απάντηση αρέσει σε " + i + " χρήστες.");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            Notes.getRef().child("CID" + id).child(key).child("answers").child(ref.getKey()).addListenerForSingleValueEvent(usersLikedValueEventListener);

                        }
                    });

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

                    View myView = layoutInflater.inflate(R.layout.qna_comment, null, false);
                    TextView username = myView.findViewById(R.id.username);
                    ImageButton pic = myView.findViewById(R.id.pic);
                    TextView text = myView.findViewById(R.id.text);
                    TextView likes = myView.findViewById(R.id.likes);
                    ImageButton likeButton = myView.findViewById(R.id.likeButton);

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

                                    Intent intent = new Intent(Calendar_Notes_Course_ViewPost.this, ViewProfile.class);
                                    intent.putExtra("key", answerCreatorKey);
                                    startActivity(intent);

                                }
                            });

                            //view the answerCreator's profile
                            pic.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(Calendar_Notes_Course_ViewPost.this, ViewProfile.class);
                                    intent.putExtra("key", answerCreatorKey);
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
                    if (child.child("likes").getValue().toString().equals("1"))
                        likes.setText("Αυτή η απάντηση αρέσει σε " + child.child("likes").getValue().toString() + " χρήστη.");
                    else
                        likes.setText("Αυτή η απάντηση αρέσει σε " + child.child("likes").getValue().toString() + " χρήστες.");

                    mparent.addView(myView);

                    //check if the user likes this post or not
                    if (child.child("usersLiked").child(Key.key).getValue() == null)
                        likeButton.setImageResource(getResources().getIdentifier("nolike", "drawable", getPackageName()));
                    else
                        likeButton.setImageResource(getResources().getIdentifier("like", "drawable", getPackageName()));

                    likeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ValueEventListener usersLikedValueEventListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    //check if the user likes this post or not
                                    if (dataSnapshot.child("usersLiked").child(Key.key).getValue() == null) {

                                        //increase answerCreator's credit count
                                        ValueEventListener postCreatorValueEventListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                int i = Integer.parseInt(dataSnapshot.child("credits").getValue().toString());
                                                i++;
                                                dataSnapshot.child("credits").getRef().setValue(i);
                                            }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        };
                                        Users.getRef().child(answerCreatorKey).addListenerForSingleValueEvent(postCreatorValueEventListener);

                                        likeButton.setImageResource(getResources().getIdentifier("like", "drawable", getPackageName()));
                                        int i = Integer.valueOf(dataSnapshot.child("likes").getValue().toString());
                                        dataSnapshot.child("usersLiked").child(Key.key).getRef().setValue("");
                                        i++;
                                        dataSnapshot.child("likes").getRef().setValue(i);
                                        if (i == 1)
                                            likes.setText("Αυτή η απάντηση αρέσει σε " + i + " χρήστη.");
                                        else
                                            likes.setText("Αυτή η απάντηση αρέσει σε " + i + " χρήστες.");
                                    }
                                    else {

                                        //decrease answerCreator's credit count
                                        ValueEventListener postCreatorValueEventListener = new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                int i = Integer.parseInt(dataSnapshot.child("credits").getValue().toString());
                                                i--;
                                                dataSnapshot.child("credits").getRef().setValue(i);
                                            }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        };
                                        Users.getRef().child(answerCreatorKey).addListenerForSingleValueEvent(postCreatorValueEventListener);

                                        likeButton.setImageResource(getResources().getIdentifier("nolike", "drawable", getPackageName()));
                                        int i = Integer.valueOf(dataSnapshot.child("likes").getValue().toString());
                                        dataSnapshot.child("usersLiked").child(Key.key).getRef().removeValue();
                                        i--;
                                        dataSnapshot.child("likes").getRef().setValue(i);
                                        if (i == 1)
                                            likes.setText("Αυτή η απάντηση αρέσει σε " + i + " χρήστη.");
                                        else
                                            likes.setText("Αυτή η απάντηση αρέσει σε " + i + " χρήστες.");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            };
                            Notes.getRef().child("CID" + id).child(key).child("answers").child(child.getKey()).addListenerForSingleValueEvent(usersLikedValueEventListener);

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        Notes.getRef().child("CID" + id).child(key).child("answers").addListenerForSingleValueEvent(answersLikedValueEventListener);
    }
}