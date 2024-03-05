package com.example.booksapp;

import static com.example.booksapp.CategoriesAct.CATEGORY_EXTRAS;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

public class ListsFragment extends Fragment implements FragmentInteraction {
    EditText searchText;
    Button btnSearch, btnAddCategory, btnAddBook, btnCategories, btnAuthors, btnAddBookOnline;
    ListView list;
    ArrayAdapter<String> adapter;
    private boolean isCategoryView = true; // Variable to track the current view state




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.lists_fragment, container, false);

        list = view.findViewById(R.id.ListViewCategories);
        btnAddBook = view.findViewById(R.id.btnAddBook);
        btnAddCategory = view.findViewById(R.id.btnAddCategory);
        btnSearch = view.findViewById(R.id.btnSearch);

        btnAddCategory.setEnabled(false);
        searchText = view.findViewById(R.id.editTextSearch);

        btnAuthors = view.findViewById(R.id.btnAuthors);
        btnCategories = view.findViewById(R.id.btnCategories);
        btnAddBookOnline = view.findViewById(R.id.btnAddBookOnline);

        Context context = requireContext();
        BookManager bookManager = BookManager.getInstance(context);



        if ( isCategoryView == true &&  getArguments() != null && getArguments().getBoolean(CATEGORY_EXTRAS)) {
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, bookManager.getCategories());
            list.setAdapter(adapter);
        }
        else{
            btnAddCategory.setVisibility(View.GONE);
            btnSearch.setVisibility(View.GONE);
            searchText.setVisibility(View.GONE);
            btnAddBook.setVisibility(View.GONE);

            final AuthorListAdapter adapter1 = new AuthorListAdapter(getActivity(), new ArrayList<>());
            adapter1.clearData(); // Clear any existing data
            if(bookManager.getAuthors().size() == 0)
            {
                //bookManager.addAuthor(new Author("test",1));
                //bookManager.addAuthor(new Author("test asd",3));

            }
            adapter1.addAll(bookManager.getAuthors()); // Add new data
            list.setAdapter(adapter1);
        }

        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookManager.getCategories().size() == 0)
                    Toast.makeText(getActivity(), "Please add category first", Toast.LENGTH_LONG).show();
                else
                    getActivity().startActivity(new Intent(getActivity(), AddBooksAct.class));
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.getFilter().filter(searchText.getText().toString(), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int count) {
                        btnAddCategory.setEnabled(count == 0);
                    }
                });
            }
        });

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCategory = searchText.getText().toString();
                bookManager.addCategory(newCategory);
           //     categoryList.add(newCategory);
                adapter.add(newCategory);
                adapter.getFilter().filter(null);
                adapter.notifyDataSetChanged();
                searchText.setText("");
                // listCategory.setSelection(adapter.getCount() - 1);
                btnAddCategory.setEnabled(false);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = list.getItemAtPosition(position).toString();
                OpenBookFragment(item);
            }
        });

        btnAuthors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCategoryView = false;
                OpenAuthorFragment();
            }
        });

        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListsFragment listsFragment = new ListsFragment();

                // Set arguments to indicate the category view
                Bundle args = new Bundle();
                args.putBoolean(CATEGORY_EXTRAS, true);
                listsFragment.setArguments(args);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLists, listsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnAddBookOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bookManager.getCategories().size() == 0)
                    Toast.makeText(getActivity(), "Please add category first", Toast.LENGTH_LONG).show();
                else
                    getActivity().startActivity(new Intent(getActivity(), OnlineAct.class));
            }
        });

        return view;
    }


    @Override
    public void OpenBookFragment(String category) {
        BooksFragment booksFragment = new BooksFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY_EXTRAS,category);
        booksFragment.setArguments(bundle);

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentLists, booksFragment) // Replace 'R.id.fragment_container' with your container ID
                    .addToBackStack(null) // Optional: Adds transaction to the back stack, allowing user to navigate back
                    .commit();
        }

    }

    @Override
    public void OpenAuthorFragment() {
        ListsFragment listsFragment = new ListsFragment();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLists, listsFragment)
                .addToBackStack(null)
                .commit();
    }

}
