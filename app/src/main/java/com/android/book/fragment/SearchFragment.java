package com.android.book.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.MainActivity;
import com.android.book.R;
import com.android.book.adapter.AdapterRecycleView;

public class SearchFragment extends Fragment {

    RecyclerView categoryRecycler ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search,container,false);

        categoryRecycler = view.findViewById(R.id.category_recycler);
        categoryRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));

        ((MainActivity)getActivity()).changeActionBar("Search");

        categoryRecycler.setAdapter(new AdapterRecycleView(getContext(), R.layout.recycler_genre_layout, new AdapterRecycleView.ItemClickListener() {
            @Override
            public void onPriceClick(int position) {

            }
            @Override
            public void onGenreClick(int position) {

            }

        }));

        return view ;
    }
}
