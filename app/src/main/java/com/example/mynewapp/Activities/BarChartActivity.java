package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mynewapp.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


public class BarChartActivity extends AppCompatActivity {
    CombinedChart barChart;
    ArrayList<Entry> Entries;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList<BarEntry> barEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);
        Bundle bundle = getIntent().getExtras();
        String rollno = bundle.getString("Rollno");
        int roll=Integer.parseInt(rollno);
        final int rollno_int=roll-1;
        final String Course = bundle.getString("Course");
        final String Semester = bundle.getString("Semester");
        final String Subject = bundle.getString("Subject");
        barChart = (CombinedChart) findViewById(R.id.barchart);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(Course).child(Semester).child(Subject);
        Entries = new ArrayList<>();
        barEntries = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> xAxisLables = new ArrayList<String>();
                ArrayList<Date> xyz = new ArrayList<Date>();
                ArrayList<String> labels=new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String substring = dataSnapshot.getKey().substring(3);
                    if (!xAxisLables.contains(substring)) {
                        xAxisLables.add(substring);
                    }
                }
                try {
                    for (String abc1 : xAxisLables) {
                        Date date = new SimpleDateFormat("MMMM-yyyy", Locale.ENGLISH).parse(abc1);
                        xyz.add(date);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for(Date date: xyz){
                    labels.add(new SimpleDateFormat("MM-yyyy",Locale.ENGLISH).format(date));
                }
                Collections.sort(xAxisLables, new Comparator<String>() {
                    DateFormat f = new SimpleDateFormat("MMMM-yyyy");
                    @Override
                    public int compare(String o1, String o2) {
                        try {
                            return f.parse(o1).compareTo(f.parse(o2));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.sort(labels, new Comparator<String>() {
                    DateFormat f = new SimpleDateFormat("MM-yyyy");
                    @Override
                    public int compare(String o1, String o2) {
                        try {
                            return f.parse(o1).compareTo(f.parse(o2));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Entries.add(new Entry(0,0));
                for (int i = 0; i < xAxisLables.size(); i++) {
                    int count = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.getKey().endsWith(xAxisLables.get(i))) {
                            String attendance = dataSnapshot.child(String.valueOf(rollno_int)).child("attendance").getValue(String.class);
                            if (attendance.equals("present")) {
                                count++;
                            }
                        }
                    }
                    Entries.add(new Entry(i+1, count));
                    barEntries.add(new BarEntry(i+1, count));
                }
                Entries.add(new Entry(xAxisLables.size()+1,0));
                LineDataSet set = new LineDataSet(Entries, "");
                set.setDrawValues(false);
                barDataSet = new BarDataSet(barEntries, "Data Set");
                barData = new BarData(barDataSet);
                set.setFillAlpha(110);
                set.setColor(Color.RED);
                set.setLineWidth(3f);
                set.setValueTextColor(Color.GREEN);
                ArrayList<ILineDataSet> dataSets=new ArrayList<>();
                dataSets.add(set);
                LineData lineData=new LineData(dataSets);
                CombinedData data=new CombinedData();
                data.setData(barData);
                data.setData(lineData);
                barChart.setData(data);
                XAxis xAxis=barChart.getXAxis();
                labels.add(0,"");
                barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                xAxis.setGranularity(1);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                barChart.animateY(2000);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
