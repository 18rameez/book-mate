package com.android.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.R;

public class LatestBooksAdapter extends RecyclerView.Adapter<LatestBooksAdapter.MyViewHolder> {

    Context context;
    String [] bookTitle = {"One Hundred Years of Solitude", "War and Peace", "Pride and Prejudice", "Khasakkinte Ithihasam","Aatujeevitham"};
    String []  bookPrice = {"₹99", "₹150", "₹180", "₹75", "₹350"};

    public LatestBooksAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_latest_book_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.book_title_view.setText(bookTitle[position]);
        holder.price_view.setText(bookPrice[position]);


    }

    @Override
    public int getItemCount() {
        return bookTitle.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView book_title_view, publisher_view, price_view ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            book_title_view = itemView.findViewById(R.id.book_title);
            publisher_view = itemView.findViewById(R.id.book_publisher);
            price_view = itemView.findViewById(R.id.book_price);
        }
    }
}
