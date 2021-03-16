package com.example.mynewapp.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mynewapp.R;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class DemoActivity extends AppCompatActivity {

    private CombinedChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_demo);
        chart = (CombinedChart) findViewById(R.id.barchart2);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        ArrayList<BarEntry> visitors = new ArrayList<>();
        visitors.add(new BarEntry(2014, 550));

        ArrayList<Entry> yValues = new ArrayList<>();
        yValues.add(new Entry(2013, 0));
        yValues.add(new Entry(2014, 550));
        yValues.add(new Entry(2015, 0));
        LineDataSet set1 = new LineDataSet(yValues, "Data Set 1");
        set1.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarDataSet barDataSet=new BarDataSet(visitors,"visitors");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);
        LineData linearData = new LineData(dataSets);
        BarData barData = new BarData(barDataSet);
        CombinedData data=new CombinedData();
        data.setData(linearData);
        data.setData(barData);
        chart.setData(data);
        chart.getDescription().setText("Bar Chart Example");
        chart.animateY(2000);
    }

}
