package com.muttychat.muttychat;


import android.content.Context;
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
public class UsersFragement extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference MuserDatabase;

    public UsersFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_fragement, container, false);
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = view.findViewById(R.id.UserRecylearViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


    @Override
    public void onStart() {

        FirebaseRecyclerAdapter<UserHolder, UserModal> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserHolder, UserModal>(
                UserHolder.class,
                R.layout.user_layout,
                UserModal.class,
                MuserDatabase

        ) {
            @Override
            protected void populateViewHolder(final UserModal userModal, UserHolder userHolder, int i) {

                String UID = getRef(i).getKey();
                MuserDatabase.child(UID)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()) {

                                    if (dataSnapshot.hasChild("image_url")) {
                                        String image_urlget = dataSnapshot.child("image_url").getValue().toString();
                                        userModal.setProfileimage(image_urlget);
                                    }

                                    if (dataSnapshot.hasChild("name")) {
                                        String nameget = dataSnapshot.child("name").getValue().toString();
                                        userModal.setusernameset(nameget);
                                    }
                                    if (dataSnapshot.hasChild("location")) {
                                        String locationget = dataSnapshot.child("location").getValue().toString();
                                        userModal.setUserlocationset(locationget);
                                    }
                                } else {

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

    public static class UserModal extends RecyclerView.ViewHolder {

        private TextView username;
        private CircleImageView profileimage;
        private TextView userlocation;
        private Context context;

        public UserModal(@NonNull View itemView) {
            super(itemView);

            userlocation = itemView.findViewById(R.id.SampleLocationID);
            username = itemView.findViewById(R.id.SampleUserNameID);
            profileimage = itemView.findViewById(R.id.SampleProfileImageID);
            context = itemView.getContext();
        }

        public void setusernameset(String nam) {
            username.setText(nam);
        }

        public void setUserlocationset(String loc) {
            userlocation.setText(loc);
        }

        public void setProfileimage(String img) {
            Picasso.with(context).load(img).placeholder(R.drawable.defaultimage).into(profileimage);
        }
    }
}
