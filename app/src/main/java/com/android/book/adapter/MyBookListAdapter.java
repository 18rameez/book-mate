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
import com.android.book.models.Book;
import com.bumptech.glide.Glide;

import java.util.List;

public class MyBookListAdapter extends RecyclerView.Adapter<MyBookListAdapter.MyViewHolder> {


    public interface ItemClickListener{

        void onClickItem(int position);
    }


    Context context;
    List<Book> bookList;
    ItemClickListener itemClickListener;

    public MyBookListAdapter(Context context, List<Book> bookList, ItemClickListener itemClickListener) {
        this.context = context;
        this.bookList = bookList;
        this.itemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_my_book_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.book_title_view.setText(bookList.get(position).getBookName());
        holder.price_view.setText("₹"+bookList.get(position).getBookPrice());
        holder.publisher_view.setText("Publisher: "+bookList.get(position).getBookAuthor());
        Glide.with(context).load(bookList.get(position).getBookThumbnail()).into(holder.thumbnail);
        holder.seller_name_view.setText("Seller: "+bookList.get(position).getBookSellerName());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemClickListener.onClickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView book_title_view, publisher_view, price_view, seller_name_view ;
        LinearLayout parentLayout;
        ImageView thumbnail;
        TextView deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            parentLayout= itemView.findViewById(R.id.latest_book_parent_layout);
            book_title_view = itemView.findViewById(R.id.book_title);
            publisher_view = itemView.findViewById(R.id.book_publisher);
            seller_name_view = itemView.findViewById(R.id.seller_name);
            price_view = itemView.findViewById(R.id.book_price);
            deleteButton = itemView.findViewById(R.id.delete_button);
            thumbnail = itemView.findViewById(R.id.book_thumbnail);
        }
    }
}
