package com.example.ezbilllite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DashBoadActivity extends AppCompatActivity {

    TextView getPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_dashboard_activity);

        getPower = findViewById(R.id.powerTextView);
        Intent intent = getIntent();

        String powerStr = intent.getStringExtra("power_message");
        getPower.setText(powerStr);

    }
}