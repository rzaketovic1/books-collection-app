package com.example.booksapp;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;
import java.util.List;

public class Book {

    private String category;
    private Drawable image;

    private String title;
    private ArrayList<Author> authors;
    private String description;
    private String imageLink;
    private String textSnippet;
    private String publishedDate;


    public Book(String title, ArrayList<Author> authors, String description, String textSnippet, String imageLink, String category, Drawable image, String publishedDate){
        this.title = title;
        this.authors = authors;
        this.description = description;
        this.textSnippet = textSnippet;
        this.imageLink = imageLink;
        this.category = category;
        this.image = image;
        this.publishedDate = publishedDate;
    }

    // getters for private attributes

    public List<Author> getAuthors(){ return  authors;}

    public String getCategory() {
        return category;
    }

    public void SetCategory(String category)
    {
        this.category = category;
    }

    public Drawable getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getTextSnippet(){
        return  textSnippet;
    }

    public String getImageLink(){
        return  imageLink;
    }

    public String getPublishedDate(){ return  publishedDate;}

}