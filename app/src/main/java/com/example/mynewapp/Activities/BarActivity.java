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

import com.example.mynewapp.Adapters.Utils;
import com.example.mynewapp.Model.Subject;
import com.example.mynewapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class BarActivity extends AppCompatActivity {
    AutoCompleteTextView Course, Semester,Subject,Rollno;
    Button Nextbtn;
    ArrayList<String> spinnerDataList2, spinnerData,spinnerData1,spinnerDataList;
    ArrayAdapter<String> adapter2, ada,ada1,adapter;
    DatabaseReference reff,reff1,reference;
    int noOf = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bar);
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        reference = FirebaseDatabase.getInstance().getReference().child("Subject");
        Nextbtn = (Button) findViewById(R.id.chart_btn);
        Course = (AutoCompleteTextView) findViewById(R.id.chart_course);
        Semester = (AutoCompleteTextView) findViewById(R.id.chart_semester);
        Subject = (AutoCompleteTextView) findViewById(R.id.chart_subject);
        Rollno = (AutoCompleteTextView) findViewById(R.id.chart_rollno);
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
                                adapter2 = new ArrayAdapter<String>(BarActivity.this,
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
                Semester.setText("");
                Subject.setText("");
                Rollno.setText("");
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
                        ada = new ArrayAdapter<String>(BarActivity.this,
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
        Semester.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subject.setText("");
                Rollno.setText("");
                final String semesterValue = Semester.getText().toString();
                String courseValue = Course.getText().toString();
                reff1 = FirebaseDatabase.getInstance().getReference().child("Student").child(courseValue).child(semesterValue);
                Query query = reff1.orderByChild("course").equalTo(courseValue);
                spinnerData1 = new ArrayList<String>();
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String rollno = ds.child("rollno").getValue(String.class);
                            spinnerData1.add(rollno);
                        }
                        if (ada1 != null) {
                            ada1.clear();
                        }
                        ada1 = new ArrayAdapter<String>(BarActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinnerData1);
                        Rollno.setAdapter(ada1);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                query.addListenerForSingleValueEvent(valueEventListener);
                Query query1 = reference.child(courseValue).child(semesterValue).orderByChild("course").equalTo(courseValue);
                spinnerDataList = new ArrayList<String>();
                ValueEventListener valueEventListener1 = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String sem = ds.child("semester").getValue(String.class);
                            if(sem.equals(semesterValue)){
                                spinnerDataList.add(ds.child("name").getValue(String.class));
                            }
                        }
                        Collections.sort(spinnerDataList);
                        if (adapter != null) {
                            adapter.clear();
                        }
                        adapter = new ArrayAdapter<String>(BarActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinnerDataList);
                        Subject.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                query1.addListenerForSingleValueEvent(valueEventListener1);
            }
        });
        Nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Course.getText().toString().equals("")) {
                    Course.setError("Please Select Course!");
                } else if (Semester.getText().toString().equals("")) {
                    Semester.setError("Please Select Semester!");
                    Course.setError(null);
                } else if (Subject.getText().toString().equals("")) {
                    Subject.setError("Please Select Subject!");
                    Semester.setError(null);
                }else if (Rollno.getText().toString().equals("")) {
                    Rollno.setError("Please Select Rollno!");
                    Subject.setError(null);
                } else {
                    Rollno.setError(null);
                    Intent intent = new Intent(BarActivity.this, BarChartActivity.class);
                    String rollno = Rollno.getText().toString();
                    String course = Course.getText().toString();
                    String semester = Semester.getText().toString();
                    String subject = Subject.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("Rollno", rollno);
                    bundle.putString("Course", course);
                    bundle.putString("Semester", semester);
                    bundle.putString("Subject", subject);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
}
