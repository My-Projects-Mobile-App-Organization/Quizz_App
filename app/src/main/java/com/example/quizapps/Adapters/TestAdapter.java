package com.example.quizapps.Adapters;

import android.content.Intent;
import  android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapps.DbQuery;
import com.example.quizapps.Models.TestModel;
import com.example.quizapps.R;
import com.example.quizapps.StartTestActivity;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {


    private List<TestModel> testList;


    public TestAdapter(List<TestModel> testList) {
        this.testList = testList;
    }




    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        int progress = testList.get(position).getTopScore();
        holder.setData(position, progress);

    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView testNo;
        private ProgressBar progressBar;
        private TextView textScore;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            testNo = (TextView) itemView.findViewById(R.id.testNo);
            progressBar = (ProgressBar) itemView.findViewById(R.id.testProgressBar);
            textScore = (TextView) itemView.findViewById(R.id.scoreText);


        }
        private void setData(int pos, int progress){
            testNo.setText("Test No: " + String.valueOf(pos+1));
            textScore.setText(String.valueOf(progress)+"%");
            progressBar.setProgress(progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DbQuery.g_selected_test_index = pos;
                    Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }
}
