package com.android.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.R;
import com.android.book.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {


    ItemClickListener itemClickListener;
    public interface ItemClickListener{

      void  onClick(String value);

    }


    Context context ;
    List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList,ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.context = context;
        this.categoryList = categoryList;
    }

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_genre_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Category category = categoryList.get(position);
        holder.GenreTitleView.setText(category.getCategoryName());
        holder.genre_image_view.setImageResource( R.drawable.personal_development);
        holder.genreParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(category.getCategoryId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class MyViewHolder  extends RecyclerView.ViewHolder{

        TextView  GenreTitleView;
        ImageView genre_image_view;
        LinearLayout  genreParentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            GenreTitleView = itemView.findViewById(R.id.genre_title);
            genre_image_view = itemView.findViewById(R.id.genre_image_view);
            genreParentLayout = itemView.findViewById(R.id.genre_parent_layout);

        }
    }
}
