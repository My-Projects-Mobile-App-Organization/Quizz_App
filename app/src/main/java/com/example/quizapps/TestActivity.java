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
import android.widget.Toast;

import com.example.quizapps.Adapters.TestAdapter;

public class TestActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView testView;
    private Dialog dialogProgress;
    private TestAdapter testAdapter;
    private TextView txtDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // set Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

//        int cat_index = getIntent().getIntExtra("CAT_INDEX",0);

        getSupportActionBar().setTitle(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testView = (RecyclerView) findViewById(R.id.test_recycler_view);


        dialogProgress = new Dialog(TestActivity.this);
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Loading.....");

        dialogProgress.show();

        // set layout Recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(TestActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        testView.setLayoutManager(layoutManager);

        DbQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                if (DbQuery.g_testModels.size()==0){
                    Toast.makeText(TestActivity.this, "Xin lỗi, nội dun này đang được cập nhật bài test", Toast.LENGTH_SHORT).show();
                    TestActivity.this.finish();
                }

                DbQuery.loadMyScore(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        testAdapter = new TestAdapter(DbQuery.g_testModels);
                        testView.setAdapter(testAdapter);
                        dialogProgress.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        dialogProgress.dismiss();
                        Toast.makeText(TestActivity.this,"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();

                    }
                });

            }

            @Override
            public void onFailure() {
                dialogProgress.dismiss();
                Toast.makeText(TestActivity.this,"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();

            }
        });


    }
//
//    private List<TestModel> getListTest() {
//        testModels = new ArrayList<>();
//
//        testModels.add(new TestModel("1",50,20));
//        testModels.add(new TestModel("2",50,20));
//        testModels.add(new TestModel("3",50,20));
//        testModels.add(new TestModel("4",50,20));
//        return testModels;
//    }

    // set btn back
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}