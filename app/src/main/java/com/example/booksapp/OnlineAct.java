package com.example.booksapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class OnlineAct extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);


        FragmentManager fragmentManager = OnlineAct.this.getSupportFragmentManager();
        OnlineFragment onlineFragment;
        onlineFragment = (OnlineFragment) fragmentManager.findFragmentById(R.id.fragmentOnline);

        if (onlineFragment == null) {
            onlineFragment = new OnlineFragment();
            fragmentManager.beginTransaction().replace(R.id.fragmentOnline, onlineFragment).commit();
        }
    }
}
