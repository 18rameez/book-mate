package com.android.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.R;
import com.android.book.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public  class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int MESSAGE_TYPE_LEFT = 0;
    public static final int MESSAGE_TYPE_RIGHT = 1;
    Context context;
    List<Message> message;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Message> message) {
        this.context = context;
        this.message = message;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MESSAGE_TYPE_RIGHT){

            View view = LayoutInflater.from(context).inflate(R.layout.right_message_layout,parent,false);
            return new MyViewHolder(view);

        }else {

            View view = LayoutInflater.from(context).inflate(R.layout.left_message_layout,parent,false);
            return new MyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Message singleMessage = message.get(position);
        holder.message_view.setText(singleMessage.getMessage());

    }

    @Override
    public int getItemCount() {
        return message.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView message_view ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            message_view = itemView.findViewById(R.id.message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (message.get(position).getSender().equals(firebaseUser.getUid())){
            return  MESSAGE_TYPE_RIGHT ;
        }else  {
            return  MESSAGE_TYPE_LEFT;
        }
    }
}
