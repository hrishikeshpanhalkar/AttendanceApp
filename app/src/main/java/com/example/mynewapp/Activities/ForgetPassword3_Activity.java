package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.mynewapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgetPassword3_Activity extends AppCompatActivity {
    Button nextbtn;
    TextView textView;
    String Email,Phone;
    PinView pinView;
    String CodeBySystem;
    ImageButton Close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_password3);
        nextbtn=(Button)findViewById(R.id.forget_password3);
        Close=(ImageButton) findViewById(R.id.forget_password3_close);
        textView=(TextView) findViewById(R.id.forget_password3_textview);
        pinView=(PinView) findViewById(R.id.pinview);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=pinView.getText().toString();
                if(!code.isEmpty()){
                    verifyCode(code);
                }else {
                    Toast.makeText(ForgetPassword3_Activity.this,"Please Enter Pin!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetPassword3_Activity.this,LoginActivity.class));
                finish();
            }
        });
        Email=getIntent().getStringExtra("Email");
        Phone=getIntent().getStringExtra("Phone");
        textView.setText(Phone);
        sendVerificationCodetoUser(Phone);
    }

    private void sendVerificationCodetoUser(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    CodeBySystem=s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code=phoneAuthCredential.getSmsCode();
                    if(code != null){
                        pinView.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(ForgetPassword3_Activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(CodeBySystem,code);
        signInWithPhoneAuthCredetail(credential);
    }
    private void signInWithPhoneAuthCredetail(PhoneAuthCredential credential) {
        final FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPassword3_Activity.this,"Verification Completed!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ForgetPassword3_Activity.this,ForgetPassword4_Activity.class);
                    intent.putExtra("Email",Email);
                    startActivity(intent);
                    finish();
                }else {
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(ForgetPassword3_Activity.this,"Verification Not Completed. Try Again!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
