package com.example.myplanningpokeruser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplanningpokeruser.Model.Answer;
import com.example.myplanningpokeruser.Model.User;
import com.example.myplanningpokeruser.R;

import java.util.List;

public class AnswerListRecyclerviewAdapter extends RecyclerView.Adapter<AnswerListRecyclerviewAdapter.MyViewHolder>{

    private Context context;
    private List<User> userList;
    private List<Answer> answerList;


    public AnswerListRecyclerviewAdapter(Context context, List<User> userList, List<Answer> answerList){
        this.answerList = answerList;
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AnswerListRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_list_recyclerview_elements, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerListRecyclerviewAdapter.MyViewHolder holder, int position) {
        holder.answer.setText(answerList.get(position).getAnswer());
        for (int i = 0; i < userList.size(); ++i) {
            if (answerList.get(position).getUser_id().equals(userList.get(i).getId())) {
                holder.name.setText(userList.get(i).getName());
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, answer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_answer_recyclerview_name);
            answer = itemView.findViewById(R.id.textView_answer_recyclerview_answer);
        }
    }

}
