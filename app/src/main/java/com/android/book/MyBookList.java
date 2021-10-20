package com.android.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.book.databinding.ActivityMyBooksBinding;

import com.android.book.adapter.BookListAdapter;
import com.android.book.adapter.MyBookListAdapter;
import com.android.book.databinding.ActivityBookBinding;
import com.android.book.models.Book;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBookList extends AppCompatActivity {

     ActivityMyBooksBinding activityMyBooksBinding;
    APIInterface apiInterface;
    RecyclerView recyclerView;
    CircularProgressIndicator progressBar;
    List<Book>bookList;
    String userId;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyBooksBinding =  ActivityMyBooksBinding.inflate(getLayoutInflater());
        setContentView(activityMyBooksBinding.getRoot());

        recyclerView = activityMyBooksBinding.recyclerView;
      //  progressBar = activityMyBooksBinding.progressBar;
        sharedPreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        userId = sharedPreferences.getString("user_id","");
        Log.v("user_id",userId);
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);

        getBookList();
    }




    private void getBookList() {



        Call<List<Book>> call = apiInterface.getUserAddedBooks(userId);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                System.out.println("onResponse");
                bookList = response.body();
             //   progressBar.setVisibility(View.GONE);



                if (bookList != null){

                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                    MyBookListAdapter bookListAdapter = new MyBookListAdapter(getApplicationContext(), bookList, new MyBookListAdapter.ItemClickListener() {
                        @Override
                        public void onClickItem(int position) {

                            showAlertBox(position);
                        }
                    });
                    recyclerView.setAdapter(bookListAdapter);

                }else {

                    Toast.makeText(getApplicationContext(), "server is down, try again", Toast.LENGTH_SHORT).show();
                }


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

    private void showAlertBox(int position) {

        new MaterialAlertDialogBuilder(MyBookList.this)
                .setTitle("Alert")
                .setMessage("Do you really want to delete this book from system?")
                .setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        deleteBook(position);

                    }
                })
                .setNegativeButton("No, Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void deleteBook(int position) {




        Call<ResponseBody> call = apiInterface.deleteBook(bookList.get(position).getBookId());

       call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

               Toast.makeText(MyBookList.this, "Deleted", Toast.LENGTH_SHORT).show();
               getBookList();
           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {

               Toast.makeText(MyBookList.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
           }
       });


    }

}