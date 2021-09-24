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

public class AdapterRecycleView extends RecyclerView.Adapter <AdapterRecycleView.ViewHolder> {

    ItemClickListener itemClickListener;
    public interface ItemClickListener{

        void onPriceClick(String value);
        void onGenreClick(String value);


    }

    String [] genreTitle = {"Crime and Thriller", "History", "Romance", "Personal Development"};
    String [] genreTitleId = {"9", "3", "2", "8" };
    String [] priceCategory = {"49","99","149","199"};
    int[] genreImage = {
            R.drawable.crime_thriller,
            R.drawable.history_book,
            R.drawable.romance,
            R.drawable.personal_development
    };

    int layout ;
    Context context;



    public AdapterRecycleView(Context context, int layout, ItemClickListener itemClickListener) {
        this.context = context;
        this.layout= layout;
        this.itemClickListener= itemClickListener;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (layout==R.layout.recycler_price_layout){
            holder.priceView.setText("Under ₹"+priceCategory[position]);
            holder.bind(position,itemClickListener);
        }else if (layout==R.layout.recycler_genre_layout) {
            holder.GenreTitleView.setText(genreTitle[position]);
            holder.genre_image_view.setImageResource(genreImage[position]);
            holder.genreParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onGenreClick(genreTitleId[position]);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView priceView, GenreTitleView;
        ImageView genre_image_view;
        LinearLayout priceParenLayout, genreParentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            priceView = itemView.findViewById(R.id.price_title);
            GenreTitleView = itemView.findViewById(R.id.genre_title);
            genre_image_view = itemView.findViewById(R.id.genre_image_view);
            priceParenLayout = itemView.findViewById(R.id.price_parent_layout);
            genreParentLayout = itemView.findViewById(R.id.genre_parent_layout);

        }

        public void bind(int position, ItemClickListener itemClickListener) {

            priceParenLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    itemClickListener.onPriceClick(priceCategory[position]);
                }
            });

            
        }
    }



}
