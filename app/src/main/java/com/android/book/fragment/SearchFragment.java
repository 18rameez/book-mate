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
import com.android.book.adapter.BookListAdapter;
import com.android.book.adapter.CategoryAdapter;
import com.android.book.models.Book;
import com.android.book.models.Category;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    RecyclerView categoryRecycler ;
    APIInterface apiInterface;
    List <Category> categoryList =  new ArrayList<>();
    CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        categoryRecycler = view.findViewById(R.id.category_recycler);
        categoryRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);

        ((MainActivity)getActivity()).changeActionBar("Search");



        getAllCategory();

        return view ;
    }

    private void getAllCategory() {


            Call<List<Category>> call = apiInterface.getAllCategory("Token");

            call.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                    System.out.println("onResponse");
                    categoryList = response.body();
                //    progressBar.setVisibility(View.GONE);

                    categoryAdapter = new CategoryAdapter(getContext(), categoryList, new CategoryAdapter.ItemClickListener() {
                        @Override
                        public void onClick(String value) {

                            Intent intent = new Intent(getContext(), BookList.class);
                            intent.putExtra("queryValue",value);
                            intent.putExtra("queryType","category");
                            startActivity(intent);
                        }
                    });

                    categoryRecycler.setAdapter(categoryAdapter);

                }

                @Override
                public void onFailure(Call<List<Category>> call, Throwable t) {

                    String message = t.getMessage();
                    Log.d("failure12", message);
                    System.out.println("onFailure");
                    System.out.println(t.fillInStackTrace());
                }
            });


    }
}
