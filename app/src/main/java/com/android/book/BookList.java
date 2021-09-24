package com.android.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.book.adapter.BookListAdapter;
import com.android.book.adapter.LatestBooksAdapter;
import com.android.book.databinding.ActivityBookBinding;
import com.android.book.databinding.ActivityBookListBinding;
import com.android.book.models.Book;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookList extends AppCompatActivity {

    APIInterface apiInterface;
    ActivityBookListBinding bookBinding;
    RecyclerView recyclerView;
    CircularProgressIndicator progressBar;
    List<Book>bookList;
    String queryValue, queryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        queryValue=getIntent().getStringExtra("queryValue");
        queryType=getIntent().getStringExtra("queryType");

        bookBinding =  ActivityBookListBinding.inflate(getLayoutInflater());
        setContentView(bookBinding.getRoot());
        recyclerView =bookBinding.recyclerView;
        progressBar=bookBinding.progressBar;
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);

        if (queryType.equalsIgnoreCase("category")){

            getBookList();

        }else if (queryType.equalsIgnoreCase("price")){

            getBookListByPrice();
        }


    }

    private void getBookList() {



        Call<List<Book>> call = apiInterface.getBooksFilter(queryValue);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                System.out.println("onResponse");
                bookList = response.body();
                progressBar.setVisibility(View.GONE);

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                BookListAdapter bookListAdapter = new BookListAdapter(getApplicationContext(), bookList, new BookListAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position) {

                        Intent intent = new Intent(getApplicationContext(),BookActivity.class);
                        intent.putExtra("book_id",bookList.get(position).getBookId());
                        intent.putExtra("seller_name",bookList.get(position).getBookSellerName());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(bookListAdapter);

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

    private void getBookListByPrice() {



        Call<List<Book>> call = apiInterface.getBooksByPrice(queryValue);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                System.out.println("onResponse");
                bookList = response.body();
                progressBar.setVisibility(View.GONE);

                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                BookListAdapter bookListAdapter = new BookListAdapter(getApplicationContext(), bookList, new BookListAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position) {

                        Intent intent = new Intent(getApplicationContext(),BookActivity.class);
                        intent.putExtra("book_id",bookList.get(position).getBookId());
                        intent.putExtra("seller_name",bookList.get(position).getBookSellerName());
                        startActivity(intent);

                    }
                });
                recyclerView.setAdapter(bookListAdapter);

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
    protected void onDestroy() {
        super.onDestroy();
        bookBinding=null;
    }
}