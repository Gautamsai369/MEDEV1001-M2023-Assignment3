package com.example.mdev1001_ice7;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mdev1001_ice7.Movie;
import com.example.mdev1001_ice7.MovieApi;
import com.example.mdev1001_ice7.R;

import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Collections;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddEditClassActivity extends AppCompatActivity {
    Movie movieDTO;
    private static final String BASE_URL = "https://mdev1001-m2023-api.onrender.com/";
    private MovieApi apiService;
    private TextView titleText;
    private Button addButton;
    private Button cancelButton;
    private EditText titleET;
    private EditText idET;
    private EditText StudioET;
    private EditText genresET;
    private EditText directorsET;
    private EditText writersET;
    private EditText actorsET;
    private EditText lengthET;
    private EditText yearET;
    private EditText shortDescriptionET;
    private EditText mpaRatingET;
    private EditText criticRatingET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create ApiService instance
        apiService = retrofit.create(MovieApi.class);
        titleText = findViewById(R.id.titleTextTV);
        idET = findViewById(R.id.idET);
        addButton = findViewById(R.id.addBT);
        cancelButton = findViewById(R.id.cancelBT);
        titleET = findViewById(R.id.titleET);
        StudioET = findViewById(R.id.StudioET);
        genresET = findViewById(R.id.genresET);
        directorsET = findViewById(R.id.directorsET);
        writersET = findViewById(R.id.writersET);
        actorsET = findViewById(R.id.actorsET);
        lengthET = findViewById(R.id.lengthET);
        yearET = findViewById(R.id.yearET);
        shortDescriptionET = findViewById(R.id.shortDescriptionET);
        mpaRatingET = findViewById(R.id.mpaRatingET);
        criticRatingET = findViewById(R.id.criticRatingET);

        if (getIntent().hasExtra("movie")) {
            movieDTO = (Movie) getIntent().getSerializableExtra("movie");
            populateFieldsWithMovieData();
            addButton.setText("Update");
        } else {
            movieDTO = null;
            addButton.setText("Add");
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMovie();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void populateFieldsWithMovieData() {
        idET.setText(movieDTO.getMovieID().toString());
        titleET.setText(movieDTO.getTitle().toString());
        StudioET.setText(movieDTO.getStudio());
        String genres = TextUtils.join(", ", movieDTO.getGenres());
        genresET.setText(genres);
        String directors = TextUtils.join(", ", movieDTO.getDirectors());
        directorsET.setText(directors);
        String writers = TextUtils.join(", ", movieDTO.getWriters());
        writersET.setText(writers);
        String actors = TextUtils.join(", ", movieDTO.getActors());
        actorsET.setText(actors);
        lengthET.setText(String.valueOf(movieDTO.getLength()));
        yearET.setText(String.valueOf(movieDTO.getYear()));
        shortDescriptionET.setText(movieDTO.getShortDescription().toString());
        mpaRatingET.setText(movieDTO.getMpaRating().toString());
        criticRatingET.setText(String.valueOf(movieDTO.getCriticsRating()));
    }

    private void updateMovie() {
        Movie movieReq = new Movie();
        if (movieDTO == null) {
            movieReq.set_id(UUID.randomUUID().toString());
        }
        movieReq.setMovieID(idET.getText().toString().trim());
        movieReq.setTitle(titleET.getText().toString().trim());
        movieReq.setStudio(StudioET.getText().toString().trim());
        movieReq.setGenres(Collections.singletonList(genresET.getText().toString().trim()));
        movieReq.setDirectors(Collections.singletonList(directorsET.getText().toString().trim()));
        movieReq.setWriters(Collections.singletonList(writersET.getText().toString().trim()));
        movieReq.setActors(Collections.singletonList(actorsET.getText().toString().trim()));
        movieReq.setYear(Integer.parseInt(yearET.getText().toString().trim()));
        movieReq.setLength(Integer.parseInt(lengthET.getText().toString().trim()));
        movieReq.setShortDescription(shortDescriptionET.getText().toString().trim());
        movieReq.setMpaRating(mpaRatingET.getText().toString().trim());
        movieReq.setCriticsRating(Double.parseDouble(criticRatingET.getText().toString().trim()));

        if (movieDTO != null) {
            // Update existing movie
            Call<Void> call = apiService.updateMovie(movieReq.get_id(), movieReq);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        finish();
                    } else {
                        // Handle update failure
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Handle update failure
                }
            });
        } else {
            Call<Void> call = apiService.addMovie(movieReq);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        finish();
                    } else {
                        // Handle add failure
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Handle add failure
                }
            });
        }
    }
}