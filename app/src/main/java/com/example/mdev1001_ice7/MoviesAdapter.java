package com.example.mdev1001_ice7;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Movie> movieList;
    private MovieApi apiService;
    private Context context;

    public MoviesAdapter(List<Movie> movieList, MovieApi apiService, Context context) {
        this.movieList = movieList;
        this.apiService = apiService;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.studioTextView.setText(movie.getStudio());
        holder.ratingTextView.setText(String.valueOf(movie.getCriticsRating()));

        float rating = (float) movie.getCriticsRating();
        int backgroundColor = Color.RED;
        int textColor = Color.WHITE;

        if (rating > 7) {
            backgroundColor = Color.GREEN;
            textColor = Color.BLACK;
        } else if (rating > 5) {
            backgroundColor = Color.YELLOW;
            textColor = Color.BLACK;
        }

        holder.ratingTextView.setBackgroundColor(backgroundColor);
        holder.ratingTextView.setTextColor(textColor);

//        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteMovie(position, movie);
//            }
//        });

        holder.parentCL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddEditActivity(movie);
            }
        });
    }

//    private void deleteMovie(int position, Movie movie) {
//        Call<Void> call = apiService.deleteMovie(movie.get_id());
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    movieList.remove(position);
//                    notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // Handle network failure
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView studioTextView;
        TextView ratingTextView;
        ConstraintLayout parentCL;
        ImageView deleteImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTV);
            studioTextView = itemView.findViewById(R.id.studioTV);
            ratingTextView = itemView.findViewById(R.id.ratingTV);
            parentCL = itemView.findViewById(R.id.parentCL);
//            deleteImageView = itemView.findViewById(R.id.deleteIV);
        }
    }

    private void openAddEditActivity(Movie movie) {
        Intent intent = new Intent(context, AddEditClassActivity.class);
        intent.putExtra("movie", (Serializable) movie);
        context.startActivity(intent);
    }
}