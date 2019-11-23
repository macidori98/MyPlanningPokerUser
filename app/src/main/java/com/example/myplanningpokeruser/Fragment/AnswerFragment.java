package com.example.myplanningpokeruser.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplanningpokeruser.Adapter.AnswerRecyclerviewAdapter;
import com.example.myplanningpokeruser.Model.Answer;
import com.example.myplanningpokeruser.R;
import com.example.myplanningpokeruser.Utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AnswerFragment extends Fragment {

    public final static String TAG = AnswerFragment.class.getSimpleName();

    private View view;
    private Button button_submit;
    private RecyclerView recyclerView_vote_table;
    private TextView question;
    private AnswerRecyclerviewAdapter mAdapter;
    private List<String> data;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.answer_fragment, container, false);
        button_submit = view.findViewById(R.id.button_answer_submit);
        question = view.findViewById(R.id.textView_answer_question);
        recyclerView_vote_table = view.findViewById(R.id.recyclerview_answer);
        mDatabase = FirebaseDatabase.getInstance();
        data = new ArrayList<>();
        data.add("0");
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("5");
        data.add("8");
        data.add("13");
        data.add("20");
        data.add("40");
        data.add("100");
        data.add("Coffee");
        mRef = mDatabase.getReference(Constant.ANSWER);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        question.setText(Constant.SELECTED_QUESTION.getQuestion());
        recyclerView_vote_table.setLayoutManager(new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL, false));
        mAdapter = new AnswerRecyclerviewAdapter(getContext(), data);
        recyclerView_vote_table.setAdapter(mAdapter);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.CHOSEN_ELEMENT!=""){
                    String KEY = mRef.push().getKey();
                    String question_id = Constant.SELECTED_QUESTION.getId();
                    String user_id = Constant.CURRENT_USER.getId();
                    String answer = Constant.CHOSEN_ELEMENT;

                    Answer answer1 = new Answer(KEY, question_id, user_id, answer);
                    mRef.child(KEY).setValue(answer1);

                    Toast.makeText(getContext(), "answer sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
