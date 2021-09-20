package com.android.book.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.book.LoginActivity;
import com.android.book.MainActivity;
import com.android.book.R;

public class UserFragment extends Fragment {


    TextView log_out;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).changeActionBar("My Account");
        View view = inflater.inflate(R.layout.fragment_user,container,false);


        sharedPreferences = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

        log_out = view.findViewById(R.id.log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putBoolean("isLogin",false);
                editor.commit();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });



        return view ;
    }
}
