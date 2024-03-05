package com.example.booksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookListAdapter extends ArrayAdapter<Book> {

    public BookListAdapter(Context context, ArrayList<Book> books) {
        super(context, R.layout.book_item, books);
    }


        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final Book book = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
            }
            ImageView image = (ImageView) convertView.findViewById(R.id.bookImage);
            TextView bookTitle = (TextView) convertView.findViewById(R.id.bookTitle);
            TextView nameAuthor = (TextView) convertView.findViewById(R.id.bookAuthor);
            TextView publishedDate =(TextView) convertView.findViewById(R.id.publishedDate);
            TextView description =(TextView) convertView.findViewById(R.id.bookDescription);

            if(book.getImage() != null)
                image.setImageDrawable(book.getImage());

            if (book.getImageLink() != null && !book.getImageLink().isEmpty())
                Picasso.get().load(book.getImageLink()).into(image);

            bookTitle.setText(book.getTitle());

            String authors ="";

            for (Author author:  book.getAuthors()) {
                authors += author.getName() + "\n";
            }
            nameAuthor.setText(authors);

            publishedDate.setText(book.getPublishedDate());
            description.setText(book.getDescription());

            return convertView;
        }
}
