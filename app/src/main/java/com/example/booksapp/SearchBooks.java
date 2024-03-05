package com.example.booksapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchBooks extends AsyncTask<String, Void, ArrayList<Book>> {

    private static final String SEARCH_BY_TITLE_API_URL = "https://www.googleapis.com/books/v1/volumes?q=intitle:%s&maxResults=5";
    private static final String SEARCH_BY_AUTHOR_API_URL = "https://www.googleapis.com/books/v1/volumes?q=inauthor:%s&maxResults=5";
    private ISearchBooksDone listener;

    public SearchBooks(ISearchBooksDone listener) {
        this.listener = listener;
    }

    @Override
    protected ArrayList<Book> doInBackground(String... params) {
        ArrayList<Book> booksList = new ArrayList<>();

        String searchString = params[0];
        String urlString;

        if(searchString.contains("Author:"))
        {
            searchString = searchString.replace("Author:", "");
            urlString = String.format(SEARCH_BY_AUTHOR_API_URL, searchString);
        }
        else{
            urlString = String.format(SEARCH_BY_TITLE_API_URL, searchString);
        }


        if (params.length == 0)
            return new ArrayList<>();

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String booksJsonStr;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return booksList;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return booksList;
            }

            booksJsonStr = builder.toString();
            booksList = parseBooksJson(booksJsonStr);

        } catch (JSONException e) {
            // Handle JSONException
            Log.e("FetchBooks", "JSON Exception", e);
        }catch (IOException e) {
            Log.e("FetchBooks", "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("FetchBooks", "Error closing stream", e);
                }
            }
        }

       // listener.onSearchDone(booksList);

        return booksList;
    }

    @Override
    protected void onPostExecute(ArrayList<Book> booksList) {
        super.onPostExecute(booksList);
        if (listener != null) {
            listener.onSearchDone(booksList);
        }
    }

    private ArrayList<Book> parseBooksJson(String jsonStr) throws JSONException {
        ArrayList<Book> booksList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray items = jsonObject.getJSONArray("items");

// Determine the number of books to parse, up to 5 or less if fewer are available
        int numOfBooksToParse = Math.min(5, items.length());

        for (int i = 0; i < numOfBooksToParse; i++) {
            JSONObject bookObject = items.getJSONObject(i);
            JSONObject volumeInfo = bookObject.getJSONObject("volumeInfo");

            String title = volumeInfo.optString("title", "");
            String publishedDate = volumeInfo.optString("publishedDate", "");
            JSONArray authorsArray = volumeInfo.optJSONArray("authors");
            ArrayList<Author> authors = new ArrayList<>();

            if (authorsArray != null) {
                for (int j = 0; j < authorsArray.length(); j++) {
                    String authorName = authorsArray.getString(j);
                    Author author = new Author(authorName, 0); // Initialize number of books as needed
                    authors.add(author);
                }
            }

            String textSnippet = "";
            String description = "";
            String thumbnailUrlString = "";

            if (volumeInfo.has("description")) {
                description = volumeInfo.getString("description");
            }

            if (volumeInfo.has("imageLinks")) {
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                if (imageLinks.has("thumbnail")) {
                    thumbnailUrlString = imageLinks.getString("thumbnail");
                }
            }

            if (bookObject.has("searchInfo")) {
                JSONObject searchInfo = bookObject.getJSONObject("searchInfo");
                if (searchInfo.has("textSnippet")) {
                    textSnippet = searchInfo.getString("textSnippet");
                }
            }

            // Create the Book object
            Book book = new Book(title, authors, description, textSnippet, thumbnailUrlString, null,null, publishedDate);

            booksList.add(book);
        }

        return booksList;
    }

    public interface ISearchBooksDone{
        public void onSearchDone(List<Book> res);
    }
}
