package com.example.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

// POJO - plain old java object
// encapsulate the movie data

//add annotation to show its a parcel
@Parcel
public class Movie {
    // initially a relative path
    String postPath;
    String title;
    String overView;
    String bdPath;
    double movieRating;
    String releaseDate;
    int movieId;

    public Movie(){
        // empty constructor needed for the Parceler library
    }

    // this constructor throws the JSONException, whoever calls this method must handle
    // the exception
    public Movie(JSONObject jsonObject) throws JSONException {
        postPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overView = jsonObject.getString("overview");
        bdPath = jsonObject.getString("backdrop_path");
        movieRating = Double.parseDouble(jsonObject.getString("vote_average"));
        releaseDate = jsonObject.getString("release_date");
        movieId = jsonObject.getInt("id");
    }

    // factory method which goes through the array results and grabs each object from the array
    // to create a Movie object from them
    // static method which creates Movies from the Movie class
    public static List<Movie> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Movie> movie = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            movie.add(new Movie(jsonArray.getJSONObject(i)));
        }
        return movie;
    }

    // to access the data, we need to get the full path
    // we want the width to be w342, this is the base URL to the poster_path URL
    public String getPostPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", postPath);
        // the correct way of doing this is fetching all the sizes appending it to the base
        // URL, then adding it to the posterPath
    }
    // base URL + relative URL = full path endpoint for the image
    public String getBdPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", bdPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverView() {
        return overView;
    }

    public double getMovieRating(){
        return movieRating;
    }
    public String getReleaseDate() {return releaseDate; }

    public int getMovieId() {
        return movieId;
    }
}
