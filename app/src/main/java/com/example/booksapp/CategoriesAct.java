package com.example.booksapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoriesAct extends AppCompatActivity implements FragmentInteraction {

    public static final String CATEGORY_EXTRAS = "category_extras";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        ListsFragment listsFragment = new ListsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(CATEGORY_EXTRAS, true);
        listsFragment.setArguments(bundle);
        replaceFragmentWithoutBackstack(listsFragment);

    }

    private void replaceFragmentWithoutBackstack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLists, fragment)
                .commit();
    }


    @Override
    public void OpenBookFragment(String category) {

    }

    @Override
    public void OpenAuthorFragment() {

    }
}