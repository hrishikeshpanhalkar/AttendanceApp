package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;


import com.example.mynewapp.Fragments.StudentFragment;
import com.example.mynewapp.Fragments.UserFragment;
import com.example.mynewapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;



import de.hdodenhof.circleimageview.CircleImageView;

public class UserHomeActivity extends AppCompatActivity {
    CircleImageView User_acccount;
    ImageButton Back;
    ChipNavigationBar chipNavigationBar;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference();
    private DatabaseReference first=databaseReference.child("Registration");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_home);

        chipNavigationBar = (ChipNavigationBar) findViewById(R.id.bottom_nav_menu_user);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_attendance,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_user,new UserFragment()).commit();
        bottomMenu();
        User_acccount=(CircleImageView) findViewById(R.id.user_account);
        Back=(ImageButton) findViewById(R.id.user_home_back);
        Bundle bundle=getIntent().getExtras();
        final String email=bundle.getString("Email");
        Query query = first.orderByChild("email").equalTo(email);
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String link=ds.child("imageURL").getValue(String.class);
                    Picasso.get().load(link).into(User_acccount);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        query.addListenerForSingleValueEvent(valueEventListener);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserHomeActivity.this);
                alertDialog.setTitle("Leave application?");
                alertDialog.setMessage("Are you sure you want to leave the application?");
                alertDialog.setIcon(R.drawable.ic_warning_);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent=new Intent(UserHomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        });
        User_acccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserHomeActivity.this, UserProfileActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("Email", email);
                intent.putExtras(bundle1);
                Pair[] pair=new Pair[1];
                pair[0]=new Pair<View,String>(User_acccount,"transition_profile_picture");
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(UserHomeActivity.this,pair);
                    startActivity(intent,options.toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });
    }
    private void bottomMenu(){
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment=null;
                switch (i){
                    case R.id.bottom_nav_student:
                        fragment=new StudentFragment();
                        break;
                    case R.id.bottom_nav_attendance:
                        fragment=new UserFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_user,fragment).commit();
            }
        });
    }
    public void onBackPressed(){
        backButtonHandler();
        return;
    }
    public void backButtonHandler(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserHomeActivity.this);
        alertDialog.setTitle("Leave application?");
        alertDialog.setMessage("Are you sure you want to leave the application?");
        alertDialog.setIcon(R.drawable.ic_warning_);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent=new Intent(UserHomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}

