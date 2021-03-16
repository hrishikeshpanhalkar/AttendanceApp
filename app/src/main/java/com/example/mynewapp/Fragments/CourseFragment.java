package com.example.mynewapp.Fragments;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mynewapp.Activities.CourseActivity;
import com.example.mynewapp.Activities.Subject_Delete1_Activity;
import com.example.mynewapp.Activities.Subject_Delete_Activity;
import com.example.mynewapp.Activities.Subject_Add_Activity;
import com.example.mynewapp.R;

public class CourseFragment extends Fragment {
    Button Addbtn, Updatebtn, Deletebtn;
    RelativeLayout Addlayout, Updatelayout, Deletelayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);
        Addbtn = (Button) view.findViewById(R.id.addcoursebtn);
        Updatebtn = (Button) view.findViewById(R.id.updatecoursebtn);
        Deletebtn = (Button) view.findViewById(R.id.deletecoursebtn);
        Addlayout = (RelativeLayout) view.findViewById(R.id.addcourse_layout);
        Updatelayout = (RelativeLayout) view.findViewById(R.id.updatecourse_layout);
        Deletelayout = (RelativeLayout) view.findViewById(R.id.deletecourse_layout);

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(Addbtn, "coursefragment");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(), pairs);
                    v.getContext().startActivity(intent, options.toBundle());
                } else {
                    v.getContext().startActivity(intent);
                }
            }
        });
        Updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Subject_Add_Activity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(Updatebtn, "coursefragment");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(), pairs);
                    v.getContext().startActivity(intent, options.toBundle());
                } else {
                    v.getContext().startActivity(intent);
                }
            }
        });
        Deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Subject_Delete1_Activity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(Deletebtn, "coursefragment");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(), pairs);
                    v.getContext().startActivity(intent, options.toBundle());
                } else {
                    v.getContext().startActivity(intent);
                }
            }
        });
        return view;
    }

}