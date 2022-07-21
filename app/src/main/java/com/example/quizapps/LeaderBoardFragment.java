package com.example.quizapps;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapps.Adapters.RankAdapter;

public class LeaderBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView totalUsersTV, myImgTextTV, myScoreTV, myRankTV;
    private RecyclerView userView;
    private RankAdapter rankAdapter;
    private Dialog dialogProgress;
    private TextView txtDialog;

    public LeaderBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderBoardFragment newInstance(String param1, String param2) {
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_leader_board, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("LeaderBoard");

        anhXa(view);

        dialogProgress = new Dialog(getContext());
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Loading.....");

        dialogProgress.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        userView.setLayoutManager(layoutManager);

        rankAdapter = new RankAdapter(DbQuery.g_users_list);
        userView.setAdapter(rankAdapter);

        DbQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                rankAdapter.notifyDataSetChanged();

                // check neu diem khac 0 ta se xet rank cua ng dung
                if (DbQuery.myPerformance.getScore() != 0){
                    // check neu diem ko trong top dau
                    if (! DbQuery.isMeOnTopList){
                        caclulatorRank();
                    }

                    myScoreTV.setText("Score: " + DbQuery.myPerformance.getScore());
                    myRankTV.setText("Rank: "+ DbQuery.myPerformance.getRank());
                }

                dialogProgress.dismiss();
            }

            @Override
            public void onFailure() {
                dialogProgress.dismiss();
                Toast.makeText(getContext(),"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();

            }
        });
        totalUsersTV.setText("Total Users: " + DbQuery.g_usersCount);
        myImgTextTV.setText(DbQuery.myPerformance.getName().toUpperCase().substring(0,1));

        return view;
    }

    private void anhXa(View view) {
        totalUsersTV = view.findViewById(R.id.total_user);
        myImgTextTV = view.findViewById(R.id.img_text);
        myScoreTV = view.findViewById(R.id.total_scoreLB);
        myRankTV = view.findViewById(R.id.rank);
        userView = view.findViewById(R.id.users_view);

    }
    private void caclulatorRank(){
        int lowTopScore = DbQuery.g_users_list.get(DbQuery.g_users_list.size() -1 ).getScore();
        int remaining_slots = DbQuery.g_usersCount - 20;

        int mySlot = (DbQuery.myPerformance.getScore()*remaining_slots)/lowTopScore;

        int rank;

        if (lowTopScore != DbQuery.myPerformance.getScore()){
            rank = DbQuery.g_usersCount - mySlot;
        }else {
            rank=21;
        }
        DbQuery.myPerformance.setRank(rank);
    }
}