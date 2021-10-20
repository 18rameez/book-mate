package com.android.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.book.adapter.MessageAdapter;
import com.android.book.databinding.ActivityChatScreenBinding;
import com.android.book.models.FirebaseUserModel;
import com.android.book.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatScreen extends AppCompatActivity {

    ActivityChatScreenBinding chatScreenBinding;
    EditText edtSendMessage;
    ImageView sendButton;
    String senderFirebaseId, receiverFirebaseId ; //senderId = my_Id, receiver = message-receiver-id

    MessageAdapter messageAdapter;
    List<Message>messageList;
    RecyclerView recyclerViewMessage;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    DatabaseReference reference;
    String receiverName, profileUrl;
    Toolbar toolbar;
    TextView toolbarName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatScreenBinding = ActivityChatScreenBinding.inflate(getLayoutInflater());
        setContentView(chatScreenBinding.getRoot());



        receiverFirebaseId = getIntent().getStringExtra("firebaseId");
        Log.v("user-firebase",receiverFirebaseId);


        toolbar = findViewById(R.id.toolbar);
        toolbarName =chatScreenBinding.userName;




        recyclerViewMessage = chatScreenBinding.messageRecycler;
        recyclerViewMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewMessage.setLayoutManager(linearLayoutManager);

        sharedPreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        senderFirebaseId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        sendButton =chatScreenBinding.snedButton;
        edtSendMessage=chatScreenBinding.editMessage;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(senderFirebaseId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                readMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getUserFromFirebaseId();

    }

    private void getUserFromFirebaseId() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = reference.orderByKey().equalTo(receiverFirebaseId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Object  object = snapshot.getValue();
                Gson gson = new Gson();
                String json = gson.toJson(object);
                JSONObject jsonObject1 = null;
                try {
                    JSONObject   jsonObject = new JSONObject(json);
                    jsonObject1 =  jsonObject.getJSONObject(receiverFirebaseId);
                    receiverName = jsonObject1.getString("username");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                toolbarName.setText(receiverName);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        edtSendMessage.setText("");
    }

    private  void readMessage(){

        messageList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Message message = dataSnapshot.getValue(Message.class);

                    if (message.getReceiver().equals(senderFirebaseId)&& message.getSender().equals(receiverFirebaseId)||
                            message.getReceiver().equals(receiverFirebaseId)&& message.getSender().equals(senderFirebaseId )){

                        messageList.add(message);
                    }

                    messageAdapter = new MessageAdapter(getApplicationContext(),messageList);
                    recyclerViewMessage.setAdapter(messageAdapter);

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}