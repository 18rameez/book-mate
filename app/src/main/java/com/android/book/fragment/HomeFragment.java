package com.android.book.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.BookActivity;
import com.android.book.MainActivity;
import com.android.book.R;
import com.android.book.adapter.AdapterRecycleView;
import com.android.book.adapter.LatestBooksAdapter;
import com.android.book.models.Book;
import com.android.book.models.User;
import com.android.book.utils.APIError;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    APIInterface apiInterface;
    RecyclerView recyclerPrice, recyclerGenre, recyclerLatest ;
    List<Book> bookList= new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);

        ((MainActivity)getActivity()).changeActionBar("Home");

        recyclerPrice = view.findViewById(R.id.recycler_price);
        recyclerGenre = view.findViewById(R.id.recycler_genre);
        recyclerLatest = view.findViewById(R.id.recycler_latest);


        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);

     //   getUserDetails();
        getLatestBooks();




        //home recyclerview - using same adapter

        recyclerLatest.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        recyclerLatest.setAdapter(new LatestBooksAdapter(getContext()));
         // recyclerLatest.setNestedScrollingEnabled(false);
         //  recyclerLatest.hasFixedSize();

        recyclerPrice.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerPrice.setAdapter(new AdapterRecycleView(getContext(), R.layout.recycler_price_layout, new AdapterRecycleView.ItemClickListener() {
            @Override
            public void onPriceClick(int position) {

                Intent intent = new Intent(getContext(), BookActivity.class);
                startActivity(intent);
            }

            @Override
            public void onGenreClick(int position) {

            }

            @Override
            public void onLanguageClick(int position) {

            }
        }));

        recyclerGenre.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerGenre.setAdapter(new AdapterRecycleView(getContext(), R.layout.recycler_genre_layout, new AdapterRecycleView.ItemClickListener() {
            @Override
            public void onPriceClick(int position) {

            }

            @Override
            public void onGenreClick(int position) {

            }

            @Override
            public void onLanguageClick(int position) {

            }
        }));


        return view;
    }

    private void getLatestBooks() {

        Call<List<Book>> call = apiInterface.getLatestBooks("empty");

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                System.out.println("onResponse");
                bookList = response.body();
                System.out.println(response.body().get(0).getBookName());
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

    private void getUserDetails() {

        Call<User> call = apiInterface.getUser("0");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {
                    // Do your success stuff...
                    if (getContext()!= null) {

                    }

                } else {
                    Toast.makeText(getContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();

                }
                //Log.w("Full json res => ",new Gson().toJson(response));
                System.out.println("onResponse");
                System.out.println(response.body().toString());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                String message = t.getMessage();
                Log.d("failure12", t.toString());
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
