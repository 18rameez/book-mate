package com.android.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.book.models.User;
import com.android.book.utils.APIInterface;
import com.android.book.utils.RetrofitClientInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_email, edt_password ;
    private Button btn_login;
    private TextView btn_sign_up;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    boolean isLogin = false ;

    FirebaseAuth auth;
    DatabaseReference reference;
    APIInterface apiInterface;
    String firebaseUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        auth = FirebaseAuth.getInstance();

        sharedPreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

        isLogin = sharedPreferences.getBoolean("isLogin",false);

        if (isLogin){

            Intent intent =  new Intent(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.login);
        btn_sign_up = findViewById(R.id.sign_up);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();

                login(email, password);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =  new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface.class);

    }

    private void login(String email, String password) {


        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    firebaseUserId =  firebaseUser.getUid();
                    editor.putBoolean("isLogin",true);
                    editor.putBoolean("firebaseId",true);
                    editor.commit();
                    getUserDetails();
                    Intent intent =  new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getUserDetails() {

        Call<User> call = apiInterface.getUser(firebaseUserId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.isSuccessful()) {

                 User user = response.body();
                 editor.putString("user_id",user.getUserId());
                 editor.commit();
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