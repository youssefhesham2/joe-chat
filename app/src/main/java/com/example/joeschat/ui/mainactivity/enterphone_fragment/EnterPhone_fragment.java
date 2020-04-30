package com.example.joeschat.ui.mainactivity.enterphone_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joeschat.ui.chat_activity.ChatActivity;
import com.example.joeschat.ui.mainactivity.MainActivity;
import com.example.joeschat.ui.mainactivity.enter_verification_fragment.EnterVerification;
import com.example.joeschat.R;
import com.example.joeschat.ui.mainactivity.register_fragment.Register_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;


public class EnterPhone_fragment extends Fragment {
    EditText phone_field,Verification_code,email_field,passwor_field;
    Button next;
    FirebaseAuth auth;
    EnterPhone_fragment fragment;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String id;
    View view;
    Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_enter_phone, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intialviews();
        nextt();
        auth=FirebaseAuth.getInstance();
    }

    private void nextt() {

        next.setOnClickListener(new View.OnClickListener() {
            String phone,email,password;
            @Override
            public void onClick(View view) {
               // phone=phone_field.getText().toString();
               /* if(phone.isEmpty()) {
                    Toast.makeText(context, "phone number is required", Toast.LENGTH_SHORT).show();
                    phone_field.requestFocus();
                    return;
                }
                if(phone.length()!=13){
                    Toast.makeText(context, "phone number is error", Toast.LENGTH_SHORT).show();
                    phone_field.requestFocus();
                    return;
                }*/

                email=email_field.getText().toString();
                password=passwor_field.getText().toString();
                if(email.isEmpty()) {
                    Toast.makeText(context, "email is required", Toast.LENGTH_SHORT).show();
                    email_field.requestFocus();
                    return;
                }
                if(password.isEmpty()) {
                    Toast.makeText(context, "password is required", Toast.LENGTH_SHORT).show();
                    email_field.requestFocus();
                    return;
                }
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           Register_fragment fragment2=new Register_fragment();
                           FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                           FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                           fragmentTransaction.replace(R.id.fram,fragment2);
                           fragmentTransaction.addToBackStack(null);
                           fragmentTransaction.commit();
                       }
                       else
                       {
                           Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    }
                });
               // editor.putString("phone",phone);
                //editor.commit();

            }
        });
    }

    private void intialviews() {
        phone_field=view.findViewById(R.id.phone_number_field);
        email_field=view.findViewById(R.id.mail_field);
        passwor_field=view.findViewById(R.id.password_field);

        next=view.findViewById(R.id.next);
        fragment = new EnterPhone_fragment();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser auth=FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        int i=preferences.getInt("login",0);
        if(auth!=null){
            Intent intent=new Intent(context, ChatActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}
