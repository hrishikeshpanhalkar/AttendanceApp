package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.mynewapp.Adapters.Utils;
import com.example.mynewapp.Model.Subject;
import com.example.mynewapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class Subject_Add_Activity extends AppCompatActivity {
    private TextInputLayout Code,Name;
    Button button;
    DatabaseReference databaseReference,databaseReference1;
    AutoCompleteTextView Course, Semester;
    int noOfchild = 0;
    ArrayList<String> spinnerDataList, spinnerData;
    ArrayAdapter<String> adapter, ada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subject_add);
        Code = (TextInputLayout) findViewById(R.id.subcode);
        Name = (TextInputLayout) findViewById(R.id.subname);
        Course = (AutoCompleteTextView) findViewById(R.id.course_subject);
        Semester = (AutoCompleteTextView) findViewById(R.id.subjectSemester);
        button = (Button) findViewById(R.id.subadd);
        final Subject subject=new Subject();
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Subject");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Member");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    noOfchild = (int) dataSnapshot.getChildrenCount();
                    spinnerDataList = new ArrayList<String>();
                    for (int i = 1; i <= noOfchild; i++) {
                        databaseReference.child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String coursename = dataSnapshot.child("name").getValue().toString();
                                spinnerDataList.add(coursename);
                                spinnerDataList = Utils.removeDuplicatesFromList(spinnerDataList);
                                adapter = new ArrayAdapter<String>(Subject_Add_Activity.this,
                                        android.R.layout.simple_spinner_dropdown_item, spinnerDataList);
                                Course.setAdapter(adapter);
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
                String courseValue = Course.getText().toString();
                Query query = databaseReference.orderByChild("name").equalTo(courseValue);
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
                        ada = new ArrayAdapter<String>(Subject_Add_Activity.this,
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String subcode=Code.getEditText().getText().toString();
                final String subname=Name.getEditText().getText().toString();
                final String course=Course.getText().toString();
                final String sem=Semester.getText().toString();
                if(subcode.equals("")){
                    Code.setError("Please Fill the Subject Code!");
                }else if(subname.equals("")){
                    Name.setError("Please Fill the Subject Name!");
                    Code.setError(null);
                }else if(course.equals("")) {
                    Course.setError("Please Select the Course Name!");
                    Name.setError(null);
                }else if(sem.equals("")) {
                    Semester.setError("Please Select the Semester Name!");
                    Course.setError(null);
                }else {
                    Semester.setError(null);
                    Query query = databaseReference1.child(course).child(sem).orderByChild("course").equalTo(course);
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            outer:
                            for(int i=1;i<2;i++) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String semester = ds.child("semester").getValue(String.class);
                                    String code=ds.child("code").getValue(String.class);
                                    if (semester.equals(sem) && code.equals(subcode)) {
                                        Toast.makeText(Subject_Add_Activity.this,"Subject already Exist!",Toast.LENGTH_SHORT).show();
                                        break outer;
                                    }
                                }
                                subject.setCode(subcode);
                                subject.setName(subname);
                                subject.setCourse(course);
                                subject.setSemester(sem);
                                databaseReference1.child(course).child(sem).child(subcode).setValue(subject);
                                Toast.makeText(getApplicationContext(),"Data Added Successfully!",Toast.LENGTH_SHORT).show();
                                Code.getEditText().setText("");
                                Name.getEditText().setText("");
                                Course.setText("");
                                Semester.setText("");
                                Code.requestFocus();
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
}
