package com.android.book.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class Book  {



    @SerializedName("book_id")
    @Expose
    private String bookId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("book_thumbnail")
    @Expose
    private String bookThumbnail;
    @SerializedName("book_name")
    @Expose
    private String bookName;
    @SerializedName("book_price")
    @Expose
    private String bookPrice;
    @SerializedName("book_author")
    @Expose
    private String bookAuthor;
    @SerializedName("book_language")
    @Expose
    private String bookLanguage;
    @SerializedName("book_category")
    @Expose
    private String bookCategory;
    @SerializedName("book_details")
    @Expose
    private String bookDescription;
    @SerializedName("user_name")
    @Expose
    private String bookSellerName;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookSellerName() {
        return bookSellerName;
    }

    public void setBookSellerName(String bookSellerName) {
        this.bookSellerName = bookSellerName;
    }

    public String getBookThumbnail() {
        return bookThumbnail;
    }

    public void setBookThumbnail(String bookThumbnail) {
        this.bookThumbnail = bookThumbnail;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookLanguage() {
        return bookLanguage;
    }

    public void setBookLanguage(String bookLanguage) {
        this.bookLanguage = bookLanguage;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }
}
