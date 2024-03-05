package com.example.booksapp;

import static com.example.booksapp.CategoriesAct.CATEGORY_EXTRAS;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AuthorListAdapter extends ArrayAdapter<Author> {

    private Activity activity;

    public void clearData() {
        clear();
        notifyDataSetChanged();
    }
    public AuthorListAdapter(Activity context, ArrayList<Author> authors) {
        super(context, R.layout.author_item, authors);
        this.activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.author_item, parent, false);
        }

        final Author author = getItem(position);


        TextView authorName = convertView.findViewById(R.id.textViewAuthorName);
        TextView numberOfBooks = convertView.findViewById(R.id.textViewNumberOfBooks);

        // Reset views to default state
        authorName.setText("");
        numberOfBooks.setText("");

        // Update views with new data
        if (author != null) {
            authorName.setText(author.getName());
            numberOfBooks.setText(String.valueOf(author.getNumberOfBooks()));
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if (context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;

                    BooksFragment booksFragment = new BooksFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("authorName", author.getName());
                    booksFragment.setArguments(bundle);

                    if (activity.getSupportFragmentManager() != null) {
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentLists, booksFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }
            }
        });

        return convertView;
    }
}
