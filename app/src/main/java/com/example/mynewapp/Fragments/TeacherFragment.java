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

import com.example.mynewapp.Activities.RegistrationActivity;
import com.example.mynewapp.Activities.Teacher_Delete_Activity;
import com.example.mynewapp.Activities.Teacher_Update_1Activity;
import com.example.mynewapp.R;

public class TeacherFragment extends Fragment {
    Button Addbtn, Updatebtn, Deletebtn;
    RelativeLayout Addlayout, Updatelayout, Deletelayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);
        Addbtn = (Button) view.findViewById(R.id.addteacherbtn);
        Updatebtn = (Button) view.findViewById(R.id.updateteacherbtn);
        Deletebtn = (Button) view.findViewById(R.id.deleteteacherbtn);
        Addlayout = (RelativeLayout) view.findViewById(R.id.addteacher_layout);
        Updatelayout = (RelativeLayout) view.findViewById(R.id.updateteacher_layout);
        Deletelayout = (RelativeLayout) view.findViewById(R.id.deleteteacher_layout);

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegistrationActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(Addbtn, "teacherfragment");
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
                Intent intent = new Intent(v.getContext(), Teacher_Update_1Activity.class);
                v.getContext().startActivity(intent);
            }
        });
        Deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Teacher_Delete_Activity.class);
                v.getContext().startActivity(intent);
            }
        });
        return view;
    }
}