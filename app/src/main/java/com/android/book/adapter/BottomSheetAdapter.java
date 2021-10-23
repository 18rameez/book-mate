package com.android.book.adapter;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.R;
import com.android.book.models.Category;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public  class BottomSheetAdapter extends BottomSheetDialogFragment {

    private BottomSheetRecyclerAdapter.ItemClickListener itemClickListener;
    private List<Category>categoryList;

    public BottomSheetAdapter(BottomSheetRecyclerAdapter.ItemClickListener itemClickListener, List<Category> categoryList) {
        this.itemClickListener = itemClickListener;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_layout,null);
        bottomSheetDialog.setContentView(view);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        BottomSheetRecyclerAdapter bottomSheetRecyclerAdapter = new BottomSheetRecyclerAdapter(categoryList,itemClickListener);
        recyclerView.setAdapter(bottomSheetRecyclerAdapter);


        return  bottomSheetDialog;
    }
}
