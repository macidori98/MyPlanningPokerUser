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
    private List<Questions> questionsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_page_fragment, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView_home_page_group = view.findViewById(R.id.recyclerview_home_page_groups);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference(Constant.QUESTIONS);
        questionsList = new ArrayList<>();
        getQuestions();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView_home_page_group.setLayoutManager(linearLayoutManager);

    }

    private void getQuestions(){
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child(Constant.GROUP_ID).getValue().toString().equals(Constant.ENTERED_GROUP.getId())){
                        boolean active = Boolean.valueOf(snapshot.child(Constant.ACTIVE).getValue().toString());
                        String date_from = snapshot.child(Constant.DATE_FROM).getValue().toString();
                        String date_until = snapshot.child(Constant.DATE_UNTIL).getValue().toString();
                        String group_id = snapshot.child(Constant.GROUP_ID).getValue().toString();
                        String id = snapshot.child(Constant.ID).getValue().toString();
                        String question = snapshot.child(Constant.QUESTION).getValue().toString();

                        Questions questions = new Questions(id,group_id,question,date_from, date_until, active);

                        questionsList.add(questions);
                    }
                }

                mAdapter = new HomePageRecyclerviewAdapter(getContext(), questionsList);
                mAdapter.setOnClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), String.valueOf(position) ,Toast.LENGTH_SHORT).show();
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
