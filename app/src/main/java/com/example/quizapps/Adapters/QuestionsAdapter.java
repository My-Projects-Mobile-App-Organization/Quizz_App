package com.example.quizapps.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapps.DbQuery;
import com.example.quizapps.Models.QuestionModel;
import com.example.quizapps.R;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    private List<QuestionModel> questionList;

    public QuestionsAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtQues;
        private Button btnOptionA, btnOptionB, btnOptionC, btnOptionD, prevSelectedB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQues = itemView.findViewById(R.id.txt_ques);
            btnOptionA = itemView.findViewById(R.id.optionA);
            btnOptionB = itemView.findViewById(R.id.optionB);
            btnOptionC = itemView.findViewById(R.id.optionC);
            btnOptionD = itemView.findViewById(R.id.optionD);

            prevSelectedB = null;
        }
        private void setData(final int pos){
            txtQues.setText(questionList.get(pos).getQuestion());
            btnOptionA.setText(questionList.get(pos).getOptionA());
            btnOptionB.setText(questionList.get(pos).getOptionB());
            btnOptionC.setText(questionList.get(pos).getOptionC());
            btnOptionD.setText(questionList.get(pos).getOptionD());

            setOption(btnOptionA, 1, pos);
            setOption(btnOptionB, 2, pos);
            setOption(btnOptionC, 3, pos);
            setOption(btnOptionD, 4, pos);

            btnOptionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(btnOptionA, 1, pos);
                }
            });

            btnOptionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectOption(btnOptionB, 2, pos);

                }
            });

            btnOptionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(btnOptionC, 3, pos);
                }
            });

            btnOptionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectOption(btnOptionD, 4, pos);
                }
            });
        }
        private void selectOption(Button btn, int option_num, int quesID) {

            if (prevSelectedB == null){

                btn.setBackgroundResource(R.drawable.selected_btn);
                DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);

                changeStatus(quesID, DbQuery.ANSWERD);
                prevSelectedB = btn;

            } else {

                if (prevSelectedB.getId() == btn.getId()){
                    btn.setBackgroundResource(R.drawable.unselected_btn);
                    DbQuery.g_quesList.get(quesID).setSelectedAns(-1);

                    changeStatus(quesID, DbQuery.UNANSWERD);
                    prevSelectedB = null;
                } else {
                    prevSelectedB.setBackgroundResource(R.drawable.unselected_btn);
                    btn.setBackgroundResource(R.drawable.selected_btn);

                    DbQuery.g_quesList.get(quesID).setSelectedAns(option_num);

                    changeStatus(quesID, DbQuery.ANSWERD);
                    prevSelectedB = btn;
                }
            }
        }

        private void changeStatus(int id, int status){
            if ( (DbQuery.g_quesList.get(id).getStatus() != DbQuery.REVIEW)){
                DbQuery.g_quesList.get(id).setStatus(status);
            }
        }

        private void setOption(Button btn, int option_num, int quesID){
            if (DbQuery.g_quesList.get(quesID).getSelectedAns() == option_num){
                btn.setBackgroundResource(R.drawable.selected_btn);
            } else {
                btn.setBackgroundResource(R.drawable.unselected_btn);
            }
        }
    }


}
