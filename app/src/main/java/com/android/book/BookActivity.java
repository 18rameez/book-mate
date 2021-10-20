package com.android.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.book.databinding.ActivityBookBinding;
import com.android.book.models.Book;
import com.android.book.models.User;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookActivity extends AppCompatActivity {

    ActivityBookBinding bookBinding;
    APIInterface apiInterface;
    String bookId, bookSellerName;
    ImageView bookImages;
    TextView bookName, authorName, sellerName, bookPrice, bookDescription, bookLanguage, bookAddedDate, chatButton;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    String firebaseId, userId;
    User user;
    Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookBinding =  ActivityBookBinding.inflate(getLayoutInflater());
        setContentView(bookBinding.getRoot());

        bookName=bookBinding.bookName;
        authorName=bookBinding.bookAuthorName;
        sellerName=bookBinding.sellerName;
        bookPrice=bookBinding.bookPrice;
        bookDescription=bookBinding.bookDescription;
        bookLanguage=bookBinding.bookLanguage;
        bookAddedDate=bookBinding.bookAddedDate;
        chatButton=bookBinding.chatButton;
        bookImages = findViewById(R.id.thumbnail);


        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChatScreen.class);
                intent.putExtra("firebaseId",user.getFirebaseId());
                Log.v("user-firebase",user.getFirebaseId());
                startActivity(intent);
            }
        });

        sharedPreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        userId = sharedPreferences.getString("user_id","");

      //  firebaseId = sharedPreferences.getString("firebaseId","");
         firebaseId = FirebaseAuth.getInstance().getUid();


        bookId=getIntent().getStringExtra("book_id");
        bookSellerName=getIntent().getStringExtra("seller_name");
        apiInterface= RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);
        getBookDetails();


    }

    private void getBookDetails() {

        Call<Book> call = apiInterface.getBookDetails(bookId);
        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {

                book = response.body();
                if (book != null){

                    System.out.println("onResponse");
                    setBookDetails(book);
                    getUserDetails();
                }else {

                    Toast.makeText(BookActivity.this, "It may take some time to process", Toast.LENGTH_SHORT).show();
                }



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

    private void setBookDetails(Book book) {

        bookName.setText(book.getBookName());
        authorName.setText("Author: "+book.getBookAuthor());
        sellerName.setText("Seller: "+bookSellerName);
        bookPrice.setText("₹"+book.getBookPrice());
        bookDescription.setText(book.getBookDescription());
        bookLanguage.setText("Language: "+book.getBookLanguage());
        Glide.with(getApplicationContext()).load(book.getBookThumbnail()).into(bookImages);
    }



    private void getUserDetails() {

        Call<User> call = apiInterface.getUser(book.getUserId());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                    user = response.body();

                }
                System.out.println("onResponse-user-details");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                String message = t.getMessage();
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