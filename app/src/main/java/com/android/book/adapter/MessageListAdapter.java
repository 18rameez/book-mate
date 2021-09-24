package com.android.book.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.R;
import com.android.book.models.FirebaseUserModel;

import java.util.List;
import java.util.ListIterator;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MyViewHolder> {


    Context context;
    List<FirebaseUserModel> users;
    ListIterator<FirebaseUserModel> listIteratorUser;

    OnClickListener onClickListener;
    public  interface  OnClickListener  {
        void onClick(int position);
    }

    public MessageListAdapter(Context context, List<FirebaseUserModel> users,OnClickListener onClickListener) {
        this.context = context;
        this.users=users;
        this.onClickListener=onClickListener;
    }

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_message_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        FirebaseUserModel userModel = users.get(position);
        holder.user_name.setText(userModel.getUsername());
        holder.message_parent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView user_name, last_message;
        LinearLayout message_parent_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name= itemView.findViewById(R.id.user_name);
            last_message= itemView.findViewById(R.id.last_message);
            message_parent_layout  = itemView.findViewById(R.id.message_parent_layout);

        }
    }
}
