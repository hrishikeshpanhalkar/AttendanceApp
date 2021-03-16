package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.mynewapp.Model.Member;
import com.example.mynewapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class CourseActivity extends AppCompatActivity {
    TextInputLayout Name,Duration,Seat,textInputLayout;
    AutoCompleteTextView Sem;
    Button Btn3,demo;
    DatabaseReference databaseReference;
    Member member;
    long maxiid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_course);
        Name=findViewById(R.id.coursename);
        Duration=findViewById(R.id.duration);
        textInputLayout=findViewById(R.id.text_input_layout);
        Sem=(AutoCompleteTextView) findViewById(R.id.spinner2);
        Seat=findViewById(R.id.seat);
        Btn3=(Button)findViewById(R.id.courseadd);
        demo=(Button)findViewById(R.id.demo123);
        ArrayAdapter<String> semAdapter =new ArrayAdapter<String>(CourseActivity.this,
                R.layout.dropdown_item,getResources().getStringArray(R.array.Sem));
        Sem.setAdapter(semAdapter);
        member=new Member();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Member");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxiid=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Name.getEditText().getText().toString().equals(""))) {
                    Name.setError("Fill the Name!");
                } else if (Duration.getEditText().getText().toString().equals("")) {
                    Duration.setError("Fill the details!");
                    Name.setError(null);
                } else if (Seat.getEditText().getText().toString().equals("")) {
                    Seat.setError("Fill the details!");
                    Duration.setError(null);
                } else if (Sem.getText().toString().equals("")) {
                    Sem.setError("Fill the details!");
                    Seat.setError(null);
                }else {
                    final String courseValue = Name.getEditText().getText().toString();
                    final String xyz=Sem.getText().toString();
                    Sem.setError(null);
                    Query query = databaseReference.orderByChild("name").equalTo(courseValue);
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            outer:
                            for(int i=1;i<2;i++) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String name2 = ds.child("sem").getValue(String.class);
                                    if (name2.equals(xyz)) {
                                        Toast.makeText(CourseActivity.this, "Course name Already Exists!", Toast.LENGTH_LONG).show();
                                        break outer;
                                    }
                                }
                                String name = Name.getEditText().getText().toString().trim();
                                int duration = Integer.parseInt(Duration.getEditText().getText().toString().trim());
                                int seat = Integer.parseInt(Seat.getEditText().getText().toString().trim());
                                String sem = Sem.getText().toString();
                                member.setName(name);
                                member.setDuration(duration);
                                member.setSeat(seat);
                                member.setSem(sem);
                                databaseReference.child(String.valueOf(maxiid + 1)).setValue(member);
                                Toast.makeText(CourseActivity.this, "Data Added Successfully!", Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(CourseActivity.this, Home1Activity.class);
                                startActivity(intent);
                                Name.getEditText().setText("");
                                Duration.getEditText().setText("");
                                Seat.getEditText().setText("");
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
        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CourseActivity.this, DemoActivity.class);
                startActivity(intent);
            }
        });
    }
}

