package com.android.book.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    RecyclerView categoryRecycler, searchRecyclerView ;
    APIInterface apiInterface;
    List <Category> categoryList =  new ArrayList<>();
    CategoryAdapter categoryAdapter;
    EditText edtSearch;
    String queryValue;
    List<Book>bookList;
    CircularProgressIndicator progressBar;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        categoryRecycler = view.findViewById(R.id.category_recycler);
        searchRecyclerView = view.findViewById(R.id.search_recycler);
        categoryRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);
        progressBar= view.findViewById(R.id.progress_bar);

        ((MainActivity)getActivity()).changeActionBar("Search");



        sharedPreferences = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        userId = sharedPreferences.getString("user_id","");
        Log.v("user_id",userId);

        edtSearch = view.findViewById(R.id.edt_search);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                queryValue = String.valueOf(charSequence);
                searchBook(queryValue);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getAllCategory();

        return view ;
    }

    private void searchBook(String queryValue) {

        progressBar.setVisibility(View.VISIBLE);

        Call<List<Book>> call = apiInterface.searchBook(queryValue,userId);

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {

                System.out.println("onResponse");
                bookList = response.body();
                progressBar.setVisibility(View.GONE);
                categoryRecycler.setVisibility(View.GONE);

                if (bookList != null){

                    searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    BookListAdapter bookListAdapter = new BookListAdapter(getContext(), bookList, new BookListAdapter.OnClickListener() {
                        @Override
                        public void onClick(int position) {

                            Intent intent = new Intent(getContext(),BookActivity.class);
                            intent.putExtra("book_id",bookList.get(position).getBookId());
                            intent.putExtra("seller_name",bookList.get(position).getBookSellerName());
                            startActivity(intent);
                        }
                    });
                    searchRecyclerView.setAdapter(bookListAdapter);
                }else {

                    Toast.makeText(getContext(), "server is down, try again", Toast.LENGTH_SHORT).show();
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

    private void getAllCategory() {


            Call<List<Category>> call = apiInterface.getAllCategory("Token");

            call.enqueue(new Callback<List<Category>>() {
                @Override
                public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                    System.out.println("onResponse");
                    categoryList = response.body();
                //    progressBar.setVisibility(View.GONE);

                    if(categoryList != null) {

                        categoryAdapter = new CategoryAdapter(getContext(), categoryList, new CategoryAdapter.ItemClickListener() {
                            @Override
                            public void onClick(String value) {

                                Intent intent = new Intent(getContext(), BookList.class);
                                intent.putExtra("queryValue", value);
                                intent.putExtra("queryType", "category");
                                startActivity(intent);
                            }
                        });

                        categoryRecycler.setAdapter(categoryAdapter);

                    }else {

                        Toast.makeText(getContext(), "server is down, try again", Toast.LENGTH_SHORT).show();
                    }
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
