package com.example.mynewapp.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mynewapp.Fragments.OnboardingFragment1;
import com.example.mynewapp.Fragments.OnboardingFragment2;
import com.example.mynewapp.Fragments.OnboardingFragment3;

public class IntroAdapter extends FragmentPagerAdapter {
    public IntroAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OnboardingFragment1();
            case 1:
                return new OnboardingFragment2();
            case 2:
                return new OnboardingFragment3();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
