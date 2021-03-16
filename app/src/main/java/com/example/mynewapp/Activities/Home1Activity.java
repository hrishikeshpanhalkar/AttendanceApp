package com.example.mynewapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.mynewapp.Fragments.CourseFragment;
import com.example.mynewapp.Fragments.StudentFragment;
import com.example.mynewapp.Fragments.TeacherFragment;
import com.example.mynewapp.R;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Home1Activity extends AppCompatActivity {
    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home1);

        chipNavigationBar = (ChipNavigationBar) findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_teacher,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TeacherFragment()).commit();
        bottomMenu();
    }
    private void bottomMenu(){
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment=null;
                switch (i){
                    case R.id.bottom_nav_course:
                        fragment=new CourseFragment();
                        break;
                    case R.id.bottom_nav_teacher:
                        fragment=new TeacherFragment();
                        break;
                    case R.id.bottom_nav_student:
                        fragment=new StudentFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
    }
    public void onBackPressed(){
        //Display alert message when back button has been pressed
        backButtonHandler();
        return;
    }
    public void backButtonHandler(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home1Activity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Leave application?");
        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to leave the application?");
        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_warning_);
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent=new Intent(Home1Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
}