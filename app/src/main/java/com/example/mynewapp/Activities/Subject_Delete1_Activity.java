package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.mynewapp.Adapters.Utils;
import com.example.mynewapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Subject_Delete1_Activity extends AppCompatActivity {
    AutoCompleteTextView Course, Semester;
    Button Nextbtn;
    ArrayList<String> spinnerDataList2, spinnerData;
    ArrayAdapter<String> adapter2, ada;
    DatabaseReference reff;
    int noOf = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delete__subject);
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        Nextbtn = (Button) findViewById(R.id.subject_delete_btn);
        Course = (AutoCompleteTextView) findViewById(R.id.subject_delete_course);
        Semester = (AutoCompleteTextView) findViewById(R.id.subject_delete_semester);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    noOf = (int) dataSnapshot.getChildrenCount();
                    spinnerDataList2 = new ArrayList<String>();
                    for (int i = 1; i <= noOf; i++) {
                        reff.child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String sem = dataSnapshot.child("name").getValue().toString();
                                spinnerDataList2.add(sem);
                                spinnerDataList2 = Utils.removeDuplicatesFromList(spinnerDataList2);
                                adapter2 = new ArrayAdapter<String>(Subject_Delete1_Activity.this,
                                        android.R.layout.simple_spinner_dropdown_item, spinnerDataList2);
                                Course.setAdapter(adapter2);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String courseValue = Course.getText().toString();
                Query query = reff.orderByChild("name").equalTo(courseValue);
                spinnerData = new ArrayList<String>();
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name2 = ds.child("sem").getValue(String.class);
                            spinnerData.add(name2);
                        }
                        Collections.sort(spinnerData);
                        if (ada != null) {
                            ada.clear();
                        }
                        ada = new ArrayAdapter<String>(Subject_Delete1_Activity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinnerData);
                        Semester.setAdapter(ada);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        });

        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Course.getText().equals("")) {
                    Toast.makeText(Subject_Delete1_Activity.this, "Please Select Course!", Toast.LENGTH_SHORT).show();
                } else if (Semester.getText().equals("")) {
                    Toast.makeText(Subject_Delete1_Activity.this, "Please Select Semister!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Subject_Delete1_Activity.this, Subject_Delete_Activity.class);
                    String course = Course.getText().toString();
                    String semester = Semester.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("Course", course);
                    bundle.putString("Semester", semester);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}

