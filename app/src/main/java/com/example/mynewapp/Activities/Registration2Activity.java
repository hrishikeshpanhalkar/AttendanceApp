package com.example.mynewapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mynewapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration2Activity extends AppCompatActivity {
    DatePicker datePicker;
    Button Registrationbtn;
    RadioGroup radioGroupage;
    RadioButton radioButtonage;
    ImageView Teacherlogo;
    TextView Teachertext;
    String radioButtonValue;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration2);
        circleImageView=(CircleImageView)findViewById(R.id.layout_two);
        datePicker = (DatePicker) findViewById(R.id.datepicker);
        datePicker.setMaxDate(System.currentTimeMillis());
        Teacherlogo=(ImageView)findViewById(R.id.teacherlogo);
        Teachertext=(TextView)findViewById(R.id.teachertext);
        Registrationbtn = (Button) findViewById(R.id.registrationbtn);
        Bundle bundle = getIntent().getExtras();
        final String Name = bundle.getString("FullName");
        final String Email = bundle.getString("Email");
        final String Password = bundle.getString("Password");
        final String Phone = bundle.getString("Phone");
        final String Course = bundle.getString("Course");
        final String Semister = bundle.getString("Semister");
        Registrationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupage = (RadioGroup) findViewById(R.id.radiogroupage);
                int selectedId = radioGroupage.getCheckedRadioButtonId();
                radioButtonage = (RadioButton) findViewById(selectedId);
                radioButtonValue = radioButtonage.getText().toString();
                final String textdate = datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear();
                Intent intent=new Intent(Registration2Activity.this, Registration3Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("FullName", Name);
                bundle.putString("Email", Email);
                bundle.putString("Password", Password);
                bundle.putString("Phone", Phone);
                bundle.putString("Course",Course);
                bundle.putString("Semister", Semister);
                bundle.putString("BirthDate",textdate);
                bundle.putString("Gender",radioButtonValue);
                intent.putExtras(bundle);
                Pair[] pairs=new Pair[4];
                pairs[0]=new Pair<View,String>(Teacherlogo,"transition_logo_image");
                pairs[1]=new Pair<View,String>(Teachertext,"transition_title_text");
                pairs[2]=new Pair<View,String>(Registrationbtn,"transition_next_btn");
                pairs[3]=new Pair<View,String>(circleImageView,"transition_layout_number");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Registration2Activity.this,pairs);
                    startActivity(intent,options.toBundle());
                }else {
                    startActivity(intent);
                }
            }
        });
    }
}
