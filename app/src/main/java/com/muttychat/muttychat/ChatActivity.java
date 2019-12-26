package com.muttychat.muttychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.file.SimpleFileVisitor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView profileiage;
    private TextView username;
    private DatabaseReference MuserDatabase;
    private String ReciverUID;
    private FirebaseAuth Mauth;
    private String SenderUID;
    private ImageButton sendbutton;
    private EditText inputmessage;
    private DatabaseReference Roodref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        inputmessage = findViewById(R.id.MessageInputTextID);
        sendbutton = findViewById(R.id.SendButtonID);
        Roodref = FirebaseDatabase.getInstance().getReference();
        ReciverUID = getIntent().getStringExtra("KEY");
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        toolbar = findViewById(R.id.ChatToolbarID);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);

        Mauth = FirebaseAuth.getInstance();
        SenderUID = Mauth.getCurrentUser().getUid();

        profileiage = findViewById(R.id.ChatImageID);
        username = findViewById(R.id.ChatUserNameID);

        MuserDatabase.child(ReciverUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("name")){
                        String nameget = dataSnapshot.child("name").getValue().toString();
                        username.setText(nameget);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "somethings error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MessageText  = inputmessage.getText().toString();
                if(MessageText.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please type anythings", Toast.LENGTH_LONG).show();
                }
                else {
                    sendingMessage(MessageText);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendingMessage(String Message){

        inputmessage.setText("");

        String message_senderref = "Message/"+SenderUID+"/"+ReciverUID;
        String message_reciverref = "Message/"+ReciverUID+"/"+SenderUID;

        DatabaseReference user_message_key = Roodref.child("Message").child(SenderUID).child(ReciverUID)
                .push();

        final  String message_push_id = user_message_key.getKey();

        Calendar calendardate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatdate = new SimpleDateFormat("dd-MMM-yy");
        String dateget = simpleDateFormatdate.format(calendardate.getTime());

        Calendar calendartime = Calendar.getInstance();
        SimpleDateFormat simpleDateFormattime = new SimpleDateFormat("hh:mm");
        String timeget = simpleDateFormattime.format(calendartime.getTime());


        Map messagetext_body = new HashMap();
        messagetext_body.put("message", Message);
        messagetext_body.put("date", dateget);
        messagetext_body.put("time", timeget);
        messagetext_body.put("type", "text");
        messagetext_body.put("from", SenderUID);

        ///
        messagetext_body.put("to", ReciverUID);
        messagetext_body.put("message_PushID", message_push_id);


        Map messagebody_details = new HashMap();
        messagebody_details.put(message_senderref + "/" + message_push_id, messagetext_body);
        messagebody_details.put(message_reciverref + "/" + message_push_id, messagetext_body);


       Roodref.updateChildren(messagebody_details).addOnCompleteListener(new OnCompleteListener() {
           @Override
           public void onComplete(@NonNull Task task) {
               if(task.isSuccessful()){

               }
               else {
                   Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
           }
       });
    }
}
