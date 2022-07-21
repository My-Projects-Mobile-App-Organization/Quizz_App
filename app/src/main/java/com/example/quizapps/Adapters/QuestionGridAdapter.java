package com.example.quizapps.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.quizapps.DbQuery;
import com.example.quizapps.QuestionActivity;
import com.example.quizapps.R;

public class QuestionGridAdapter extends BaseAdapter {

    private int numOfTest;
    private Context context;

    public QuestionGridAdapter(Context context, int numOfTest) {
        this.context = context;
        this.numOfTest = numOfTest;
    }

    @Override
    public int getCount() {
        return numOfTest;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView( final int i, View view, ViewGroup viewGroup) {
        View myView;
        if (view == null){
            myView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ques_grid_item, viewGroup, false);
        } else {
            myView = view;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof QuestionActivity){
                    ((QuestionActivity) context).goToQuestion(i);
                }
            }
        });

        TextView txtques = myView.findViewById(R.id.ques_num);
        txtques.setText(String.valueOf(i+1));

        switch (DbQuery.g_quesList.get(i).getStatus()){
            case DbQuery.NOT_VISITED:
                txtques.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.grey)));
                break;
            case DbQuery.UNANSWERD:
                txtques.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.red)));
                break;
            case DbQuery.ANSWERD:
                txtques.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.green)));
                break;
            case DbQuery.REVIEW:
                txtques.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(), R.color.pink)));
                break;

        }

        return myView;
    }
}
