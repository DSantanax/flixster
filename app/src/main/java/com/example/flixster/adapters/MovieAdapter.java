package com.example.flixster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixster.DetailActivity;
import com.example.flixster.MainActivity;
import com.example.flixster.R;
import com.example.flixster.databinding.ItemMovieBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

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
        // View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        // data binding way
        // inflate the ViewGroup -> then bind
        // or .from(context)
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemMovieBinding movieBinding = ItemMovieBinding.inflate(layoutInflater, parent, false);

        // return wrapper around the ViewHolder, which adds the data
        return new ViewHolder(movieBinding);
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
        RelativeLayout relativeLayout;
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        TextView tvRating;
        TextView tvReleaseDate;
        ImageView ivOverlay;
        // not necessary
        ItemMovieBinding binding;

        // we get the layout from the View and assign it to our variables
        public ViewHolder(@NonNull ItemMovieBinding movieBinding) {
            // getRoot() calls the binding's view
            super(movieBinding.getRoot());
            binding = movieBinding;
            relativeLayout = movieBinding.itemMovie;
            tvTitle = movieBinding.tvTitle;
            tvOverview = movieBinding.tvOverview;
            ivPoster = movieBinding.ivPoster;
            tvRating = movieBinding.tvRating;
            tvReleaseDate = movieBinding.tvDate;
            ivOverlay = movieBinding.ivOverlay;
        }

        // set the data to the layouts
        public void bind(Movie movie) {
            // can do this instead with binding
            // binding.tvTitle.setText(movie.getTitle());

            // load the text
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverView());
            tvRating.setText(String.format("Rating: %s", movie.getMovieRating()));
            tvReleaseDate.setText(String.format("Release Date: %s", movie.getReleaseDate()));
            // if the movie is not popular hide the image view overlay YT icon (won't be played immediately)
            if(movie.getMovieRating() < 5){
                ivOverlay.setVisibility(View.INVISIBLE);
            }
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

            // debug -> show toast when click the title
            // use lambda expression rather than inner anonymous
//            tvTitle.setOnClickListener(v -> Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show());

            // 1. register click listener on the entire container (row view)
            relativeLayout.setOnClickListener((v) -> {
                // Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
                // intent
                // 2. navigate to a new activity
                // create an intent from context to the class
                Intent intent = new Intent(context, DetailActivity.class);

                // pass data to the activity Key/value pair
                // intent.putExtra("title", movie.getTitle());

                // can also pass objects using parceler and wrapping the object
                intent.putExtra("movie", Parcels.wrap(movie));
                // For multiple shared elements use Pair (view, transitionName)
                Pair<View, String> p1 = Pair.create((View) tvTitle, "movieTitle");
                Pair<View, String> p2 = Pair.create((View) tvOverview, "overViewText");

                // Call for shared transition, first param needs activity cast it
                // to the context (current), Views/Pairs, if View use elemName)

//                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        (Activity) context, (View)tvTitle, "movieTitle"
//                );

                // Safe Varargs
                @SuppressWarnings("unchecked")
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) context, p1, p2
                );
                // show the activity, with shared elements
                context.startActivity(intent, optionsCompat.toBundle());
            });

        }
    }
}
