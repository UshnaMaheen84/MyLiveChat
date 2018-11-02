package com.example.admin.mylivechat;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daasuu.bl.ArrowDirection;
import com.example.admin.mylivechat.model.ChatMessage;
import com.example.admin.mylivechat.viewHolder.MyVH;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity {

    FirebaseRecyclerAdapter<ChatMessage, MyVH> adapter;
    LinearLayout main;
    ImageView submit_button;
    EditText emojiconEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = findViewById(R.id.activity_main);
        submit_button = findViewById(R.id.submit_button);
        emojiconEditText = findViewById(R.id.emojicon_edit_text);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Messages").push().setValue(new ChatMessage(emojiconEditText.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                emojiconEditText.setText("");
                emojiconEditText.requestFocus();
            }
        });


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), 1);
        } else {
            Toast.makeText(this, "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail() + " to Chat App", Toast.LENGTH_SHORT).show();
            displayMessages();

        }
    }

    private void displayMessages() {

        final RecyclerView listOfMessage = findViewById(R.id.list_of_message);
        listOfMessage.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        Query query = FirebaseDatabase.getInstance().getReference("Messages");

        FirebaseRecyclerOptions<ChatMessage> options = new FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<ChatMessage, MyVH>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyVH holder, int position, @NonNull ChatMessage model) {
                holder.message.setText(model.getMessageText());
                holder.messageTime.setText(DateFormat.format("dd-MM-yy (HH:mm)", model.getMessageTime()));
                holder.messageUserName.setText(model.getMessageUser());

                if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(model.getMessageUser())){
                    holder.bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);
                    holder.linearLayout.setGravity(Gravity.RIGHT);
                }


                listOfMessage.scrollToPosition(position);
            }

            @NonNull
            @Override
            public MyVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
            }
        };


        adapter.startListening();
        listOfMessage.setAdapter(adapter);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Successfully Signed In!", Toast.LENGTH_SHORT).show();
            displayMessages();
        } else {
            Snackbar.make(main, "We couldn't sign you in.Please try again later", Snackbar.LENGTH_SHORT).show();
            finish();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null)
            adapter.startListening();
    }
}
