package com.example.booksapp;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class BooksFragment extends Fragment {

    ListView bookList;
    Button btnReturn;

    BookListAdapter adapter;
    public static final String CATEGORY_EXTRAS = "category_extras";

    public BooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.books_fragment, container, false);

        bookList = view.findViewById(R.id.listViewBooks);
        btnReturn = view.findViewById(R.id.btnReturn);

        String category= getArguments().getString(CATEGORY_EXTRAS);

        ArrayList<Book> booksFromSameCategory=new ArrayList<>();

        if(category != null)
        {
            for(Book book : BookManager.getInstance(requireContext()).getBooks()){
                if(book.getCategory().equalsIgnoreCase(category)){
                    booksFromSameCategory.add(book);
                }
            }
        }

        String author = getArguments().getString("authorName");

        ArrayList<Book> booksFromSameAuthor=new ArrayList<>();

        for(Book book : BookManager.getInstance(requireContext()).getBooks()){
            for(Author a:book.getAuthors())
                if(a.getName().equals(author))
                booksFromSameAuthor.add(book);
        }



        if(booksFromSameCategory.size()!=0)
        {
            adapter = new BookListAdapter(getActivity(),booksFromSameCategory);
            bookList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else {
            adapter=new BookListAdapter(getActivity(),booksFromSameAuthor);
            bookList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoriesAct.class);
                startActivity(intent);
            }
        });

        return  view;
    }
}