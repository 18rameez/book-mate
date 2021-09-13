package com.android.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.book.adapter.ImageAdapter;
import com.android.book.models.Book;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.okhttp.MultipartBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;


public class AddBook extends AppCompatActivity {
    
    private static final int PICK_IMAGE = 18;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    APIInterface apiInterface;

    TextInputEditText etBookName, etBookPrice, etBookAuthor, etBookLanguage, etBookDescription, etBookCategory;
    RecyclerView recycSelectedImage;
    Button addButton ;
    ImageView btnSelectImage;
    ArrayList<Uri> selectedImageUri =  new ArrayList<Uri>();
    List<MultipartBody.Part> parts = new ArrayList<>();

    RequestBody bookName, bookPrice, bookAuthor, bookLanguage, bookCategory, bookDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etBookName =  findViewById(R.id.book_name);
        etBookPrice =  findViewById(R.id.book_price);
        etBookAuthor =  findViewById(R.id.book_author);
        etBookLanguage =  findViewById(R.id.book_langugae);
        etBookDescription =  findViewById(R.id.book_description);
        etBookCategory =  findViewById(R.id.book_category);

        addButton = findViewById(R.id.add_button);
        btnSelectImage = findViewById(R.id.select_image);
        recycSelectedImage = findViewById(R.id.recycler_view_image);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 bookName = createPartFromString(etBookName.getText().toString());
                bookPrice = createPartFromString(etBookPrice.getText().toString());
                bookAuthor = createPartFromString(etBookAuthor.getText().toString());
                bookLanguage = createPartFromString(etBookLanguage.getText().toString());
                bookCategory = createPartFromString(etBookCategory.getText().toString());
                bookDescription = createPartFromString(etBookDescription.getText().toString());

                for (int i=0; i < selectedImageUri.size(); i++){
                    Log.v("times_run"," "+ i);
                    parts.add(prepareFilePart("book_images[]",selectedImageUri.get(i)));
                }

             //   sendBookRequest(bookName,bookPrice,bookAuthor,bookLanguage,bookCategory,bookDescription,parts);

                try {
                    sendTest(selectedImageUri.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImageUri.size() < 4){
                    selectImageFromGallery();
                }else {
                    Toast.makeText(AddBook.this, "You have already selected 4 images", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void sendTest(Uri fileUri) throws IOException {

        File file = new File(fileUri.getPath());
       RequestBody requestFile =
               RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

       MultipartBody.Part imagePart = MultipartBody.Part.createFormData("book_images[]", file.getName(), requestFile);

        Toast.makeText(this,  String.valueOf(imagePart.body().contentType()), Toast.LENGTH_SHORT).show();

        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);
        Call<RequestBody>addTest = apiInterface.addTest(imagePart);
        addTest.enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {

                System.out.println("onResponse");
                System.out.println(response.body().toString());

                JSONObject object = null;
                try {
                    object = new JSONObject(response.body().toString());
                    String messageString = object.getString("Message");
                    Toast.makeText(getApplicationContext(), messageString, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<RequestBody> call, Throwable t) {


                String message = t.getMessage();
                Log.d("failure12", "rammi"+message);
                System.out.println("onFailure");
                System.out.println(t.fillInStackTrace());
                System.out.println(t.getStackTrace());
            }
        });

    }

    private void selectImageFromGallery() {

        if (checkPermission()){

//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//            startActivityForResult(intent, PICK_IMAGE);

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

        } else {

            checkAndRequestStoragePermissions();
        }


    }

    private boolean checkAndRequestStoragePermissions() {

            int writeStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int readStorage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (writeStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (readStorage != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
            return true;


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE&& resultCode == Activity.RESULT_OK && data !=null) {
            //TODO: action


                selectedImageUri.add(data.getData());
                recycSelectedImage.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                recycSelectedImage.setAdapter(new ImageAdapter(this,selectedImageUri));



        }
    }

    private void sendBookRequest(RequestBody bookName, RequestBody bookPrice, RequestBody bookAuthor, RequestBody bookLanguage, RequestBody bookCategory, RequestBody bookDescription, List<MultipartBody.Part> parts) {

     apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);
//      Call<ResponseBody> addBooks = apiInterface.addBook(bookName,bookPrice,bookAuthor,bookLanguage,bookCategory,bookDescription, parts);
//
//      addBooks.enqueue(new Callback<ResponseBody>() {
//          @Override
//          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//              System.out.println("onResponse");
//              System.out.println(response.body().toString());
//              Toast.makeText(getApplicationContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
//          }
//
//          @Override
//          public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//              String message = t.getMessage();
//              Log.d("failure12", "rammi"+message);
//              System.out.println("onFailure");
//              System.out.println(t.fillInStackTrace());
//          }
//      });


    }




    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        // use the FileUtils to get the actual file by uri
        File file = new File(fileUri.getPath());


        // create RequestBody instance from file
        okhttp3.RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

     //   Toast.makeText(this,  String.valueOf(fileUri), Toast.LENGTH_SHORT).show();

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}