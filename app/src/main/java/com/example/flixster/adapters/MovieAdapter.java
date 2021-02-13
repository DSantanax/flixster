package com.example.flixster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // needed for the movie data
    List<Movie> movieList;
    // context - inflate a view, where is it being constructed from - Activity
    Context context;

    public MovieAdapter(Context context, List<Movie> movieList){
        this.movieList = movieList;
        this.context = context;
    }

    // will inflate the layout XML (item_movie) and return it in a ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d("MovieAdapter", "onCreateViewHolder");
        // inflate the item_movie XML, parent - ViewGroup, false not attaching to root
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        // return wrapper around the ViewHolder, which adds the data
        return new ViewHolder(view);
    }

    // populate the data through the ViewHolder
    @Override
    // bind data to the view holder using the movieList for each movie
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder: " + position);
        // get the movie at the position
        Movie movie = movieList.get(position);
        // bind the movie data (model) into the view holder
        holder.bind(movie);
    }

    // get the size of the movies, total count of items in the list
    @Override
    public int getItemCount() {
        return movieList.size();
    }

    //    the ViewHolder is a representation of the row (item_movie) of the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        TextView tvRating;
        TextView tvReleaseDate;

        // we get the layout from the View and assign it to our variables
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReleaseDate = itemView.findViewById(R.id.tvDate);
        }

        // set the data to the layouts
        public void bind(Movie movie) {
            // load the text
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverView());
            // TODO add resource or add placeholder instead
            tvRating.setText(String.format("Rating: %s", movie.getMovieRating()));
            tvReleaseDate.setText(String.format("Release Date: %s", movie.getReleaseDate()));
            // in some cases we would want to load the backdrop path instead
            // when in landscape mode
            String imageUrl;
            // check phone orientation of current activity
            int orientation = context.getResources().getConfiguration().orientation;
            // if portrait get image for portrait
            if(orientation == Configuration.ORIENTATION_PORTRAIT){
                imageUrl = movie.getPostPath();
            }
            // else get landscape mode
            else {
                imageUrl = movie.getBdPath();
            }

            // load the image, using the Glide library
            // get context load the path and put it into the image view
            // add a placeholder for the image when waiting & an error image if it does not load

            // .transform(new RoundedCorners(5)) , causes the image to add more margins for some reason and pushes the relative layouts
            // can use GranularRoundedCorners to do each corner of the image
            // more down per view layout
            Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_waiting).transform(new GranularRoundedCorners(20,20,20,20)).error(R.drawable.ic_movie_tickets).into(ivPoster);

        }
    }
}
