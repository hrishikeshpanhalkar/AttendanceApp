package com.example.mynewapp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mynewapp.R;


public class OnboardingFragment3 extends Fragment {

    public OnboardingFragment3() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_blank_fragment3,container,false);
    }
}
