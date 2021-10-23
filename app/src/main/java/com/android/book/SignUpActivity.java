package com.android.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.book.resposne_model.InsertResponse;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText edt_name, edt_email, edt_password ;
    private Button btn_login ;

    FirebaseAuth auth;
    DatabaseReference reference;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    APIInterface apiInterface;
    String userFirebaseId, user_id_from_database;

    String name, email, password ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();


        sharedPreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);

        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  name = edt_name.getText().toString();
                 email = edt_email.getText().toString();
                 password = edt_password.getText().toString();

                if (name.equalsIgnoreCase("")){

                    Toast.makeText(SignUpActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();

                }else if (email.equalsIgnoreCase("")) {

                    Toast.makeText(SignUpActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();

                }else if (password.equalsIgnoreCase("")){

                    Toast.makeText(SignUpActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();

                }else {

                    signUp(name,email, password);
                }


            }
        });
    }

    private void signUp(String userName, String email, String password) {

          auth.createUserWithEmailAndPassword(email, password)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {

                          if (task.isSuccessful()){


                             Toast.makeText(SignUpActivity.this, "Verification link is sent to your email address. Verify and Login", Toast.LENGTH_LONG).show();
                              FirebaseUser firebaseUser = auth.getCurrentUser();
                              userFirebaseId =  firebaseUser.getUid();

                              sendEmailVerification(firebaseUser);

                              reference = FirebaseDatabase.getInstance().getReference("Users").child(userFirebaseId);

                              HashMap<String, String> hashMap = new HashMap<>();
                              hashMap.put("email", email);
                              hashMap.put("username", userName);
                              hashMap.put("imageUrl", "default");

                              reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if (task.isSuccessful()){

                                          addUserDetailsToDatabase(userName,email);

                                      }else {

                                          Toast.makeText(SignUpActivity.this,  task.toString(), Toast.LENGTH_SHORT).show();

                                      }
                                  }
                              });
                          }else {

                              String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                              Toast.makeText(SignUpActivity.this, "Failed: " + errorCode, Toast.LENGTH_SHORT).show();

                          }

                      }
                  });
    }

    private void sendEmailVerification(FirebaseUser firebaseUser) {

        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });

    }

    private void addUserDetailsToDatabase(String userName, String email) {

        Call<InsertResponse> call = apiInterface.addUserDetails(userFirebaseId,userName,email);
        call.enqueue(new Callback<InsertResponse>() {
            @Override
            public void onResponse(Call<InsertResponse> call, Response<InsertResponse> response) {

                InsertResponse insertResponse = response.body();
                user_id_from_database = insertResponse.getUserId();
                editor.putString("user_id",user_id_from_database);
                editor.commit();

            }

            @Override
            public void onFailure(Call<InsertResponse> call, Throwable t) {

                String message = t.getMessage();
                Log.d("failure12", message);
                System.out.println("onFailure");
                System.out.println(t.fillInStackTrace());
            }
        });

    }
}