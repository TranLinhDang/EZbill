package com.example.ezbilllite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ActivityServices extends AppCompatActivity {
    Button btn;
    EditText macInput;
    private DBHandlerSensor dbHandlerSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_scan);


        btn = findViewById(R.id.buttonRegisterMAC);
        macInput = findViewById(R.id.editTextRegisterDevice);

        // creating a new dbhandler class
        // and passing our context to it.
        dbHandlerSensor = new DBHandlerSensor(ActivityServices.this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // below line is to get data from all edit text fields.
                String sensorName = macInput.getText().toString();

                // validating if the text fields are empty or not.
                if (sensorName.isEmpty()) {
                    Toast.makeText(ActivityServices.this, "Please enter sensor's MAC..", Toast.LENGTH_SHORT).show();
                    return;
                }

                // on below line we are calling a method to add new
                // course to sqlite data and pass all our values to it.
                dbHandlerSensor.addNewSensor(sensorName);

                // after adding the data we are displaying a toast message.
                Toast.makeText(ActivityServices.this, "Sensor has been added.", Toast.LENGTH_SHORT).show();
                macInput.setText("");
            }
        });

        //        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!macInput.getText().toString().isEmpty()){
//                    try{
//                        FileOutputStream fileOutputStream = openFileOutput("mytextfile.txt", Context.MODE_PRIVATE);
//                        OutputStreamWriter outputWriter = new OutputStreamWriter(fileOutputStream);
//                        outputWriter.write(macInput.getText().toString());
//                        outputWriter.close();
//                    } catch ( Exception e ) {
//                        e.printStackTrace ( ) ;
//                    }
//                }
//            }
//        });



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_scan){
                    Toast.makeText(ActivityServices.this, "Scan", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
                if (item.getItemId() == R.id.nav_character){
                    Toast.makeText(ActivityServices.this, "Scan", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), DashBoadActivity.class);
                    startActivity(i);
                }
                DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}
