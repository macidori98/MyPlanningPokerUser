package com.example.myplanningpokeruser.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplanningpokeruser.Adapter.HomePageRecyclerviewAdapter;
import com.example.myplanningpokeruser.Interface.OnItemClickListener;
import com.example.myplanningpokeruser.Model.Questions;
import com.example.myplanningpokeruser.R;
import com.example.myplanningpokeruser.Utils.Constant;
import com.example.myplanningpokeruser.Utils.FragmentNavigation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment {

    public static final String TAG = HomePageFragment.class.getSimpleName();

    private View view;
    private RecyclerView recyclerView_home_page_group;
    private HomePageRecyclerviewAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private DatabaseReference answerRef;
    private List<Questions> questionsList;
    private List<String> answered_question_ids;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_page_fragment, container, false);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(Constant.QUESTIONS);
        answerRef = mDatabase.getReference(Constant.ANSWER);
        questionsList = new ArrayList<>();
        answered_question_ids = new ArrayList<>();

        getUserAnswers();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getUserAnswers(){
        answerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    if (snapshot.child(Constant.USER_ID).getValue().toString()
                    .equals(Constant.CURRENT_USER.getId())){
                        answered_question_ids.add(snapshot.child(Constant.QUESTION_ID).getValue().toString());
                    }
                }

                getQuestions();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getQuestions(){

        mRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child(Constant.GROUP_ID).getValue().toString().equals(Constant.ENTERED_GROUP.getId())){
                        boolean found = false;
                        boolean active = Boolean.valueOf(snapshot.child(Constant.ACTIVE).getValue().toString());
                        int active_time = Integer.valueOf(snapshot.child(Constant.ACTIVE_TIME_SECONDS).getValue().toString());
                        String group_id = snapshot.child(Constant.GROUP_ID).getValue().toString();
                        String id = snapshot.child(Constant.ID).getValue().toString();
                        String question = snapshot.child(Constant.QUESTION).getValue().toString();

                        Questions questions = new Questions(id,group_id,question, active, active_time);

                        for (int i = 0; i < questionsList.size(); ++i){
                            if (questionsList.get(i).getId().equals(questions.getId())){
                                questionsList.get(i).setActive(questions.isActive());
                                found = true;
                                break;
                            }
                        }
                        if (!found){
                            questionsList.add(questions);
                        }
                    }
                }
                linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView_home_page_group = view.findViewById(R.id.recyclerview_home_page_groups);
                recyclerView_home_page_group.setLayoutManager(linearLayoutManager);
                mAdapter = new HomePageRecyclerviewAdapter(getContext(), questionsList, answered_question_ids);
                mAdapter.setOnClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (questionsList.get(position).isActive()){
                            Constant.SELECTED_QUESTION = questionsList.get(position);
                            //Toast.makeText(getContext(), R.string.active ,Toast.LENGTH_SHORT).show();
                            FragmentNavigation.getInstance(getContext()).replaceFragment(new AnswerFragment(), R.id.fragment_content);

                        } else {
                            Toast.makeText(getContext(), R.string.inactive ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                recyclerView_home_page_group.setAdapter(mAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
