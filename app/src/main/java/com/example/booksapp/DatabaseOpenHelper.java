package com.example.booksapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "books_database";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_BOOK = "book";
    private static final String TABLE_AUTHOR = "author";
    private static final String TABLE_AUTHORSHIP = "authorship";

    // Common column names
    private static final String KEY_ID = "_id";

    // Category table column names
    private static final String KEY_CATEGORY_NAME = "name";

    // Book table column names
    private static final String KEY_BOOK_NAME = "name";
    private static final String KEY_BOOK_DESCRIPTION = "description";
    private static final String KEY_BOOK_PUBLISH_DATE = "publish_date";
    private static final String KEY_BOOK_CATEGORY_ID = "category_id";
    private static final String KEY_BOOK_IMAGE = "image";

    // Author table column names
    private static final String KEY_AUTHOR_NAME = "name";

    // Authorship table column names
    private static final String KEY_AUTHORSHIP_AUTHOR_ID = "author_id";
    private static final String KEY_AUTHORSHIP_BOOK_ID = "book_id";

    // Table creation SQL statements
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CATEGORY_NAME + " TEXT)";

    private static final String CREATE_TABLE_BOOK = "CREATE TABLE " + TABLE_BOOK + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_BOOK_NAME + " TEXT,"
            + KEY_BOOK_DESCRIPTION + " TEXT,"
            + KEY_BOOK_PUBLISH_DATE + " TEXT,"
            + KEY_BOOK_CATEGORY_ID + " INTEGER,"
            + KEY_BOOK_IMAGE + " TEXT,"
            + "FOREIGN KEY(" + KEY_BOOK_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID + "))";

    private static final String CREATE_TABLE_AUTHOR = "CREATE TABLE " + TABLE_AUTHOR + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_AUTHOR_NAME + " TEXT)";

    private static final String CREATE_TABLE_AUTHORSHIP = "CREATE TABLE " + TABLE_AUTHORSHIP + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_AUTHORSHIP_AUTHOR_ID + " INTEGER,"
            + KEY_AUTHORSHIP_BOOK_ID + " INTEGER,"
            + "FOREIGN KEY(" + KEY_AUTHORSHIP_AUTHOR_ID + ") REFERENCES " + TABLE_AUTHOR + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_AUTHORSHIP_BOOK_ID + ") REFERENCES " + TABLE_BOOK + "(" + KEY_ID + "))";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_BOOK);
        db.execSQL(CREATE_TABLE_AUTHOR);
        db.execSQL(CREATE_TABLE_AUTHORSHIP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Drop existing tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHOR);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHORSHIP);

            // Create new tables
            onCreate(db);
        }
    }

    public long addCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        long categoryId = -1;

        try {
            // Check if the category already exists
            if (!categoryExists(db, name)) {
                ContentValues values = new ContentValues();
                values.put(KEY_CATEGORY_NAME, name);

                // Insert the new category
                categoryId = db.insert(TABLE_CATEGORY, null, values);

                db.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryId;
    }

    private boolean categoryExists(SQLiteDatabase db, String categoryName) {
        Cursor cursor = null;

            String[] selectionArgs = {categoryName};
            cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORY + " WHERE " + KEY_CATEGORY_NAME + "=?", selectionArgs);
            return cursor.getCount() > 0;
    }



    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        long insertedId = -1;

        try {

            ContentValues values = new ContentValues();

            // get category_Id by category name
            int category_id = getCategoryIdByName(book.getCategory());

            values.put(KEY_BOOK_NAME, book.getTitle());
            values.put(KEY_BOOK_DESCRIPTION, book.getDescription());
            values.put(KEY_BOOK_PUBLISH_DATE, book.getPublishedDate());
            values.put(KEY_BOOK_CATEGORY_ID, category_id);
            values.put(KEY_BOOK_IMAGE, book.getImageLink());

            // Insert the new book and get the inserted row ID
            insertedId = db.insert(TABLE_BOOK, null, values);

            db.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return insertedId;
    }

    // Helper method to get CATEGORY_ID based on the category name
    private int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int categoryId = -1;

        Cursor cursor = db.query(
                TABLE_CATEGORY,
                new String[]{KEY_ID},
                KEY_CATEGORY_NAME + " = ?",
                new String[]{categoryName},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Check if KEY_ID is present in the cursor
            int columnIndex = cursor.getColumnIndex(KEY_ID);
            if (columnIndex >= 0) {
                categoryId = cursor.getInt(columnIndex);
            }
            cursor.close();
        }

        return categoryId;
    }

    public long addAuthor(Author author) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_AUTHOR_NAME, author.getName());

        // Insert the new author and get the inserted row ID
        long insertedId = db.insert(TABLE_AUTHOR, null, values);

        db.close();

        // Return the inserted row ID (or -1 if an error occurred)
        return insertedId;
    }

    public long addAuthorship(long authorId, long bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_AUTHORSHIP_AUTHOR_ID, authorId);
        values.put(KEY_AUTHORSHIP_BOOK_ID, bookId);

        // Insert the new authorship entry and get the inserted row ID
        long insertedId = db.insert(TABLE_AUTHORSHIP, null, values);

        db.close();

        // Return the inserted row ID (or -1 if an error occurred)
        return insertedId;
    }


}
