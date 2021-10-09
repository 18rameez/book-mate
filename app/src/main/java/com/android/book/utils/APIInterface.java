package com.android.book.utils;

import com.android.book.models.Book;
import com.android.book.models.Category;
import com.android.book.models.User;
import com.android.book.resposne_model.InsertResponse;

import okhttp3.RequestBody;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public  interface APIInterface {

    @FormUrlEncoded
    @POST("/api/get_user_id.php")
    Call<User> getUser(@Field("id") String firebaseId) ;

    @FormUrlEncoded
    @POST("/api/get_user.php")
    Call<User> getUserId(@Field("id") String id) ;

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
    @POST("/api/add_book_without_image.php")
    Call<ResponseBody> addBookWithoutImage(@Part("book_name") RequestBody bookName,
                               @Part("book_price") RequestBody bookPrice,
                               @Part("user_id") RequestBody userId,
                               @Part("book_author") RequestBody bookAuthor,
                               @Part("book_language") RequestBody bookLanguage,
                               @Part("book_description") RequestBody bookDescription,
                               @Part("book_date")RequestBody publishDate,
                               @Part("book_category")RequestBody bookCategory);


    @Multipart
    @POST("/api/add_book.php")
        // can be used ResponseBody if needed
    Call<RequestBody> addTest(@Part MultipartBody.Part images);


    @FormUrlEncoded
    @POST("/api/get_book_list.php")
    Call<List<Book>> getLatestBooks(@Field("user_id")String Token) ;

    @FormUrlEncoded
    @POST("/api/get_book_list_filter.php")
    Call<List<Book>> getBooksFilter(@Field("category_id")String queryValue,@Field("user_id")String user_id) ;

    @FormUrlEncoded
    @POST("/api/get_book_list_by_price.php")
    Call<List<Book>> getBooksByPrice(@Field("book_price")String queryValue,@Field("user_id")String user_id) ;

    @FormUrlEncoded
    @POST("/api/get_book_details.php")
    Call<Book> getBookDetails(@Field("book_id")String bookID) ;

    @FormUrlEncoded
    @POST("/api/get_all_category.php")
    Call<List<Category>> getAllCategory(@Field("token")String Token) ;

    @FormUrlEncoded
    @POST("/api/search_books.php")
    Call<List<Book>> searchBook(@Field("search_query")String Token) ;

    @FormUrlEncoded
    @POST("/api/add_user_details.php")
    Call<InsertResponse> addUserDetails(@Field("firebase_id")String firebaseId,
                                        @Field( "user_name")String userName,
                                        @Field( "user_email")String userEmail) ;


}

//video
