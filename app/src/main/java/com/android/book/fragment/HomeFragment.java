package com.android.book.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.BookActivity;
import com.android.book.BookList;
import com.android.book.MainActivity;
import com.android.book.R;
import com.android.book.adapter.AdapterRecycleView;
import com.android.book.adapter.LatestBooksAdapter;
import com.android.book.models.Book;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    APIInterface apiInterface;
    RecyclerView recyclerPrice, recyclerGenre, recyclerLatest ;
    List<Book> bookList= new ArrayList<>();
    LinearProgressIndicator progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);

        ((MainActivity)getActivity()).changeActionBar("Home");

        recyclerPrice = view.findViewById(R.id.recycler_price);
        recyclerGenre = view.findViewById(R.id.recycler_genre);
        recyclerLatest = view.findViewById(R.id.recycler_latest);
        progressBar =view.findViewById(R.id.progress_bar);

        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);

       getLatestBooks();

        //home recyclerview - using same adapter

        recyclerPrice.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerPrice.setAdapter(new AdapterRecycleView(getContext(), R.layout.recycler_price_layout, new AdapterRecycleView.ItemClickListener() {
            @Override
            public void onPriceClick(String value) {

                Intent intent = new Intent(getContext(), BookList.class);
                intent.putExtra("queryValue",value);
                intent.putExtra("queryType","price");
                startActivity(intent);
            }

            @Override
            public void onGenreClick(String position) {

            }
        }));

        recyclerGenre.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerGenre.setAdapter(new AdapterRecycleView(getContext(), R.layout.recycler_genre_layout, new AdapterRecycleView.ItemClickListener() {
            @Override
            public void onPriceClick(String position) {

            }

            @Override
            public void onGenreClick(String value) {

                Intent intent = new Intent(getContext(), BookList.class);
                intent.putExtra("queryValue",value);
                intent.putExtra("queryType","category");
                startActivity(intent);
            }
        }));


        return view;
    }

    private void getLatestBooks() {

        Call<List<Book>> call = apiInterface.getLatestBooks("empty");

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                System.out.println("onResponse");
                bookList = response.body();
             //   System.out.println(response.body().get(0).getBookName());
                if (response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                }

                recyclerLatest.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                LatestBooksAdapter latestBooksAdapter = new LatestBooksAdapter(getContext(), bookList, new LatestBooksAdapter.ClickListener() {
                    @Override
                    public void onItemClickListener(int position) {

                        Intent intent = new Intent(getContext(),BookActivity.class);
                        intent.putExtra("book_id",bookList.get(position).getBookId());
                        intent.putExtra("seller_name",bookList.get(position).getBookSellerName());
                        startActivity(intent);
                    }
                });
                recyclerLatest.setAdapter(latestBooksAdapter);
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

                String message = t.getMessage();
                Log.d("failure12", message);
                System.out.println("onFailure");
                System.out.println(t.fillInStackTrace());
            }
        });

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
