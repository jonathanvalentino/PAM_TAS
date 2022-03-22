package com.jonathan.pam_tas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Bottom navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavBar);

        bottomNavigationView.setSelectedItemId(R.id.calendar_menu);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.calendar_menu:
                        return true;
                    case R.id.bookmark_menu:
                        startActivity(new Intent(getApplicationContext(), BookmarkActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home_menu:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.trip_menu:
                        startActivity(new Intent(getApplicationContext(), TripActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}