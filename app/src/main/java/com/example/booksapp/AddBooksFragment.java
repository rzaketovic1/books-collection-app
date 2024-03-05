package com.example.booksapp;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;


public class AddBooksFragment extends Fragment {

    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    private EditText editTextAuthorName, editTextBookName, editTextDescription, editTextPublishDate;
    private Button btnFindImage, btnAddBook, btnReset;
    private ImageView image;
    private Drawable drawable;

    private int PICK_IMAGE_REQUEST = 1;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    public AddBooksFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                            image.setImageBitmap(bitmap);
                            drawable = new BitmapDrawable(getResources(), bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.add_books_fragment, container, false);
        editTextAuthorName = view.findViewById(R.id.editTextAuthorName);
        editTextBookName = view.findViewById(R.id.editTextBookName);
        editTextDescription = view.findViewById(R.id.descrptionText);
        editTextPublishDate = view.findViewById(R.id.publishedDateText);
        spinner = view.findViewById(R.id.SpinnerBookCategory);
        btnAddBook = view.findViewById(R.id.btnAddBook);

        btnFindImage = view.findViewById(R.id.btnFindImage);
        image = view.findViewById(R.id.bookImage);

        btnReset = view.findViewById(R.id.btnReset);

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, BookManager.getInstance(getActivity()).getCategories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnAddBook.setOnClickListener(v -> {
            String authorName = editTextAuthorName.getText().toString();
            String bookName = editTextBookName.getText().toString();
            String description = editTextDescription.getText().toString();
            String publishedDate = editTextPublishDate.getText().toString();
            String category = spinner.getSelectedItem().toString();

            ArrayList<Author> newAuthor = new ArrayList<>();
            Author author = new Author(authorName,1);
            newAuthor.add(author);



            long bookInsertedId = BookManager.getInstance(getActivity()).addBook(new Book(bookName,newAuthor,description,null,null,category,drawable, publishedDate));

            Boolean authorExist=false;
            ArrayList<Author> authors = BookManager.getInstance(requireContext()).getAuthors();

            for(Author author1 : authors){
                if(author1.getName().equalsIgnoreCase(authorName)){
                    author1.setNumberOfBooks(author1.getNumberOfBooks()+1);
                    authorExist=true;
                }
            }

            if(authorExist==false)
            {
                long authorInsertedId = BookManager.getInstance(requireContext()).addAuthor(author);
                BookManager.getInstance(requireContext()).addAuthorship(authorInsertedId, bookInsertedId);
            }

            editTextAuthorName.setText("");
            editTextBookName.setText("");
            editTextDescription.setText("");
            editTextPublishDate.setText("");
            Toast.makeText(getActivity(), "Book successfully added", Toast.LENGTH_SHORT).show();
            image.setImageResource(R.drawable.ic_launcher_background);
            drawable = getResources().getDrawable(R.drawable.ic_launcher_background);
        });

        btnFindImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImageLauncher.launch(intent);

            }
        });

        btnReset.setOnClickListener(v -> requireActivity().onBackPressed());

        return  view;

    }
}