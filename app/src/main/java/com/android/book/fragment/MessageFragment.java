package com.android.book.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.book.ChatScreen;
import com.android.book.MainActivity;
import com.android.book.R;
import com.android.book.adapter.MessageListAdapter;
import com.android.book.models.FirebaseUserModel;
import com.android.book.models.Message;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MessageFragment extends Fragment {

    RecyclerView recyclerView;
    MessageListAdapter messageListAdapter;
    List<String> userList;
    List<FirebaseUserModel> users;
    DatabaseReference databaseReference;
    String currentUserFirebaseId;
    CircularProgressIndicator progressBar;

    int loopCount= 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        ((MainActivity)getActivity()).changeActionBar("Messages");

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        progressBar = view.findViewById(R.id.progress_bar);

        userList = new ArrayList<>();
        currentUserFirebaseId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();

                for ( DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Message message = dataSnapshot.getValue(Message.class);

                   Log.v("get-sender",message.getSender());
                    Log.v("get-receiver",message.getReceiver());

                    if (message.getSender().equals(currentUserFirebaseId)){
                        userList.add(message.getReceiver());
                    }

                    if (message.getReceiver().equals(currentUserFirebaseId)){
                        userList.add(message.getSender());
                    }


                }

                readChats();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }

    private void readChats() {
        users = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                progressBar.setVisibility(View.GONE);
                loopCount++;
               users.clear();
                ListIterator<FirebaseUserModel> listIteratorUser;

                for (DataSnapshot dataSnapshot : snapshot.getChildren() ){

                    FirebaseUserModel userModel = dataSnapshot.getValue(FirebaseUserModel.class);
                    userModel.setFirebaseId(dataSnapshot.getKey());

                    for (String id :  userList){

                        if (userModel.getFirebaseId().equals(id)){

                            if (users.size()!=0){


                                 listIteratorUser = users.listIterator();
                                while(listIteratorUser.hasNext()){
                                    FirebaseUserModel user1 = listIteratorUser.next();
                                    if (!userModel.getFirebaseId().equals(user1.getFirebaseId())&&!users.contains(userModel)){
                                        listIteratorUser.add(userModel);
                                    }

                                }

                            }else {
                                users.add(userModel);
                                Log.v("loop", String.valueOf(loopCount));
                            }
                        }
                    }
                }

                if (users.size()==0){
                    Toast.makeText(getContext(), "Inbox is empty", Toast.LENGTH_SHORT).show();
                }

                messageListAdapter = new MessageListAdapter(getContext(), users, new MessageListAdapter.OnClickListener() {
                    @Override
                    public void onClick(int position) {

                        Intent intent = new Intent(getContext(), ChatScreen.class);
                        intent.putExtra("firebaseId",users.get(position).getFirebaseId());
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(messageListAdapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
