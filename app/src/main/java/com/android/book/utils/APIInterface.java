package com.android.book.utils;

import com.android.book.models.Book;
import com.android.book.models.User;
import okhttp3.RequestBody;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public  interface APIInterface {

    @FormUrlEncoded
    @POST("/api/get_user.php")
    Call<User> getUser(@Field("id") String id) ;

    @Multipart
    @POST("/api/add_book.php")
    // can be used ResponseBody if needed
    Call<ResponseBody> addBook(@Part("book_name") RequestBody bookName,
                               @Part("book_price") RequestBody bookPrice,
                               @Part("book_author") RequestBody bookAuthor,
                               @Part("book_language") RequestBody bookLanguage,
                               @Part("book_category") RequestBody bookCategory,
                               @Part("book_description") RequestBody bookDescription,
                               @Part List<MultipartBody.Part> images);


    @Multipart
    @POST("/api/add_book.php")
        // can be used ResponseBody if needed
    Call<RequestBody> addTest(@Part MultipartBody.Part images);


    @FormUrlEncoded
    @POST("/api/get_book_list.php")
    Call<List<Book>> getLatestBooks(@Field("token")String Token) ;

    @FormUrlEncoded
    @POST("/api/get_book_details.php")
    Call<Book> getBookDetails(@Field("book_id")String bookID) ;

}

//video
