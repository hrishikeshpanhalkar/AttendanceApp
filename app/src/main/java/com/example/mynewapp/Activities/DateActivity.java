package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.mynewapp.Adapters.Utils;
import com.example.mynewapp.Model.User;
import com.example.mynewapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class DateActivity extends AppCompatActivity {
    AutoCompleteTextView Coursename, Semister,Subject;
    Button Nextbtn;
    MaterialButton datebtn;
    ArrayList<String> spinnerDataList2, spinnerData,spinnerDataList;
    ArrayAdapter<String> adapter2, ada,adapter;
    DatabaseReference reff,reference;
    int noOf = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_date);
        datebtn = (MaterialButton) findViewById(R.id.todayDate);
        Nextbtn = (Button) findViewById(R.id.selectdatebutton);
        Coursename = (AutoCompleteTextView) findViewById(R.id.attendence_course);
        Semister = (AutoCompleteTextView) findViewById(R.id.attendence_semister);
        Subject = (AutoCompleteTextView) findViewById(R.id.attendence_subject);
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        reference = FirebaseDatabase.getInstance().getReference().child("Subject");
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy");
                        calendar.set(year, month, dayOfMonth);
                        String dateString = sdf.format(calendar.getTime());
                        datebtn.setText(dateString);
                    }
                }, year, month, day
                );
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

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
                                adapter2 = new ArrayAdapter<String>(DateActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item, spinnerDataList2);
                                Coursename.setAdapter(adapter2);
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
        Coursename.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Semister.setText("");
                Subject.setText("");
                String courseValue = Coursename.getText().toString();
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
                        ada = new ArrayAdapter<String>(DateActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinnerData);
                        Semister.setAdapter(ada);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        Semister.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subject.setText("");
                String courseValue = Coursename.getText().toString();
                final String semesterValue = Semister.getText().toString();
                Query query = reference.child(courseValue).child(semesterValue).orderByChild("course").equalTo(courseValue);
                spinnerDataList = new ArrayList<String>();
                ValueEventListener valueEventListener = new ValueEventListener() {
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
                        adapter = new ArrayAdapter<String>(DateActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinnerDataList);
                        Subject.setAdapter(adapter);
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
                if (Coursename.getText().toString().equals("")) {
                    Coursename.setError("Please Select Course!");
                } else if (Semister.getText().toString().equals("")) {
                    Semister.setError("Please Select Semister!");
                    Coursename.setError(null);
                } else if (Subject.getText().toString().equals("")) {
                    Subject.setError("Please Select Subject!");
                    Semister.setError(null);
                }else if (datebtn.getText().toString().equals("")) {
                    datebtn.setError("Please Select Date!");
                    Subject.setError(null);
                } else {
                    datebtn.setError(null);
                    Intent intent = new Intent(DateActivity.this, AttendanceActivity.class);
                    String date = datebtn.getText().toString();
                    String course = Coursename.getText().toString();
                    String semister = Semister.getText().toString();
                    String subject = Subject.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("Date", date);
                    bundle.putString("Course", course);
                    bundle.putString("Semister", semister);
                    bundle.putString("Subject", subject);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
