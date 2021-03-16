package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mynewapp.Model.Member;
import com.example.mynewapp.R;
import com.example.mynewapp.Model.Registration;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class RegistrationActivity extends AppCompatActivity {
    private Button button;
    private TextInputLayout Name, Password, Email,Phone;
    DatabaseReference databaseReference1, databaseReference;
    Registration registration;
    Member member;
    AutoCompleteTextView Course, Semister;
    int noOfchild = 0;
    ImageView logoimage;
    TextView textView;
    ArrayList<String> spinnerDataList, spinnerData;
    ArrayAdapter<String> adapter, ada;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);
        circleImageView = (CircleImageView) findViewById(R.id.layout_one);
        Name = (TextInputLayout) findViewById(R.id.name);
        Password = (TextInputLayout) findViewById(R.id.password);
        Email = (TextInputLayout) findViewById(R.id.email);
        Phone = (TextInputLayout) findViewById(R.id.phone);
        Course = (AutoCompleteTextView) findViewById(R.id.course);
        Semister = (AutoCompleteTextView) findViewById(R.id.Semister);
        button = (Button) findViewById(R.id.btn3);
        logoimage = (ImageView) findViewById(R.id.teacherlogo);
        textView = (TextView) findViewById(R.id.teachertext);
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Registration");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Member");
        registration = new Registration();
        member = new Member();
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
                                adapter = new ArrayAdapter<String>(RegistrationActivity.this,
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
                Semister.setText("");
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
                        ada = new ArrayAdapter<String>(RegistrationActivity.this,
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = Password.getEditText().getText().toString().trim();
                String email = Email.getEditText().getText().toString().trim();
                if ((Name.getEditText().getText().toString().equals(""))) {
                    Name.setError("Fill the Name!");
                } else if (Email.getEditText().getText().toString().equals("")) {
                    Email.setError("Fill the Email!");
                    Name.setError(null);
                }   else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Email.setError("Invalid email address!");
                }else if (Password.getEditText().getText().toString().equals("")) {
                    Password.setError("Fill the Password!");
                    Email.setError(null);
                } else if (Password.getEditText().getText().length() < 6) {
                    Password.setError("Password must be 6 letter long!");
                }else if (Phone.getEditText().getText().toString().equals("")) {
                    Phone.setError("Fill the Phone!");
                    Password.setError(null);
                } else if (Phone.getEditText().getText().length()<10) {
                    Phone.setError("Please Enter Correct phone number");
                } else if (Course.getText().toString().equals("")) {
                    Course.setError("Select Course!");
                    Phone.setError(null);
                } else if (Semister.getText().toString().equals("")) {
                    Semister.setError("Select Semister!");
                    Course.setError(null);
                } else {
                    Semister.setError(null);
                    Intent intent = new Intent(RegistrationActivity.this, Registration2Activity.class);
                    String fullname = Name.getEditText().getText().toString();
                    String phone = Phone.getEditText().getText().toString();
                    String course = Course.getText().toString();
                    String semister = Semister.getText().toString();
                    Bundle bundle = new Bundle();
                    bundle.putString("FullName", fullname);
                    bundle.putString("Email", email);
                    bundle.putString("Password", password);
                    bundle.putString("Phone", phone);
                    bundle.putString("Course", course);
                    bundle.putString("Semister", semister);
                    intent.putExtras(bundle);
                    Pair[] pairs = new Pair[4];
                    pairs[0] = new Pair<View, String>(logoimage, "transition_logo_image");
                    pairs[1] = new Pair<View, String>(textView, "transition_title_text");
                    pairs[2] = new Pair<View, String>(button, "transition_next_btn");
                    pairs[3] = new Pair<View, String>(circleImageView, "transition_layout_number");
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegistrationActivity.this, pairs);
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                }
            }
        });
    }

}
