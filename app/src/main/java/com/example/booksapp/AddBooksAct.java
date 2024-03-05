package com.example.booksapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.ArrayList;

public class AddBooksAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbooks);

        FragmentManager fragmentManager = AddBooksAct.this.getSupportFragmentManager();
        AddBooksFragment addBooksFragment;
        addBooksFragment = (AddBooksFragment) fragmentManager.findFragmentById(R.id.fragmentAddBooks);

        if(addBooksFragment == null)
            addBooksFragment = new AddBooksFragment();

        fragmentManager.beginTransaction().replace(R.id.fragmentAddBooks, addBooksFragment).commit();

    }

}
