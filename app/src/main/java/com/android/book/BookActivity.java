package com.android.book;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.book.databinding.ActivityBookBinding;
import com.android.book.models.Book;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookActivity extends AppCompatActivity {

    ActivityBookBinding bookBinding;
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bookBinding =  ActivityBookBinding.inflate(getLayoutInflater());
        setContentView(bookBinding.getRoot());

        apiInterface= RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);
        getBookDetails();

    }

    private void getBookDetails() {

        Call<Book> call = apiInterface.getBookDetails("532");
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {

                Toast.makeText(getApplicationContext(), response.body().getBookName(), Toast.LENGTH_SHORT).show();
                System.out.println("onResponse");

            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {

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