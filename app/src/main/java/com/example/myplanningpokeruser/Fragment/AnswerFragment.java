package com.example.myplanningpokeruser.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.view.KeyEvent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnswerFragment extends Fragment {

    public final static String TAG = AnswerFragment.class.getSimpleName();

    private View view;
    private Button button_submit;
    private RecyclerView recyclerView_vote_table;
    private TextView question, users_answer, timer;
    private AnswerRecyclerviewAdapter mAdapter;
    private List<String> data;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef, mRefQuestion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.answer_fragment, container, false);
        button_submit = view.findViewById(R.id.button_answer_submit);
        timer = view.findViewById(R.id.timer);
        question = view.findViewById(R.id.textView_answer_question);
        recyclerView_vote_table = view.findViewById(R.id.recyclerview_answer);
        users_answer = view.findViewById(R.id.textView_answer_answer);
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
        mRefQuestion = mDatabase.getReference(Constant.QUESTIONS);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CountDownTimer countDownTimer;
        countDownTimer = new CountDownTimer(Constant.SELECTED_QUESTION.getActive_time_seconds()*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                String KEY = mRef.push().getKey();
                String question_id = Constant.SELECTED_QUESTION.getId();
                String user_id = Constant.CURRENT_USER.getId();
                String answer = "Time is up!";

                Answer answer1 = new Answer(KEY, question_id, user_id, answer);
                mRef.child(KEY).setValue(answer1);
                Toast.makeText(getContext(), R.string.time_up, Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        }.start();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    countDownTimer.cancel();
                    String KEY = mRef.push().getKey();
                    String question_id = Constant.SELECTED_QUESTION.getId();
                    String user_id = Constant.CURRENT_USER.getId();
                    String answer = "Time is up!";

                    Answer answer1 = new Answer(KEY, question_id, user_id, answer);
                    mRef.child(KEY).setValue(answer1);
                }
                return false;
            }
        });

        question.setText(Constant.SELECTED_QUESTION.getQuestion());
        recyclerView_vote_table.setLayoutManager(new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL, false));
        mAdapter = new AnswerRecyclerviewAdapter(getContext(), data, users_answer);
        recyclerView_vote_table.setAdapter(mAdapter);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.CHOSEN_ELEMENT!=""){
                    mRefQuestion.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                if (snapshot.child(Constant.ID).getValue().toString().equals(Constant.SELECTED_QUESTION.getId())){
                                    if(Boolean.valueOf(snapshot.child(Constant.ACTIVE).getValue().toString())){
                                        countDownTimer.cancel();
                                        String KEY = mRef.push().getKey();
                                        String question_id = Constant.SELECTED_QUESTION.getId();
                                        String user_id = Constant.CURRENT_USER.getId();
                                        String answer = Constant.CHOSEN_ELEMENT;

                                        Answer answer1 = new Answer(KEY, question_id, user_id, answer);
                                        mRef.child(KEY).setValue(answer1);

                                        Toast.makeText(getContext(), R.string.answer_sent, Toast.LENGTH_SHORT).show();
                                        getActivity().onBackPressed();
                                    } else {
                                        countDownTimer.cancel();
                                        Toast.makeText(getContext(), R.string.answer_not_sent, Toast.LENGTH_SHORT).show();
                                        getActivity().onBackPressed();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(getContext(), R.string.choose_element, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
