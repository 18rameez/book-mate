package com.android.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.R;
import com.android.book.models.Category;

import java.util.List;

public class BottomSheetRecyclerAdapter extends RecyclerView.Adapter<BottomSheetRecyclerAdapter.MyViewHolder> {


    public interface ItemClickListener {
      void onItemClick(int position);
    }


    List<Category> categoryList;
    ItemClickListener itemClickListener;

    public BottomSheetRecyclerAdapter(List<Category> categoryList, ItemClickListener itemClickListener) {

        this.categoryList = categoryList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_sheet_item_layout,parent,false);
      return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.category_item_view.setText(categoryList.get(position).getCategoryName());
        holder.category_item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (categoryList.size()!=0){
          return  categoryList.size();
        }
         return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView category_item_view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            category_item_view = itemView.findViewById(R.id.category_item);

        }
    }
}
