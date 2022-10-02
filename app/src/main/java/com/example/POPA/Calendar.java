package com.example.POPA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

public class Calendar extends AppCompatActivity {

    CalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendar = findViewById(R.id.calendar);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Intent data = new Intent();
                data.putExtra("year", String.valueOf(year));
                data.putExtra("month", String.valueOf(month+1));
                data.putExtra("dayOfMonth", String.valueOf(dayOfMonth));
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}