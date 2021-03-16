package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mynewapp.Model.SessionManager;
import com.example.mynewapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class ForgetPassword4_Activity extends AppCompatActivity {
    Button nextbtn;
    TextInputLayout password, confirmPassword;
    String spassword, parentvalue;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password4);
        final String Email = getIntent().getStringExtra("Email");
        nextbtn = (Button) findViewById(R.id.forget_password4);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar=(ProgressBar)findViewById(R.id.password_update_prgressbar);
        progressBar.setVisibility(View.INVISIBLE);
        password = (TextInputLayout) findViewById(R.id.forget_password4_password);
        confirmPassword = (TextInputLayout) findViewById(R.id.forget_password4_Cpassword);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Password = password.getEditText().getText().toString();
                String ConfirmPassword = confirmPassword.getEditText().getText().toString();
                if (Password.equals("")) {
                    password.setError("Please Enter Password!");
                } else if (ConfirmPassword.equals("")) {
                    confirmPassword.setError("Please Enter Password");
                } else if (Password.length() < 6 || ConfirmPassword.length() < 6) {
                    Toast.makeText(ForgetPassword4_Activity.this, "Password is too short!", Toast.LENGTH_SHORT).show();
                } else {
                    if (Password.equals(ConfirmPassword)) {
                        progressBar.setVisibility(View.VISIBLE);
                        DatabaseReference databaseReference12 = FirebaseDatabase.getInstance().getReference().child("Registration");
                        Query query1 = databaseReference12.orderByChild("email").equalTo(Email);
                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    spassword = ds.child("password").getValue(String.class);
                                    parentvalue = ds.getKey();
                                    firebaseAuth.signInWithEmailAndPassword(Email,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                firebaseAuth.getCurrentUser().updatePassword(Password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Registration");
                                                            databaseReference.child(parentvalue).child("password").setValue(Password);
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            Toast.makeText(ForgetPassword4_Activity.this,"Password Updated Successfully!",Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(ForgetPassword4_Activity.this, ForgetPassword5_Activity.class));
                                                            finish();
                                                        }else {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            Toast.makeText(ForgetPassword4_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }else {
                                                Toast.makeText(ForgetPassword4_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                            }
                                        }
                                    });
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(ForgetPassword4_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        };
                        query1.addListenerForSingleValueEvent(valueEventListener);
                    } else {
                        confirmPassword.setError("Password doesn't match");
                    }

                }
            }
        });
    }
}