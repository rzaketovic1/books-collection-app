package com.example.booksapp;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class OnlineFragment extends Fragment {

    private List<Book> res;

    private EditText editTextSearch;
    private Button btnSearch;
    private Spinner sResults;
    private Spinner sCategories;
    private Button btnAdd;
    private Button btnReturn;

    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.online_fragment, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        sResults = view.findViewById(R.id.sResults);
        sCategories = view.findViewById(R.id.sCategories);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnReturn = view.findViewById(R.id.btnReturn);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookTitle = editTextSearch.getText().toString();
                if (!bookTitle.isEmpty()) {
                    performBookSearch(bookTitle);
                } else {
                    // Show a message or handle empty search query
                }
            }
        });

        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, BookManager.getInstance(getActivity()).getCategories());
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategories.setAdapter(adapterCategories);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelectedBookToLibrary();
            }
        });

        btnReturn.setOnClickListener(v -> requireActivity().onBackPressed());

        // Inflate the layout for this fragment
        return  view;
    }


    private void performBookSearch(String searchText) {
        SearchBooks searchBooks = new SearchBooks(new SearchBooks.ISearchBooksDone() {
            @Override
            public void onSearchDone(List<Book> result) {
                // Update UI with the search results
                res = result;
                List<String> bookTitles = new ArrayList<>();

                for (Book book : res) {
                    bookTitles.add(book.getTitle()); // Assuming you have a method to get the title from Book
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, bookTitles);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sResults.setAdapter(adapter);

                sResults.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Handle the selection
                        updateDescriptionAndImage(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Do nothing if nothing is selected
                    }
                });

            }
        });
        searchBooks.execute(searchText);
    }

    private void updateDescriptionAndImage(int position) {
        if (position >= 0 && position < res.size()) {
            Book selectedBook = res.get(position);

            // Update TextView with the description
            TextView textDescription = view.findViewById(R.id.textDescription);
            textDescription.setVisibility(View.VISIBLE);
            if (selectedBook.getTextSnippet()  == null || selectedBook.getTextSnippet().equals("")) {
                textDescription.setText("Not exist description");
            } else {
                textDescription.setText(selectedBook.getTextSnippet());
            }

            // Update ImageView with the image link
            ImageView imageView = view.findViewById(R.id.image);
            if (selectedBook.getImageLink() != null && !selectedBook.getImageLink().isEmpty()) {
                Picasso.get().load(selectedBook.getImageLink()).into(imageView);
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    private void addSelectedBookToLibrary() {
        int selectedPosition = sResults.getSelectedItemPosition();

        if (res != null && selectedPosition >= 0 && selectedPosition < res.size()) {
            Book selectedBook = res.get(selectedPosition);

            String category = sCategories.getSelectedItem().toString();

            selectedBook.SetCategory(category);

            long bookInsertedId = BookManager.getInstance(requireContext()).addBook(selectedBook);

            for (Author author: selectedBook.getAuthors()) {
                Author newAuthor = new Author(author.getName(),1);
                Boolean authorExist=false;

                ArrayList<Author> authors = BookManager.getInstance(getActivity()).getAuthors();

                for(Author author1 : authors){
                    if(author1.getName().equalsIgnoreCase(newAuthor.getName())){
                        author1.setNumberOfBooks(author1.getNumberOfBooks()+1);
                        authorExist=true;
                    }
                }

                if(authorExist==false) {
                    long authorInsertedId = BookManager.getInstance(getActivity()).addAuthor(newAuthor);
                    BookManager.getInstance(getActivity()).addAuthorship(authorInsertedId, bookInsertedId);
                }
            }

            ArrayList<Author> authors = BookManager.getInstance(getActivity()).getAuthors();

            // Provide feedback to the user, e.g., show a Toast
            Toast.makeText(getActivity(), "Book added to library", Toast.LENGTH_SHORT).show();
        } else {
            // Provide feedback that no book is selected
            Toast.makeText(getActivity(), "No book selected", Toast.LENGTH_SHORT).show();
        }
    }

}