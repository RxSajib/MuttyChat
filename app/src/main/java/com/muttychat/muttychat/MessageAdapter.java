package com.muttychat.muttychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private List<Message> usermessagelist;
    private FirebaseAuth Mauth;
    private DatabaseReference usersdatabaseref;
    private Context context;


    public MessageAdapter(List<Message> usermessagelist) {
        this.usermessagelist = usermessagelist;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_layout, parent, false);
        Mauth = FirebaseAuth.getInstance();
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageHolder holder, int position) {

        final String MessageSenderID = Mauth.getCurrentUser().getUid();
        final Message messagelist = usermessagelist.get(position);

        String fromuserID = messagelist.getFrom();
        String frommessagetype = messagelist.getType();

        usersdatabaseref = FirebaseDatabase.getInstance().getReference().child("Users").child(fromuserID);
        usersdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    ///setup your image get database
                }
                else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return usermessagelist.size();
    }

    public class MessageHolder extends RecyclerView.ViewHolder{

        private CircleImageView reciverimage;
        private TextView sendertext;
        private TextView recivertext;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            reciverimage = itemView.findViewById(R.id.ReciverProfieImageID);
            sendertext = itemView.findViewById(R.id.SnderTrxtID);
            recivertext = itemView.findViewById(R.id.ReciverTextID);
        }
    }
}
