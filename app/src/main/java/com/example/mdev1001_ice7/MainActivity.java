package com.example.mdev1001_ice7;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button addButton,searchButton;
    private EditText editText;
    List<Movie> filteredMovies;
    RecyclerView list;
    MoviesAdapter moviesAdapter;
    MovieApi movieApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = findViewById(R.id.addBT);
        searchButton = findViewById(R.id.searchBT);
        editText = findViewById(R.id.searchText);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),AddEditClassActivity.class);
                startActivity(i);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText.getText())){
                    Toast.makeText(MainActivity.this, "Enter movie name to search", Toast.LENGTH_SHORT).show();
                    filteredMovies.clear();
                    list = findViewById(R.id.moviesRL);
                    moviesAdapter = new MoviesAdapter(filteredMovies, movieApi, MainActivity.this);
                    list.setAdapter(moviesAdapter);
                }
                else{
                    String filterdata = editText.getText().toString();
                    CallApi(filterdata);
                }
            }
        });
    }

    public void CallApi(String str ) {
        Moshi moshi = new Moshi.Builder().addLast(new KotlinJsonAdapterFactory()).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.omdbapi.com/?i=tt3896198&apikey=99c335c0")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build();

        movieApi = retrofit.create(MovieApi.class);
        movieApi.fetchMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body();
                    filteredMovies = new ArrayList<>();
                    filteredMovies.clear();
                    for (Movie movie : movies) {
                        if (movie.getTitle().contains(str)) {
                            filteredMovies.add(movie);
                        }
                    }
                    if (movies != null && !movies.isEmpty()) {
                        list = findViewById(R.id.moviesRL);
                        moviesAdapter = new MoviesAdapter(filteredMovies, movieApi, MainActivity.this);
                        list.setAdapter(moviesAdapter);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                // Handle failure
            }

        });

    }

}
