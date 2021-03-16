package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.mynewapp.Model.Member;
import com.example.mynewapp.R;
import com.example.mynewapp.Model.Student;
import com.example.mynewapp.Adapters.Utils;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class StudentActivity extends AppCompatActivity {
    private Button buttonStud;
    private TextInputLayout sRollno, sName, sEmail,sMobile;
    private AutoCompleteTextView sCourse, sSemister,sGender;
    FirebaseAuth firebaseAuth;
    DatabaseReference reff,reff1,rootRef,itemsRef;
    ArrayList<String> spinnerData,spinnerDataList3;
    ArrayAdapter<String> ada,adapter3;
    int noOf=0;
    Student student;
    Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student);
        sRollno=(TextInputLayout) findViewById(R.id.srollno);
        sName=(TextInputLayout) findViewById(R.id.sname);
        sEmail=(TextInputLayout) findViewById(R.id.semail);
        sMobile=(TextInputLayout) findViewById(R.id.smobile);
        sGender=(AutoCompleteTextView) findViewById(R.id.sgender);
        sCourse=(AutoCompleteTextView) findViewById(R.id.scourse);
        sSemister=(AutoCompleteTextView) findViewById(R.id.ssemister);
        buttonStud=(Button)findViewById(R.id.btnstud);
        firebaseAuth = FirebaseAuth.getInstance();
        reff1= FirebaseDatabase.getInstance().getReference().child("Student");
        reff= FirebaseDatabase.getInstance().getReference().child("Member");
        student=new Student();
        member=new Member();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, GENDER_LIST);
        sGender.setAdapter(adapter);
        rootRef = FirebaseDatabase.getInstance().getReference();
        itemsRef = rootRef.child("Member");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    noOf=(int)dataSnapshot.getChildrenCount();
                    spinnerDataList3 = new ArrayList<String>();
                    for(int i=1;i<=noOf;i++) {
                        reff.child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String coursename = dataSnapshot.child("name").getValue().toString();
                                spinnerDataList3.add(coursename);
                                spinnerDataList3 = Utils.removeDuplicatesFromList(spinnerDataList3);
                                adapter3 = new ArrayAdapter<String>(StudentActivity.this,
                                        R.layout.dropdown_item, spinnerDataList3);
                                sCourse.setAdapter(adapter3);
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
        sCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sSemister.setText("");
                String courseValue=sCourse.getText().toString();
                Query query = itemsRef.orderByChild("name").equalTo(courseValue);
                spinnerData = new ArrayList<String>();
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name2 = ds.child("sem").getValue(String.class);
                            spinnerData.add(name2);
                        }
                        Collections.sort(spinnerData);
                        if (ada != null) {
                            ada.clear();
                        }
                        ada = new ArrayAdapter<String>(StudentActivity.this,
                                R.layout.dropdown_item, spinnerData);
                        sSemister.setAdapter(ada);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                };
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        buttonStud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semail = sEmail.getEditText().getText().toString().trim();
                if(sRollno.getEditText().getText().toString().equals("")){
                    sRollno.setError("Fill the Roll No!");
                }else if (sName.getEditText().getText().toString().equals("")){
                    sName.setError("Fill the Name!");
                    sRollno.setError(null);
                }else if (sEmail.getEditText().getText().toString().equals("")) {
                    sEmail.setError("Fill the Email!");
                    sName.setError(null);
                } else if (!Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
                    sEmail.setError("Invalid email address!");
                }else if (sMobile.getEditText().getText().toString().equals("")) {
                    sMobile.setError("Fill the Phone No!");
                    sEmail.setError(null);
                }else if(sMobile.getEditText().getText().toString().length()<10){
                    sMobile.setError("Please Enter Correct Phone Number!");
                } else if (sGender.getText().toString().equals("")) {
                    sGender.setError("Fill the Gender!");
                    sMobile.setError(null);
                }else if (sCourse.getText().toString().equals("")) {
                    sCourse.setError("Please Select Course!");
                    sGender.setError(null);
                } else if (sSemister.getText().toString().equals("")) {
                    sSemister.setError("Please Select Semister");
                    sCourse.setError(null);
                } else{
                    sSemister.setError(null);
                    final String rollno=sRollno.getEditText().getText().toString().trim();
                    final String name=sName.getEditText().getText().toString().trim();
                    final String email=sEmail.getEditText().getText().toString().trim();
                    final String mobile=sMobile.getEditText().getText().toString().trim();
                    final String gender=sGender.getText().toString().trim();
                    final String course=sCourse.getText().toString().trim();
                    final String sem=sSemister.getText().toString().trim();
                    Query query = reff1.child(course).child(sem).orderByChild("course").equalTo(course);
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            outer:
                            for(int i=1;i<2;i++) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String semister = ds.child("semister").getValue(String.class);
                                    String roll=ds.child("rollno").getValue(String.class);
                                    if (semister.equals(sem) && roll.equals(rollno)) {
                                        Toast.makeText(StudentActivity.this,"Roll No already present!",Toast.LENGTH_LONG).show();
                                        break outer;
                                    }
                                }
                                student.setRollno(rollno);
                                student.setName(name);
                                student.setEmail(email);
                                student.setMobile(mobile);
                                student.setGender(gender);
                                student.setCourse(course);
                                student.setSemister(sem);
                                reff1.child(course).child(sem).child(rollno).setValue(student);
                                Toast.makeText(StudentActivity.this, "Student Details Added Successfully!", Toast.LENGTH_LONG).show();
                                sName.getEditText().setText("");
                                sRollno.getEditText().setText("");
                                sEmail.getEditText().setText("");
                                sCourse.setText("");
                                sSemister.setText("");
                                sMobile.getEditText().setText("");
                                sGender.setText("");
                                sRollno.requestFocus();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    query.addListenerForSingleValueEvent(valueEventListener);
                }
            }
        });
    }
    private static final String[] GENDER_LIST = new String[]{
            "Male", "Female", "Other"
    };
}
