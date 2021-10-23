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

import com.android.book.ComplaintActivity;
import com.android.book.FeedbackActivity;
import com.android.book.LoginActivity;
import com.android.book.MainActivity;
import com.android.book.MyBookList;
import com.android.book.R;
import com.android.book.models.User;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {


    TextView log_out, complaint, feedback, myBooks, pendingBooks, editProfile, settings;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    APIInterface apiInterface;
    String firebaseUserId;
    TextView user_name_view, user_email_view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).changeActionBar("My Account");
        View view = inflater.inflate(R.layout.fragment_user,container,false);

        log_out = view.findViewById(R.id.log_out);
        complaint = view.findViewById(R.id.complaint_view);
        feedback = view.findViewById(R.id.feedback_view);
        myBooks = view.findViewById(R.id.my_books_view);
        pendingBooks = view.findViewById(R.id.pending_books);
        editProfile = view.findViewById(R.id.edit_profile);
        settings =view.findViewById(R.id.settings_view);
        user_name_view =view.findViewById(R.id.user_name);
        user_email_view =view.findViewById(R.id.user_email);

        sharedPreferences = getContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editor.putBoolean("isLogin",false);
                editor.commit();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), FeedbackActivity.class);
                startActivity(intent);
            }
        });

        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), ComplaintActivity.class);
                startActivity(intent);
            }
        });

        myBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), MyBookList.class);
                startActivity(intent);
            }
        });


        getUserDetails();

        return view ;
    }


    private void getUserDetails() {

        firebaseUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Call<User> call = apiInterface.getUserFromFirebaseId(firebaseUserId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    User user = response.body();
                    user_name_view.setText(user.getUserName());
                    user_email_view.setText(user.getUserEmail());

                }
                System.out.println("onResponse");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                String message = t.getMessage();
                System.out.println("onFailure");
                System.out.println(t.fillInStackTrace());
            }

        });

    }
}
