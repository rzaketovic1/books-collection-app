package com.example.booksapp;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Arrays;

public class BookManager {
    private static BookManager instance;
    private ArrayList<Book> books;
    private ArrayList<String> categories;

    private ArrayList<Author> authors;
    private DatabaseOpenHelper dbHelper;
    private Context context;
    private BookManager(Context context) {
        // Private constructor to prevent instantiation
        this.context = context.getApplicationContext();
        dbHelper = new DatabaseOpenHelper(this.context);
        categories = new ArrayList<>();
        books = new ArrayList<>();
        authors = new ArrayList();
    }

    private BookManager() {
    }
    public static synchronized BookManager getInstance(Context context) {
        if (instance == null) {
            instance = new BookManager(context);
        }
        return instance;
    }

    public static synchronized BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }
    public ArrayList<String> getCategories(){return categories;}
    public  ArrayList<Author> getAuthors(){
        return  authors;
    }


    long addBook(Book book) {

        long insertedId = -1;
        // Check if the book already exists
        if (!books.contains(book)) {
            // Add to the in-memory list
            books.add(book);

            // Add to the database
            insertedId = dbHelper.addBook(book);

            // Handle the result if needed
            if (insertedId == -1) {
                // Handle error
                // For example, show a message or log an error
            }
        }
        return insertedId;
    }

    public long addAuthor(Author author) {
        // Add to the in-memory list
        authors.add(author);

        // Add to the database
        long insertedId = dbHelper.addAuthor(author);

        // Handle the result if needed
        if (insertedId == -1) {
            // Handle error
            // For example, show a message or log an error
        }

        return  insertedId;
    }

    public void addAuthorship(long authorId, long bookId)
    {
        long insertedId = dbHelper.addAuthorship(authorId, bookId);

        if (insertedId == -1) {
            // Handle error
            // For example, show a message or log an error
        }
    }


    public void addCategory(String categoryName) {
        // Check if the category already exists
        if (!categories.contains(categoryName)) {
            // Add to the in-memory list
            categories.add(categoryName);
            // Add to the database
            long categoryId = dbHelper.addCategory(categoryName);
            // Handle the result if needed
            if (categoryId == -1) {
                // Handle error
            }
        }
    }
}
