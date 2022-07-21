package com.example.quizapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.quizapps.Adapters.AnswersAdapter;

public class AnswersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView answerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        toolbar = (Toolbar) findViewById(R.id.aa_toolbar);
        answerView = findViewById(R.id.aa_test_recycler_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

//        int cat_index = getIntent().getIntExtra("CAT_INDEX",0);

        getSupportActionBar().setTitle("ANSWERS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(AnswersActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        answerView.setLayoutManager(layoutManager);

        AnswersAdapter adapter = new AnswersAdapter(DbQuery.g_quesList);
        answerView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            AnswersActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}