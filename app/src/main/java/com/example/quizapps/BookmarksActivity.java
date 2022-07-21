package com.example.quizapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quizapps.Adapters.AnswersAdapter;
import com.example.quizapps.Adapters.BookmarksAdapter;

public class BookmarksActivity extends AppCompatActivity {

    private RecyclerView questionView;
    private Toolbar toolbar;
    private Dialog dialogProgress;
    private TextView txtDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        toolbar = (Toolbar) findViewById(R.id.ba_toolbar);
        questionView = findViewById(R.id.ba_test_recycler_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

//        int cat_index = getIntent().getIntExtra("CAT_INDEX",0);

        getSupportActionBar().setTitle("Saved Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogProgress = new Dialog(BookmarksActivity.this);
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Loading.....");

        dialogProgress.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(BookmarksActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        questionView.setLayoutManager(layoutManager);

        DbQuery.loadBookmarks(this,new MyCompleteListener() {
            @Override
            public void onSuccess() {
                BookmarksAdapter adapter = new BookmarksAdapter(DbQuery.g_bookmarksList);
                questionView.setAdapter(adapter);

                dialogProgress.dismiss();
            }

            @Override
            public void onFailure() {
                dialogProgress.dismiss();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            BookmarksActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}