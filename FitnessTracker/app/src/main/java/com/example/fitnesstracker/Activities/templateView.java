package com.example.fitnesstracker.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.fitnesstracker.APIHolder;
import com.example.fitnesstracker.Objects.ExercisesData;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.exerciseAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class templateView extends AppCompatActivity {

    private RecyclerView exerciseHolder; // Recvycler view that holds exercise cards
    private exerciseAdapter exerciseHolderAdapter; // Bridge between recycler view and data for each card
    private RecyclerView.LayoutManager exerciseHolderLayoutManager; // Aligning each card

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_view);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Templates");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://workoutapp-api-heroku.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIHolder apiHolder = retrofit.create(APIHolder.class); // API
        Call<List<ExercisesData.Exercise>> call = apiHolder.getExercises(); // Call
        List<ExercisesData.Exercise> exercises = null;
        exerciseHolder = findViewById(R.id.templateRv);
        exerciseHolderLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) exerciseHolderLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        exerciseHolder.setLayoutManager(exerciseHolderLayoutManager);
        exerciseHolderAdapter = new exerciseAdapter(exercises);
        exerciseHolder.setAdapter(exerciseHolderAdapter);

        call.enqueue(new Callback<List<ExercisesData.Exercise>>() {
            @Override
            public void onResponse(Call<List<ExercisesData.Exercise>> call, Response<List<ExercisesData.Exercise>> response) {
                if (!response.isSuccessful()){
                    return;
                }
                // Gets exercise data
                ExercisesData data = new ExercisesData();
                data.setExercises(response.body());
                List<ExercisesData.Exercise> exercises = data.getExercises(); // List of exercises

                // Exercise datat is put in recycler view
                exerciseHolderAdapter = new exerciseAdapter(exercises); // Recycler view Adapter
                exerciseHolder.setAdapter(exerciseHolderAdapter);
            }

            @Override
            public void onFailure(Call<List<ExercisesData.Exercise>> call, Throwable t) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.template_menu, menu);
        MenuItem searchItem= menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                exerciseHolderAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}