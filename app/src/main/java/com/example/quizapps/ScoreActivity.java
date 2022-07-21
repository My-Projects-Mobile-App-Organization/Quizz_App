package com.example.quizapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapps.Models.QuestionModel;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    private TextView txtScore, txtTime, txtTotalQ, txtCorrectQ, txtWrongQ, txtUnAttempt;
    private Button btnLeaderBoard, btnReAttempt, btnViewAns;
    private long timeTaken;
    private Dialog dialogProgress;
    private TextView txtDialog;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        int cat_index = getIntent().getIntExtra("CAT_INDEX",0);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogProgress = new Dialog(ScoreActivity.this);
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Loading.....");
        dialogProgress.show();


        anhXa();

        loadData();

        setBookmars();

        btnViewAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(ScoreActivity.this, AnswersActivity.class);
                startActivity(intent);
            }
        });
        // làm lại
        btnReAttempt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reAttempt();
            }
        });

        btnLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        saveResult();
    }

    private void setBookmars() {
        for (int i=0;i<DbQuery.g_quesList.size();i++){
            QuestionModel questionModel = DbQuery.g_quesList.get(i);
            if (questionModel.isBookmark()){
                if (! DbQuery.g_bmIdList.contains(questionModel.getqID())){
                    DbQuery.g_bmIdList.add(questionModel.getqID());
                    DbQuery.myprofileModel.setBookmarsCount(DbQuery.g_bmIdList.size());
                }
            }else {
                if (DbQuery.g_bmIdList.contains(questionModel.getqID())){
                    DbQuery.g_bmIdList.remove(questionModel.getqID());
                    DbQuery.myprofileModel.setBookmarsCount(DbQuery.g_bmIdList.size());
                }
            }
        }
    }

    private void saveResult() {
        DbQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {

                dialogProgress.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this, "Co loi gi do xay ra Vui long thu lai",Toast.LENGTH_SHORT).show();
                dialogProgress.dismiss();
            }
        });
    }

    private void reAttempt() {
        for (int i=0;i<DbQuery.g_quesList.size();i++){
            DbQuery.g_quesList.get(i).setSelectedAns(-1);
            DbQuery.g_quesList.get(i).setStatus(DbQuery.NOT_VISITED);
        }

        Intent intent =new Intent(ScoreActivity.this,StartTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadData() {
        int correctQ=0, wrongQ=0, unattemptQ=0;

        for (int i=0;i<DbQuery.g_quesList.size();i++){
            if (DbQuery.g_quesList.get(i).getSelectedAns() == -1){
                unattemptQ ++;
            } else {
                if (DbQuery.g_quesList.get(i).getSelectedAns() == DbQuery.g_quesList.get(i).getCorrectAns()){
                    correctQ++;
                }
                else {
                    wrongQ++;
                }
            }
        }
        txtCorrectQ.setText(String.valueOf(correctQ));
        txtWrongQ.setText(String.valueOf(wrongQ));
        txtUnAttempt.setText(String.valueOf(unattemptQ));

        txtTotalQ.setText(String.valueOf(DbQuery.g_quesList.size()));

        finalScore = (correctQ*100)/DbQuery.g_quesList.size();
        txtScore.setText(String.valueOf(finalScore));

        timeTaken = getIntent().getLongExtra("TIME_TAKEN",0);

        String time = String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
        );

        txtTime.setText(time);
    }

    private void anhXa() {
        txtScore = findViewById(R.id.txtScore);
        txtTime = findViewById(R.id.txtTime);
        txtTotalQ = findViewById(R.id.totalQ);
        txtCorrectQ = findViewById(R.id.txtRightQ);
        txtWrongQ = findViewById(R.id.txtWrongQ);
        txtUnAttempt = findViewById(R.id.txtUnattempt);
        btnLeaderBoard = findViewById(R.id.leaderboardB);
        btnReAttempt = findViewById(R.id.re_attemptB);
        btnViewAns = findViewById(R.id.view_AnswerB);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}