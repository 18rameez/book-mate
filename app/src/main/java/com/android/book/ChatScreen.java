package com.android.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.book.databinding.ActivityChatScreenBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ChatScreen extends AppCompatActivity {

    ActivityChatScreenBinding chatScreenBinding;
    EditText edtSendMessage;
    ImageView sendButton;
    String senderFirebaseId, receiverFirebaseId ;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        chatScreenBinding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        setContentView(chatScreenBinding.getRoot());
        receiverFirebaseId = getIntent().getStringExtra("firebaseId");
        Toast.makeText(this, receiverFirebaseId, Toast.LENGTH_SHORT).show();

        sharedPreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        senderFirebaseId = sharedPreferences.getString("firebaseId","");

        sendButton =chatScreenBinding.snedButton;
        edtSendMessage=chatScreenBinding.editMessage;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();
            }
        });
    }

    private void sendMessage() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object>hashMap = new HashMap<>();
        hashMap.put("sender",senderFirebaseId);
        hashMap.put("receiver",receiverFirebaseId);
        hashMap.put("message",edtSendMessage.getText().toString());

        reference.child("Chats").push().setValue(hashMap);
    }
}