package com.example.myplanningpokeruser.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplanningpokeruser.Interface.OnItemClickListener;
import com.example.myplanningpokeruser.R;
import com.example.myplanningpokeruser.Utils.Constant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.collection.LLRBNode;

import java.util.ArrayList;
import java.util.List;

public class AnswerRecyclerviewAdapter extends RecyclerView.Adapter<AnswerRecyclerviewAdapter.MyViewHolder> {

    private List<String> mData; // = {"0", "1", "2", "3", "5", "8", "13", "20", "40", "100", "Coffee"};
    private LayoutInflater mInflater;
    private TextView users_answer;
    private OnItemClickListener mClickListener;
    private Context context;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private List<AnswerRecyclerviewAdapter.MyViewHolder> holders;

    public AnswerRecyclerviewAdapter(Context context, List<String> data, TextView users_answer){
        this.context = context;
        this.mData = data;
        this.users_answer = users_answer;
        this.holders = new ArrayList<>();
    }

    @NonNull
    @Override
    public AnswerRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_fragment_recyclerview_element, parent, false);
        return new MyViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerRecyclerviewAdapter.MyViewHolder holder, int position) {
        holders.add(holder);
        holder.textView_element.setText(mData.get(position));
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener{

        public TextView textView_element;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textView_element = itemView.findViewById(R.id.textView_answer_recyclerview_element_answer);
        }

        public void bind(final int position){
            textView_element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (AnswerRecyclerviewAdapter.MyViewHolder holder : holders) {
                        holder.textView_element.setBackgroundColor(Color.WHITE);
                    }

                    holders.get(position).textView_element.setBackgroundColor(Color.GREEN);
                        Constant.CHOSEN_ELEMENT = mData.get(position);
                        String text = "Your answer is: ".concat(mData.get(position));
                        users_answer.setText(text);
                }
            });
        }

        @Override
        public void onItemClick(int position) {
            if (mClickListener != null) mClickListener.onItemClick( getAdapterPosition());

        }
    }
}
