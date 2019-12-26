package com.muttychat.muttychat;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistationFragement extends Fragment {

    private EditText email;
    private EditText password;
    private EditText cpassword;
    private Button regasterbutton;
    private FirebaseAuth Mauth;
    private ProgressDialog Mprogress;


    public RegistationFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registation_fragement, container, false);
        Mprogress = new ProgressDialog(getContext());
        Mauth = FirebaseAuth.getInstance();
        email = view.findViewById(R.id.RegisterEmailID);
        password = view.findViewById(R.id.RegusterPasswordID);
        cpassword = view.findViewById(R.id.RegisterConfrimPasswordID);

        regasterbutton = view.findViewById(R.id.RegisterButtonID);

        regasterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailtext = email.getText().toString();
                String passwordtext = password.getText().toString();
                String cpasswordtext = cpassword.getText().toString();

                if(emailtext.isEmpty()){
                    email.setError("Email require");
                }
                else if(passwordtext.isEmpty()){
                    password.setError("Password require");
                }
                else if(cpasswordtext.isEmpty()){
                    cpassword.setError("Password require");
                }
                else if(!cpasswordtext.equals(passwordtext)){
                    password.setError("Password not match");
                    cpassword.setError("Password not match");
                }
                else if(cpasswordtext.length() <= 7){
                    Toast.makeText(getContext(), "Password need 7 char", Toast.LENGTH_LONG).show();
                }
                else {
                    Mprogress.setTitle("Please wait");
                    Mprogress.setMessage("We are creating your account");
                    Mprogress.setCanceledOnTouchOutside(false);
                    Mprogress.show();
                    Mauth.createUserWithEmailAndPassword(emailtext, passwordtext)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Mprogress.dismiss();
                                        Toast.makeText(getContext(), "you are done", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getContext(), HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                    else {
                                        Mprogress.dismiss();
                                        Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        FirebaseUser user = Mauth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(getContext(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finish();
        }
        super.onStart();
    }
}
