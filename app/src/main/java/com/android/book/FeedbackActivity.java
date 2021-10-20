package com.android.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {
    APIInterface apiInterface;
    String feedback, date, userId;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    EditText edtFeedback;
    Button sendFeedback;
    CircularProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        edtFeedback= findViewById(R.id.edt_feedback);
        sendFeedback= findViewById(R.id.send_feedback);
        progressIndicator= findViewById(R.id.progress_bar);

        sharedPreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        userId = sharedPreferences.getString("user_id","");
        Log.v("user_id",userId);

        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        date = df.format(c);

        sendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendFeedback.setVisibility(View.GONE);
                progressIndicator.setVisibility(View.VISIBLE);

                feedback= edtFeedback.getText().toString();
                sendFeedback(feedback);
            }
        });


    }

    private void sendFeedback(String feedback) {

        Call<ResponseBody>call = apiInterface.addFeedback(userId,feedback,date);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                progressIndicator.setVisibility(View.GONE);
                sendFeedback.setVisibility(View.VISIBLE);
                edtFeedback.setText("");
                Toast.makeText(FeedbackActivity.this, "Your feedback has been successfully sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}