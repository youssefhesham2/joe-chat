package com.example.joeschat.ui.mainactivity.enter_verification_fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.telecom.PhoneAccount;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joeschat.R;
import com.example.joeschat.ui.mainactivity.register_fragment.Register_fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class EnterVerification extends Fragment {
    FirebaseAuth auth;
    EditText Verification_field;
    View view;
    TextView counter,resend_code;
    String phone,id;
    Button next2;
    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view= inflater.inflate(R.layout.fragment_enter_verification, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intialviews();
        auth=FirebaseAuth.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
         phone = preferences.getString("phone", "");
            login();

    }

    private void counter() {
        counter.setVisibility(View.VISIBLE);
        resend_code.setVisibility(View.GONE);
        int timeinminutes =60;
        final CountDownTimer timer = new CountDownTimer(timeinminutes * 1000, 1000) {
             int timerr=60;
            public void onTick(long millisUntilFinished) {
                  timerr=timerr-1;
                counter.setText(timerr-1+"");
            }

            @Override
            public void onFinish() {
                counter.setVisibility(View.GONE);
                resend_code.setVisibility(View.VISIBLE);
                resend_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Loginwithphone(phone);
                    }
                });
            }
        }.start();
    }

    void intialviews(){
        Verification_field=view.findViewById(R.id.Verification_code);
        next2=view.findViewById(R.id.next2);
        counter=view.findViewById(R.id.timer);
        resend_code=view.findViewById(R.id.resend_code);
    }
    public void login() {
        next2.setOnClickListener(new View.OnClickListener() {
            String Verificationcode;
            @Override
            public void onClick(View view) {
                Verificationcode=Verification_field.getText().toString();
           if(Verificationcode.isEmpty()){
            Verification_field.requestFocus();
            Toast.makeText(context, "please enter verification", Toast.LENGTH_SHORT).show();
               return;
        }
           if(id!=null){
               Verifycode(Verificationcode);
           }
            }
        });

        Loginwithphone(phone);

    }

    void Loginwithphone(String phonee){
        counter();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Toast.makeText(context, "sucssfussssaaaal", Toast.LENGTH_SHORT).show();
                String code=credential.getSmsCode();
                if(code!=null){
                    Verifycode(code);
                    //   Toast.makeText(getApplicationContext(), "sucssful", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(getActivity().getApplicationContext(), "ee1", Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(context, "ee2", Toast.LENGTH_SHORT).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(context, "ee3", Toast.LENGTH_SHORT).show();

                }
                Toast.makeText(context, "ee4", Toast.LENGTH_SHORT).show();
                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                id=verificationId;
                Toast.makeText(context, "ee5555554", Toast.LENGTH_SHORT).show();

                // Save verification ID and resending token so we can use them later
                //    mVerificationId = verificationId;
                //  mResendToken = token;

                // ...
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phonee,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
               getActivity(),               // Activity (for callback binding)
                mCallbacks);
    }
    void Verifycode(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(id,code);
        singninwithcraditial(credential);
    }

    private void singninwithcraditial(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "sucssesfl222", Toast.LENGTH_SHORT).show();
                    Register_fragment fragment3=new Register_fragment();
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fram,fragment3);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}

