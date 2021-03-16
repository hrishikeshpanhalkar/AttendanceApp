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
import com.example.mynewapp.Activities.StudentActivity;
import com.example.mynewapp.Activities.Student_Delete1_Activity;
import com.example.mynewapp.Activities.Student_Delete_Activity;
import com.example.mynewapp.Activities.Student_Update1_Activity;
import com.example.mynewapp.Activities.Student_Update_Activity;
import com.example.mynewapp.R;

public class StudentFragment extends Fragment {
    Button Addbtn, Updatebtn, Deletebtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        Addbtn = (Button) view.findViewById(R.id.addstudentbtn);
        Updatebtn = (Button) view.findViewById(R.id.updatestudentbtn);
        Deletebtn = (Button) view.findViewById(R.id.deletestudentbtn);

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StudentActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(Addbtn, "login_User1");
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
                Intent intent = new Intent(v.getContext(), Student_Update1_Activity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(Updatebtn, "login_User1");
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
                Intent intent = new Intent(v.getContext(), Student_Delete1_Activity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(Deletebtn, "login_User1");
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