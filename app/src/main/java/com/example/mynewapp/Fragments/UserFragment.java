package com.example.mynewapp.Fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mynewapp.Activities.BarActivity;
import com.example.mynewapp.Activities.DateActivity;
import com.example.mynewapp.Activities.ShowAttendanceActivity;
import com.example.mynewapp.R;

public class UserFragment extends Fragment {
    Button Attendance,Attendanceview,BarChart;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        Attendance = (Button) view.findViewById(R.id.attendence);
        Attendanceview = (Button) view.findViewById(R.id.attendenceupdate);
        BarChart = (Button) view.findViewById(R.id.barchartbtn);
        Attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DateActivity.class);
                Pair[] pair=new Pair[1];
                pair[0]=new Pair<View,String>(Attendance,"attendanceTransition");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(),pair);
                    startActivity(intent,options.toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });
        Attendanceview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShowAttendanceActivity.class);
                Pair[] pair=new Pair[1];
                pair[0]=new Pair<View,String>(Attendanceview,"login_User1");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(),pair);
                    startActivity(intent,options.toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });
        BarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BarActivity.class);
                Pair[] pair=new Pair[1];
                pair[0]=new Pair<View,String>(BarChart,"bartransition");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(),pair);
                    startActivity(intent,options.toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}
