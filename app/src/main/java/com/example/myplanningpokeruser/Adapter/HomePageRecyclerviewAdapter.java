package com.example.myplanningpokeruser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplanningpokeruser.Model.Questions;
import com.example.myplanningpokeruser.Interface.OnItemClickListener;
import com.example.myplanningpokeruser.R;

import java.util.ArrayList;
import java.util.List;

public class HomePageRecyclerviewAdapter extends RecyclerView.Adapter<HomePageRecyclerviewAdapter.MyViewHolder> {

    private Context context;
    private List<Questions> questionsList;
    private OnItemClickListener listener;
    private ArrayList<HomePageRecyclerviewAdapter.MyViewHolder> holders;
    private List<String> answered_question_ids;

    public HomePageRecyclerviewAdapter(Context context, List<Questions> questionsList, List<String> answered_question_ids){
        this.context = context;
        this.questionsList = questionsList;
        this.holders = new ArrayList<>();
        this.answered_question_ids = answered_question_ids;
    }

    @NonNull
    @Override
    public HomePageRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_page_fragment_recylcerview__group_elements, parent, false);
        return new MyViewHolder(itemView,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageRecyclerviewAdapter.MyViewHolder holder, int position) {
        holder.textView_question.setText(questionsList.get(position).getQuestion());

        if (answered_question_ids.contains(questionsList.get(position).getId())){
            holder.textView_active.setText(R.string.answered);
            holder.textView_active.setTextColor(ContextCompat.getColor(context,R.color.red));
            holder.itemView.setOnClickListener(null);
        } else if (questionsList.get(position).isActive()){
            holder.textView_active.setText(R.string.active);
            holder.textView_active.setTextColor(ContextCompat.getColor(context,R.color.green));
        } else {
            holder.textView_active.setText(R.string.inactive);
            holder.textView_active.setTextColor(ContextCompat.getColor(context,R.color.red));
            holder.itemView.setOnClickListener(null);
        }

        holders.add(holder);
    }

    public void QuestionAnswered(int position){
        holders.get(position).textView_active.setText(R.string.answered);
    }

    public void ResetList(){
        this.questionsList.clear();
        this.answered_question_ids.clear();
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView_question, textView_active;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView_active = itemView.findViewById(R.id.textView_home_page_active);
            textView_question = itemView.findViewById(R.id.textView_home_page_question);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {//&& textView_active.getText().toString().equals(R.string.active)){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public void setOnClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
