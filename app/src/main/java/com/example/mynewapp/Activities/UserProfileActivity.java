package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynewapp.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView FullName,BirthDate,Gender;
    TextInputLayout Fullname,Email,CourseName,Semisterno,Phone;
    ImageButton Back;

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference();
    private DatabaseReference first=databaseReference.child("Registration");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_profile);
        Back=(ImageButton) findViewById(R.id.back);
        circleImageView=(CircleImageView)findViewById(R.id.profile_image);
        FullName=(TextView)findViewById(R.id.User_fullName);
        BirthDate=(TextView)findViewById(R.id.user_birthdate_label);
        Gender=(TextView)findViewById(R.id.user_gender_label);
        Fullname=(TextInputLayout)findViewById(R.id.fullname);
        Email=(TextInputLayout)findViewById(R.id.user_email);
        CourseName=(TextInputLayout)findViewById(R.id.course_name);
        Semisterno=(TextInputLayout)findViewById(R.id.semisterno);
        Phone=(TextInputLayout)findViewById(R.id.user_phone);
        Bundle bundle = getIntent().getExtras();
        final String email = bundle.getString("Email");
        Query query = first.orderByChild("email").equalTo(email);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String link=ds.child("imageURL").getValue(String.class);
                    Picasso.get().load(link).into(circleImageView);
                    FullName.setText(ds.child("name").getValue(String.class));
                    Fullname.getEditText().setText(ds.child("name").getValue(String.class));
                    BirthDate.setText(ds.child("birthDate").getValue(String.class));
                    Gender.setText(ds.child("gender").getValue(String.class));
                    Email.getEditText().setText(ds.child("email").getValue(String.class));
                    Phone.getEditText().setText(ds.child("phone").getValue(String.class));
                    CourseName.getEditText().setText(ds.child("course").getValue(String.class));
                    Semisterno.getEditText().setText(ds.child("semister").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        query.addListenerForSingleValueEvent(valueEventListener);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfileActivity.this,UserHomeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Email",email);
                intent.putExtras(bundle);
                Pair[] pairs=new Pair[1];
                pairs[0]=new Pair<View,String>(Back,"login_User");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(UserProfileActivity.this,pairs);
                    startActivity(intent,options.toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });
    }
}
