package com.example.quizapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quizapps.Adapters.QuestionGridAdapter;
import com.example.quizapps.Adapters.QuestionsAdapter;

import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity {

    private RecyclerView quesView;
    private TextView txtQuesID, txtTime, txtCatname;
    private Button btnSubmit, btnMark, btnClearSelect;
    private ImageButton ibtnPrevQues, ibtnNextQues;
    private ImageView quesListB;
    private QuestionsAdapter questionsAdapter;
    private ImageButton ibtncloseDrawer;
    private GridView queslistGridv;
    private ImageView img_marked;
    private CountDownTimer countDownTimer;
    private ImageView imgBookmarkbtn;

    private QuestionGridAdapter questionGridAdapter;

    private DrawerLayout drawerLayout;

    // lấy time làm bài
    private long timeLeft;

    private int quesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list_layout);

        anhXa();

         questionsAdapter = new QuestionsAdapter(DbQuery.g_quesList);
        quesView.setAdapter(questionsAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        quesView.setLayoutManager(layoutManager);

        questionGridAdapter = new QuestionGridAdapter(this,DbQuery.g_quesList.size());
        queslistGridv.setAdapter(questionGridAdapter);

        // set view cho từng câu hỏi
        setSnapHelper();

        setClickListeners();

        startTimer();
    }

    public void goToQuestion(int pos){
        quesView.smoothScrollToPosition(pos);

        if (drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
    }

    private void startTimer() {
        long totalTime = DbQuery.g_testModels.get(DbQuery.g_selected_test_index).getTime() * 60 * 1000;

         countDownTimer = new CountDownTimer(totalTime + 1000,1000) {
            @Override
            public void onTick(long remainingTime) {

                timeLeft = remainingTime;

                String time = String.format("%02d:%02d min",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime))
                        );
                txtTime.setText(time);

            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);

                long totalTime = DbQuery.g_testModels.get(DbQuery.g_selected_test_index).getTime()*60*1000;
                intent.putExtra("TIME_TAKEN",totalTime-timeLeft);
                startActivity(intent);
                QuestionActivity.this.finish();
            }
        };
        countDownTimer.start();
    }

    private void setClickListeners() {
        ibtnPrevQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quesId > 0){
                    quesView.smoothScrollToPosition(quesId - 1);
                }

            }
        });

        ibtnNextQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (quesId < DbQuery.g_quesList.size() - 1){
                    quesView.smoothScrollToPosition(quesId + 1);
                }

            }
        });

        btnClearSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DbQuery.g_quesList.get(quesId).setSelectedAns(-1);
                DbQuery.g_quesList.get(quesId).setStatus(DbQuery.UNANSWERD);
                img_marked.setVisibility(View.GONE);
                questionsAdapter.notifyDataSetChanged();
            }
        });

        quesListB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! drawerLayout.isDrawerOpen(GravityCompat.END)){

                    questionGridAdapter.notifyDataSetChanged();
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        ibtncloseDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        btnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (img_marked.getVisibility() != View.VISIBLE){
                    img_marked.setVisibility(View.VISIBLE);

                    DbQuery.g_quesList.get(quesId).setStatus(DbQuery.REVIEW);
                }
                else {
                    img_marked.setVisibility(View.GONE);

                    if (DbQuery.g_quesList.get(quesId).getSelectedAns() != -1){
                        DbQuery.g_quesList.get(quesId).setStatus(DbQuery.ANSWERD);
                    } else {
                        DbQuery.g_quesList.get(quesId).setStatus(DbQuery.UNANSWERD);
                    }
                }

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTest();
            }
        });

        imgBookmarkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBookmark();
            }
        });
    }

    private void addToBookmark() {
        if (DbQuery.g_quesList.get(quesId).isBookmark()){
            DbQuery.g_quesList.get(quesId).setBookmark(false);
            imgBookmarkbtn.setImageResource(R.drawable.ic_bookmark);
        } else {
            DbQuery.g_quesList.get(quesId).setBookmark(true);
            imgBookmarkbtn.setImageResource(R.drawable.ic_bookmark_selected);
        }
    }

    private void submitTest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout, null);

        Button btnCancel = view.findViewById(R.id.btn_Cancel);
        Button btnConfirm = view.findViewById(R.id.btn_confirmB);

        builder.setView(view);

         final AlertDialog alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                alertDialog.cancel();

                Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
                long totalTime = DbQuery.g_testModels.get(DbQuery.g_selected_test_index).getTime()*60*1000;
                intent.putExtra("TIME_TAKEN",totalTime-timeLeft);

                startActivity(intent);
                QuestionActivity.this.finish();
            }
        });
        alertDialog.show();
    }

    private void setSnapHelper() {

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(quesView);

        quesView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());

                quesId = recyclerView.getLayoutManager().getPosition(view);

                if (DbQuery.g_quesList.get(quesId).getStatus() == DbQuery.NOT_VISITED){
                    DbQuery.g_quesList.get(quesId).setStatus(DbQuery.UNANSWERD);
                }

                if (DbQuery.g_quesList.get(quesId).getStatus() == DbQuery.REVIEW){
                    img_marked.setVisibility(View.VISIBLE);

                } else {
                    img_marked.setVisibility(View.GONE);
                }
                txtQuesID.setText(String.valueOf(quesId + 1) + "/" + String.valueOf(DbQuery.g_quesList.size()));

                // check bookmark prev next
                if (DbQuery.g_quesList.get(quesId).isBookmark()){
                    imgBookmarkbtn.setImageResource(R.drawable.ic_bookmark_selected);
                }
                else {
                    imgBookmarkbtn.setImageResource(R.drawable.ic_bookmark);
                }

            }



            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void anhXa() {
        quesView = findViewById(R.id.question_view);
        txtQuesID = findViewById(R.id.txt_ques_ID);
        txtTime = findViewById(R.id.txt_time);
        txtCatname = findViewById(R.id.qa_catName);
        btnSubmit = findViewById(R.id.btn_submit);
        btnMark = findViewById(R.id.markB);
        btnClearSelect = findViewById(R.id.clear_selB);
        ibtnNextQues = findViewById(R.id.next_quesB);
        ibtnPrevQues = findViewById(R.id.prev_quesB);
        quesListB = findViewById(R.id.ques_list_view);
        drawerLayout = findViewById(R.id.ques_list_drawer_layout);
        ibtncloseDrawer = findViewById(R.id.drawercloseB);
        img_marked = findViewById(R.id.img_marked);
        queslistGridv = findViewById(R.id.ques_list_gridv);
        imgBookmarkbtn =findViewById(R.id.qa_bookmarkB);


        quesId=0;
        txtQuesID.setText("1/" + String.valueOf(DbQuery.g_quesList.size()));
        txtCatname.setText(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());

        DbQuery.g_quesList.get(0).setStatus(DbQuery.UNANSWERD);

        if (DbQuery.g_quesList.get(0).isBookmark()){
            imgBookmarkbtn.setImageResource(R.drawable.ic_bookmark_selected);
        }
        else {
            imgBookmarkbtn.setImageResource(R.drawable.ic_bookmark);
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.dialog_cancel_test_layout, null);

        Button btnCancel = view.findViewById(R.id.btn_Cancel_Dialog);
        Button btnConfirm = view.findViewById(R.id.btn_Exit_Test);

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countDownTimer.cancel();
                alertDialog.cancel();

                QuestionActivity.this.finish();
            }
        });
        alertDialog.show();

    }
}