package com.example.quizapps;

import static com.example.quizapps.DbQuery.g_catList;
import static com.example.quizapps.DbQuery.g_selected_test_index;
import static com.example.quizapps.DbQuery.loadQuestion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class StartTestActivity extends AppCompatActivity {

    private TextView catName, testNo, totalQ, bestScore, time;
    private Button startTestB;
    private ImageView backB;
    private Dialog dialogProgress;
    private TextView txtDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        dialogProgress = new Dialog(StartTestActivity.this);
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Loading.....");

        dialogProgress.show();

        anhXa();

        loadQuestion(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                if (DbQuery.g_quesList.size()==0){
                    Toast.makeText(StartTestActivity.this, "Xin lỗi bài test này hiện đang được cập nhật, vui lòng chọn bài test khác", Toast.LENGTH_SHORT).show();
                    StartTestActivity.this.finish();
                }

                setData();
                dialogProgress.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(StartTestActivity.this,"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();
                dialogProgress.dismiss();
            }
        });
    }

    private void anhXa() {
        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_no);
        totalQ = findViewById(R.id.st_total_ques);
        bestScore = findViewById(R.id.st_best_score);
        time = findViewById(R.id.st_time);
        startTestB = findViewById(R.id.start_TestB);
        backB = findViewById(R.id.st_backB);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTestActivity.this.finish();
            }
        });

        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTestActivity.this,QuestionActivity.class);
                StartTestActivity.this.startActivity(intent);
            }
        });

    }

    private void setData(){
        catName.setText(g_catList.get(DbQuery.g_selected_cat_index).getName());
        testNo.setText("TEST No. " + String.valueOf(DbQuery.g_selected_test_index + 1));
        totalQ.setText(String.valueOf(DbQuery.g_quesList.size()));
        bestScore.setText(String.valueOf(DbQuery.g_testModels.get(DbQuery.g_selected_test_index).getTopScore()));
        time.setText(String.valueOf(DbQuery.g_testModels.get(DbQuery.g_selected_test_index).getTime()));
    }
}