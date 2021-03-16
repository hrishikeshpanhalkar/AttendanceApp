package com.example.mynewapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynewapp.Adapters.AttendanceAdapter;
import com.example.mynewapp.Model.Attendance;
import com.example.mynewapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class AttendanceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Attendance> list;
    private AttendanceAdapter attendanceAdapter;
    Button button;
    DatabaseReference databaseReference;
    Attendance attendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_attendance);
        Bundle bundle = getIntent().getExtras();
        final String attendanceDate = bundle.getString("Date");
        final String Course = bundle.getString("Course");
        final String Semester = bundle.getString("Semister");
        final String Subject = bundle.getString("Subject");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        attendance = new Attendance();
        button = (Button) findViewById(R.id.attendence);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference itemsRef = rootRef.child("Student");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Attendance");
        Query query = itemsRef.child(Course).child(Semester).orderByChild("course").equalTo(Course);
        list = new ArrayList<Attendance>();
        attendanceAdapter = new AttendanceAdapter(AttendanceActivity.this, list);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String semister = ds.child("semister").getValue(String.class);
                    if (semister.equals(Semester)) {
                        Attendance u = ds.getValue(Attendance.class);
                        list.add(u);
                    }
                }
                attendanceAdapter = new AttendanceAdapter(AttendanceActivity.this, list);
                if (attendanceAdapter.getItemCount() == 0) {
                    Toast.makeText(AttendanceActivity.this, "Students are not added!", Toast.LENGTH_SHORT).show();
                    button.setVisibility(View.INVISIBLE);
                } else {
                    recyclerView.setAdapter(attendanceAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
        attendanceAdapter.presentList.clear();
        attendanceAdapter.absentList.clear();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int presentStudent = AttendanceAdapter.presentList.size();
                int absentStudent = AttendanceAdapter.absentList.size();
                int totalStudent = list.size();
                if ((presentStudent + absentStudent) != totalStudent) {
                    Toast.makeText(getApplicationContext(), "Please Select all Student!", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog dialog = new AlertDialog.Builder(AttendanceActivity.this).create();
                    View view = LayoutInflater.from(AttendanceActivity.this).inflate(R.layout.custom_dialog_layout, null);
                    TextView total, present, absent, date;
                    Button cancleBtn, confirmBtn;
                    date = view.findViewById(R.id.attendance_date);
                    total = view.findViewById(R.id.total_attendance);
                    present = view.findViewById(R.id.present_attendance);
                    absent = view.findViewById(R.id.absent_attendance);
                    cancleBtn = view.findViewById(R.id.cancel_button_attendance);
                    confirmBtn = view.findViewById(R.id.confirm_button_attendance);
                    present.setText(String.valueOf(presentStudent));
                    absent.setText(String.valueOf(absentStudent));
                    total.setText(String.valueOf(totalStudent));
                    date.setText(attendanceDate);
                    dialog.setCancelable(true);
                    dialog.setView(view);
                    cancleBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    confirmBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Collections.sort(attendanceAdapter.attendanceList, new Comparator<Attendance>() {
                                @Override
                                public int compare(Attendance lhs, Attendance rhs) {
                                    return lhs.getRollno().compareTo(rhs.getRollno());
                                }
                            });
                            attendance = new Attendance();
                            databaseReference.child(Course).child(Semester).child(Subject).child(attendanceDate).setValue(attendanceAdapter.attendanceList);
                            AttendanceAdapter.presentList.clear();
                            AttendanceAdapter.absentList.clear();
                            attendanceAdapter.attendanceList.clear();
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "Attendance data added successfully ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AttendanceActivity.this, DateActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.show();
                }
            }
        });
    }
}
