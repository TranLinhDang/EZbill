package com.example.ezbilllite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PowerLevel extends AppCompatActivity {
    TextView receiverPower;
    TextView receiverTime;
    TextView userEmailName;
    TextView payDate;

    double powerNum;
    double powerKw;

    private DBHandlerPower dbHandlerPower;

    PieChart pieChart;

    // variable for our bar chart
    BarChart barChart;

    // variable for our bar data.
    BarData barData;

    // variable for our bar data set.
    BarDataSet barDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;
    String userEmail;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power);

        if(user != null){
            userEmail = user.getEmail();
        }else{
            userEmail = "unknown@email.com";
        }

        receiverPower = (TextView) findViewById(R.id.powerlevel);
        receiverTime = (TextView) findViewById(R.id.timeelapsed);
        userEmailName = (TextView) findViewById(R.id.uName);
        payDate = (TextView) findViewById(R.id.paydate);

        Intent intent = getIntent();
        String str = intent.getStringExtra("powerlevel");
        System.out.println("STRING:" + str);
        String eTime = intent.getStringExtra("elapsed time");
        double d= Double.parseDouble(eTime);

        if(str.equals("F4 01 ")){
            powerNum = 500;
        } else if (str.equals("E8 03 ")) {
            powerNum = 1000;
        } else if (str.equals("D0 07 ")){
            powerNum = 2000;
        }

        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(currentTime);

        payDate.setText("Next pay date: 2022-12-31");

        powerKw = (powerNum * d) / 3600000;
        receiverPower.setText(Double.toString(powerKw));

//        receiverPower.setText("Power level " + Double.toString(powerNum));
//        receiverTime.setText("Time elapsed: " + eTime + " s");

//        powerKw = (powerNum * d) / 3600000;

        receiverTime.setText(strDate);

        userEmailName.setText(userEmail);

        dbHandlerPower = new DBHandlerPower(PowerLevel.this);


        String userName = userEmailName.getText().toString();
        String userDate = receiverTime.getText().toString();
        String userUsage = receiverPower.getText().toString();
        dbHandlerPower.addNewUsage(userName, userDate, userUsage);

        Toast.makeText(PowerLevel.this, "Usage has been added.", Toast.LENGTH_SHORT).show();



//        userEmailName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String userName = userEmailName.getText().toString();
//                String userDate = receiverTime.getText().toString();
//                String userUsage = receiverPower.getText().toString();
//
//                dbHandlerPower.addNewUsage(userName, userDate, userUsage);
//
//                Toast.makeText(PowerLevel.this, "Usage has been added.", Toast.LENGTH_SHORT).show();
//
//            }
//        });


        SQLiteDatabase db = dbHandlerPower.getReadableDatabase();
//
        Cursor cursorSensor = db.rawQuery("SELECT power FROM usage ", null);

        ArrayList<String> powerArrayList = new ArrayList<>();

        if(null != cursorSensor && cursorSensor.moveToFirst()){
            do {
                powerArrayList.add(cursorSensor.getString(0));
            } while(cursorSensor.moveToNext());
        }
        cursorSensor.close();

        barChart = findViewById(R.id.barchart);


//        pieChart = findViewById(R.id.activity_main_piechart);
//        setupPieChart();
//        loadPieChartData();

//        getBarEntries();

        barEntriesArrayList = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        for(int i = 0; i < powerArrayList.size();i++){
            barEntriesArrayList.add(new BarEntry((float) (i+1), Float.parseFloat(powerArrayList.get(i)) ));
        }


//        barEntriesArrayList.add(new BarEntry(2f, (float) powerKw));
//        barEntriesArrayList.add(new BarEntry(3f, 0));
//        barEntriesArrayList.add(new BarEntry(4f, 0));
//        barEntriesArrayList.add(new BarEntry(5f, 0));
//        barEntriesArrayList.add(new BarEntry(6f, 0));
//        barEntriesArrayList.add(new BarEntry(7f, 0));
//        barEntriesArrayList.add(new BarEntry(8f, 0));

        // creating a new bar data set.
        barDataSet = new BarDataSet(barEntriesArrayList, "User");

        // creating a new bar data and
        // passing our bar data set.
        barData = new BarData(barDataSet);

        // below line is to set data
        // to our bar chart.
        barChart.setData(barData);

        // adding color to our bar data set.
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(Color.BLACK);

        // setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);

    }

    public void getBarEntries() {
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntriesArrayList.add(new BarEntry(2f, (float) powerKw));
        barEntriesArrayList.add(new BarEntry(3f, 0));
        barEntriesArrayList.add(new BarEntry(4f, 0));
        barEntriesArrayList.add(new BarEntry(5f, 0));
        barEntriesArrayList.add(new BarEntry(6f, 0));
        barEntriesArrayList.add(new BarEntry(7f, 0));
        barEntriesArrayList.add(new BarEntry(8f, 0));
    }
}


