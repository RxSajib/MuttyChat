package com.muttychat.muttychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView profileimage;
    private Uri imageuri = null;
    private EditText username, email, location;
    private Button setuppbutton;
    private ProgressDialog progressDialog;
    private DatabaseReference MuserDatabase;
    private FirebaseAuth Mauth;
    private String CurrentUserID;
    private StorageReference MprofileStores;
    private String DownloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MprofileStores = FirebaseStorage.getInstance().getReference().child("profile_image");
        Mauth = FirebaseAuth.getInstance();
        CurrentUserID = Mauth.getCurrentUser().getUid();
        MuserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        progressDialog = new ProgressDialog(ProfileActivity.this);
        profileimage = findViewById(R.id.ProfileImageID);

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileActivity.this);
            }
        });

        username = findViewById(R.id.UserNameID);
        email = findViewById(R.id.EmailAddressID);
        location = findViewById(R.id.LocationddressID);

        setuppbutton = findViewById(R.id.EnterButtonID);


        setuppbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernametext = username.getText().toString();
                String emailtext = email.getText().toString();
                String locationtext=  location.getText().toString();

                if(usernametext.isEmpty()){
                    username.setError("Username require");
                }
                else if(emailtext.isEmpty()){
                    email.setError("Email require");
                }
                else if(locationtext.isEmpty()){
                    location.setError("Location require");
                }
                else {

                    progressDialog.setTitle("Please wait");
                    progressDialog.setMessage("Saving your information");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    Map usermap = new HashMap();
                    usermap.put("name", usernametext);
                    usermap.put("email", emailtext);
                    usermap.put("location", locationtext);
                    usermap.put("image_url", DownloadUri);

                    MuserDatabase.child(CurrentUserID).updateChildren(usermap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {

                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageuri = result.getUri();
                profileimage.setImageURI(imageuri);

                StorageReference filepath = MprofileStores.child(imageuri.getLastPathSegment());
                filepath.putFile(imageuri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if(task.isSuccessful()){
                            DownloadUri = task.getResult().getDownloadUrl().toString();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }
}
