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

import java.util.List;

public class HomePageRecyclerviewAdapter extends RecyclerView.Adapter<HomePageRecyclerviewAdapter.MyViewHolder> {

    private Context context;
    private List<Questions> questionsList;
    private OnItemClickListener listener;

    public HomePageRecyclerviewAdapter(Context context, List<Questions> questionsList){
        this.context = context;
        this.questionsList = questionsList;
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
        if (questionsList.get(position).isActive()){
            holder.textView_active.setText(R.string.active);
            holder.textView_active.setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        } else {
            holder.textView_active.setText(R.string.inactive);
            holder.textView_active.setTextColor(ContextCompat.getColor(context,R.color.colorAccent));

        }
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
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
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
