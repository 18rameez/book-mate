package com.android.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private EditText edt_name, edt_email, edt_password ;
    private Button btn_login ;

    FirebaseAuth auth;
    DatabaseReference reference;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();



        edt_name = findViewById(R.id.edt_name);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = edt_name.getText().toString();
                String email = edt_email.getText().toString();
                String password = edt_password.getText().toString();

               signUp(name,email, password);
            }
        });
    }

    private void signUp(String userName, String email, String password) {

          auth.createUserWithEmailAndPassword(email, password)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task) {

                          if (task.isSuccessful()){

                              Toast.makeText(SignUpActivity.this, "success", Toast.LENGTH_SHORT).show();

                              FirebaseUser firebaseUser = auth.getCurrentUser();
                              String userId =  firebaseUser.getUid();

                              reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                              HashMap<String, String> hashMap = new HashMap<>();
                              hashMap.put("email", email);
                              hashMap.put("username", userName);
                              hashMap.put("imageUrl", "default");

                              reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if (task.isSuccessful()){

                                          editor.putBoolean("isLogin",true);
                                          editor.putString("firebaseId",userId);
                                          editor.commit();
                                          Intent intent =  new Intent(SignUpActivity.this,MainActivity.class);
                                          startActivity(intent);

                                      }else {

                                          Toast.makeText(SignUpActivity.this,  task.toString(), Toast.LENGTH_SHORT).show();

                                      }
                                  }
                              });
                          }else {

                              String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                              Toast.makeText(SignUpActivity.this, "failed" + errorCode, Toast.LENGTH_SHORT).show();

                          }

                      }
                  });
    }
}