package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.http.SslCertificate;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynewapp.Model.Student_Update;
import com.example.mynewapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Student_Update2_Activity extends AppCompatActivity {
    ImageButton Home, Back;
    Button Update;
    TextInputLayout Rollno, Name, Email, Phone;
    AutoCompleteTextView Gender;
    String rollno, Course, Semester;
    DatabaseReference databaseReference;
    TextView FullName, Label_Email;
    String DfullName, DPhone, DGender, DEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student__update2);
        Back = (ImageButton) findViewById(R.id.student2_backbtn);
        Home = (ImageButton) findViewById(R.id.back_to_home_button);
        Update = (Button) findViewById(R.id.student_update_data_btn);
        Rollno = (TextInputLayout) findViewById(R.id.student_rollno_update);
        Name = (TextInputLayout) findViewById(R.id.student_fullname_update);
        Email = (TextInputLayout) findViewById(R.id.student_email_update);
        Phone = (TextInputLayout) findViewById(R.id.student_mobile_update);
        Gender = (AutoCompleteTextView) findViewById(R.id.student_gender_update);
        FullName = (TextView) findViewById(R.id.student_update_name);
        Label_Email = (TextView) findViewById(R.id.email_label);
        Intent intent = getIntent();
        Name.requestFocus();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, GENDER_LIST);
        Gender.setAdapter(adapter);
        if (intent.getExtras() != null) {
            Student_Update student_update = (Student_Update) intent.getSerializableExtra("Email");
            rollno = student_update.getRollno();
            Course = intent.getStringExtra("Course");
            Semester = intent.getStringExtra("Semester");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Student").child(Course).child(Semester);
        Query query = databaseReference.orderByChild("rollno").equalTo(rollno);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Rollno.getEditText().setText(ds.child("rollno").getValue(String.class));
                    DfullName =ds.child("name").getValue(String.class);
                    FullName.setText(ds.child("name").getValue(String.class));
                    Name.getEditText().setText(ds.child("name").getValue(String.class));
                    DEmail=ds.child("email").getValue(String.class);
                    Label_Email.setText(ds.child("email").getValue(String.class));
                    Email.getEditText().setText(ds.child("email").getValue(String.class));
                    DPhone=ds.child("mobile").getValue(String.class);
                    Phone.getEditText().setText(ds.child("mobile").getValue(String.class));
                    DGender=ds.child("gender").getValue(String.class);
                    Gender.setText(ds.child("gender").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Student_Update2_Activity.this, Student_Update_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Course", Course);
                bundle.putString("Semester", Semester);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Student_Update2_Activity.this, Home1Activity.class));
                finish();
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update.setVisibility(View.INVISIBLE);
                if (Name.getEditText().getText().toString().equals("") || Email.getEditText().getText().toString().equals("")
                        || Phone.getEditText().getText().toString().equals("") || Gender.getText().toString().equals("")) {
                    Toast.makeText(Student_Update2_Activity.this, "Please Fill the details!", Toast.LENGTH_LONG).show();
                    Update.setVisibility(View.VISIBLE);
                } else {
                    if (isNameChanged() || isEmailChanged() || isGenderChanged() || isPhoneChanged()) {
                        Toast.makeText(Student_Update2_Activity.this, "Data has been Updated!", Toast.LENGTH_LONG).show();
                        Update.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(Student_Update2_Activity.this, "Data is same and cannot be updated!", Toast.LENGTH_LONG).show();
                        Update.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private boolean isNameChanged() {
        if (!DfullName.equals(Name.getEditText().getText().toString())) {
            databaseReference.child(Rollno.getEditText().getText().toString()).child("name").setValue(Name.getEditText().getText().toString());
            DfullName = Name.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isEmailChanged() {
        if (!DEmail.equals(Email.getEditText().getText().toString())) {
            databaseReference.child(Rollno.getEditText().getText().toString()).child("email").setValue(Email.getEditText().getText().toString());
            DEmail = Email.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isPhoneChanged() {
        if (!DPhone.equals(Phone.getEditText().getText().toString())) {
            databaseReference.child(Rollno.getEditText().getText().toString()).child("mobile").setValue(Phone.getEditText().getText().toString());
            DPhone = Phone.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isGenderChanged() {
        if (!DGender.equals(Gender.getText().toString())) {
            databaseReference.child(Rollno.getEditText().getText().toString()).child("gender").setValue(Gender.getText().toString());
            DGender = Gender.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private static final String[] GENDER_LIST = new String[]{
            "Male", "Female", "Other"
    };
    public void onBackPressed() {
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        Intent intent = new Intent(Student_Update2_Activity.this, Student_Update_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Course", Course);
        bundle.putString("Semester", Semester);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
