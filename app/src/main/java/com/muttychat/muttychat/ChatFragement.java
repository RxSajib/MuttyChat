package com.muttychat.muttychat;


import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragement extends Fragment {

    private DatabaseReference MuserDatabase;
    private RecyclerView recyclerView;

    public ChatFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_fragement, container, false);

        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = view.findViewById(R.id.ChatRecylerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onStart() {

        FirebaseRecyclerAdapter<UserHolder, ChatModal> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserHolder, ChatModal>(
                UserHolder.class,
                R.layout.user_layout,
                ChatModal.class,
                MuserDatabase
        ) {
            @Override
            protected void populateViewHolder(final ChatModal chatModal, UserHolder userHolder, int i) {

                final String UID = getRef(i).getKey();
                MuserDatabase.child(UID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            if(dataSnapshot.hasChild("image_url")){
                                String image_urlget = dataSnapshot.child("image_url").getValue().toString();
                                chatModal.setProfileimagesett(image_urlget);
                            }

                            if(dataSnapshot.hasChild("location")){
                                String locationget = dataSnapshot.child("location").getValue().toString();
                                chatModal.setUserlocationset(locationget);
                            }
                            if(dataSnapshot.hasChild("name")){
                                String nameget = dataSnapshot.child("name").getValue().toString();
                                chatModal.setUsernameset(nameget);
                            }

                            chatModal.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("KEY", UID);
                                    startActivity(intent);
                                }
                            });
                        }
                        else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        super.onStart();
    }

    public static class ChatModal extends RecyclerView.ViewHolder{

        private CircleImageView profileimage;
        private TextView username, userlocation;
        private Context context;

        public ChatModal(@NonNull View itemView) {
            super(itemView);

            profileimage = itemView.findViewById(R.id.SampleProfileImageID);
            username = itemView.findViewById(R.id.SampleUserNameID);
            userlocation = itemView.findViewById(R.id.SampleLocationID);
            context = itemView.getContext();
        }

        public void setProfileimagesett(String image){
            Picasso.with(context).load(image).placeholder(R.drawable.defaultimage).into(profileimage);
        }
        public void setUsernameset(String nam){
            username.setText(nam);
        }
        public void setUserlocationset(String location){
            userlocation.setText(location);
        }
    }
}
