package com.android.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.ContentValues.TAG;

public class ForgotPassword extends AppCompatActivity {

    EditText edtEmail;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edtEmail= findViewById(R.id.edt_email);
        btnReset= findViewById(R.id.reset_button);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edtEmail.getText().toString().equalsIgnoreCase("")){

                    Toast.makeText(ForgotPassword.this, "Enter registered Email", Toast.LENGTH_SHORT).show();

                }else{

                    sendPasswordResetMail(edtEmail.getText().toString());
                }


            }
        });


    }

    private void sendPasswordResetMail(String email) {

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(ForgotPassword.this, "Password reset email is sent. Click on reset link and change password", Toast.LENGTH_LONG).show();
                        }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ForgotPassword.this, "Invalid email or Not registered with Book Mate", Toast.LENGTH_LONG).show();

            }
        });
    }
}