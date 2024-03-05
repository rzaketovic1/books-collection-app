package com.example.booksapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookListAct extends AppCompatActivity {

    ListView bookList;
    Button btnReturn;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        bookList = findViewById(R.id.listViewBooks);
        btnReturn = findViewById(R.id.btnReturn);

        sharedPref = getSharedPreferences("BookPrefs", Context.MODE_PRIVATE);

        String category=getIntent().getExtras().getString("ITEM");

        ArrayList<Book> books = BookManager.getInstance(this).getBooks();
        ArrayList<Book> sameCategoryBooks = new ArrayList<>();

        for (Book book: books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                sameCategoryBooks.add(book);
            }
        }

        final BookListAdapter adapter=new BookListAdapter(this,sameCategoryBooks);
        bookList.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(Color.BLUE);
            }
        });

    }
}
