package com.example.quizapps;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private LinearLayout btnLogout;

    private TextView profile_img_text, name, score, rank;
    private LinearLayout leaderB, profileB, bookmarksB;

    private BottomNavigationView bottomNavigationView;
    private Dialog dialogProgress;
    private TextView txtDialog;




    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        anhXa(view);

        dialogProgress = new Dialog(getContext());
        dialogProgress.setContentView(R.layout.dialog_layout);
        dialogProgress.setCancelable(false);
        dialogProgress.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        txtDialog = (TextView) dialogProgress.findViewById(R.id.dialog_text);
        txtDialog.setText("Loading.....");



        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");

        String userName = DbQuery.myprofileModel.getName();

        profile_img_text.setText(userName.toUpperCase().substring(0,1));
        name.setText(userName);

        score.setText(String.valueOf(DbQuery.myPerformance.getScore()));

       // rank.setText();
        if (DbQuery.g_users_list.size() == 0){
            dialogProgress.show();
            DbQuery.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    // check neu diem khac 0 ta se xet rank cua ng dung
                    if (DbQuery.myPerformance.getScore() != 0){
                        // check neu diem ko trong top dau
                        if (! DbQuery.isMeOnTopList){
                            caclulatorRank();
                        }

                        score.setText("Score: " + DbQuery.myPerformance.getScore());
                        rank.setText("Rank: "+ DbQuery.myPerformance.getRank());
                    }

                    dialogProgress.dismiss();
                }

                @Override
                public void onFailure() {
                    dialogProgress.dismiss();
                    Toast.makeText(getContext(),"Da co loi gi do, vui long thu lai",Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            score.setText("Score: " + DbQuery.myPerformance.getScore());
            if (DbQuery.myPerformance.getScore() != 0)
            rank.setText("Rank: "+ DbQuery.myPerformance.getRank());
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("1013918394720-g43tcolpkgkr27fgu46r21qihr2rbj7r.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(view.getContext(), gso);

                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent intent = new Intent(getContext(),LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        getActivity().finish();
                    }
                });
            }
        });


        bookmarksB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),BookmarksActivity.class);
                startActivity(intent);
            }
        });

        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), MyProfileActivity.class);
                startActivity(intent);

            }
        });

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomNavigationView.setSelectedItemId(R.id.navigation_leaderboard);
            }
        });

        return  view;
    }

    private void anhXa(View view) {
        btnLogout = (LinearLayout) view.findViewById(R.id.logout_B);
        profile_img_text = view.findViewById(R.id.txtProFile);
        name = view.findViewById(R.id.txtName_Account);
        rank = view.findViewById(R.id.rank);
        score = view.findViewById(R.id.total_score);
        leaderB = (LinearLayout) view.findViewById(R.id.leader_B);
        bookmarksB = (LinearLayout) view.findViewById(R.id.bookmark_B);
        profileB = (LinearLayout) view.findViewById(R.id.profile_B);

        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);
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