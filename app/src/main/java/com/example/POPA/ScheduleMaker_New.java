package com.example.POPA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class ScheduleMaker_New extends AppCompatActivity {

    ImageButton backButton;
    ListView listView;
    ListView listView2;

    Button submitButton;

    //list of all the courses
    ArrayList<String> courses;

    //list of the selected courses
    ArrayList<String> myCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_maker_new);

        listView = findViewById(R.id.listView);
        listView2 = findViewById(R.id.listView2);

        submitButton = findViewById(R.id.submitButton);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        HashMap<String, String[]> table = (HashMap<String, String[]>)getIntent().getSerializableExtra("map");

        //table keys -> array list
        Set<String> keySet = table.keySet();

        courses = new ArrayList<String>(keySet);
        myCourses = new ArrayList<>();

        Collections.sort(courses);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ScheduleMaker_New.this, R.layout.list_view, R.id.textView, courses);
        listView.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(ScheduleMaker_New.this, R.layout.list_view, R.id.textView, myCourses);
        listView2.setAdapter(arrayAdapter2);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                myCourses.add(courses.get(position));
                courses.remove(position);

                Collections.sort(courses);
                Collections.sort(myCourses);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ScheduleMaker_New.this, R.layout.list_view, R.id.textView, courses);
                listView.setAdapter(arrayAdapter);

                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(ScheduleMaker_New.this, R.layout.list_view, R.id.textView, myCourses);
                listView2.setAdapter(arrayAdapter2);
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                courses.add(myCourses.get(position));
                myCourses.remove(position);

                Collections.sort(courses);
                Collections.sort(myCourses);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ScheduleMaker_New.this, R.layout.list_view, R.id.textView, courses);
                listView.setAdapter(arrayAdapter);

                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(ScheduleMaker_New.this, R.layout.list_view, R.id.textView, myCourses);
                listView2.setAdapter(arrayAdapter2);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(myCourses.size() == 0)) {

                    //key: course, value: 5-value Array for every weekday
                    HashMap<String, String[]> tableGive = new HashMap<>();
                    for (String iter: myCourses)
                        tableGive.put(iter, table.get(iter));

                    //go to ScheduleMaker_View
                    Intent intent = new Intent(ScheduleMaker_New.this, ScheduleMaker_View.class);
                    intent.putExtra("map", tableGive);
                    startActivity(intent);
                }
            }
        });
    }
}