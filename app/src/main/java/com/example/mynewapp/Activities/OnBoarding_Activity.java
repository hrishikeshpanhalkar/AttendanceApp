package com.example.mynewapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mynewapp.Adapters.IntroAdapter;
import com.example.mynewapp.R;

public class OnBoarding_Activity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dotsLayout;
    Button get_started_btn;
    TextView[] dots;
    Animation animation;
    int currentPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_on_boarding_);

        viewPager=findViewById(R.id.slider);
        dotsLayout=findViewById(R.id.dots);
        get_started_btn=(Button)findViewById(R.id.get_started_btn) ;

        IntroAdapter adapter=new IntroAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
        get_started_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OnBoarding_Activity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void skip(View view){
        Intent intent=new Intent(OnBoarding_Activity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void next(View view){
        viewPager.setCurrentItem(currentPosition + 1);
    }
    private void addDots(int position){
        dots=new TextView[3];
        dotsLayout.removeAllViews();
        for(int i=0;i<dots.length;i++){
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if(dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
    ViewPager.OnPageChangeListener changeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPosition=position;
            if(position==0){
                get_started_btn.setVisibility(View.INVISIBLE);
            }else if(position==1){
                get_started_btn.setVisibility(View.INVISIBLE);
            }else {
                animation= AnimationUtils.loadAnimation(OnBoarding_Activity.this,R.anim.side_animation);
                get_started_btn.setAnimation(animation);
                get_started_btn.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
