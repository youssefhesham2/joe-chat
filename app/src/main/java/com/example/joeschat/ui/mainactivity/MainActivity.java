package com.example.joeschat.ui.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.joeschat.R;
import com.example.joeschat.ui.chat_activity.ChatActivity;
import com.example.joeschat.ui.mainactivity.enterphone_fragment.EnterPhone_fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fram,new EnterPhone_fragment());
        fragmentTransaction.commit();
    }

/*  public void validation(View view) {
        String phone,Verificationcode;
        phone=phone_field.getText().toString();
        Verificationcode=Verification_code.getText().toString();
        if(phone.isEmpty()){
            Toast.makeText(this, "phone number is required", Toast.LENGTH_SHORT).show();
            phone_field.requestFocus();
            return;
        }
        if(Verificationcode.isEmpty()){
            Toast.makeText(this, "password is required", Toast.LENGTH_SHORT).show();
            Verification_code.requestFocus();
            return;
        }
        Loginwithphone(phone);
    }

    public void idonthaveaccount(View view) {
    }
    void Loginwithphone(String phone){

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
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
                Toast.makeText(getApplicationContext(), "ee1", Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                    Toast.makeText(getApplicationContext(), "ee2", Toast.LENGTH_SHORT).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    Toast.makeText(getApplicationContext(), "ee3", Toast.LENGTH_SHORT).show();

                }
                Toast.makeText(getApplicationContext(), "ee4", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "ee5555554", Toast.LENGTH_SHORT).show();

                // Save verification ID and resending token so we can use them later
                //    mVerificationId = verificationId;
                //  mResendToken = token;

                // ...
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
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
                    Toast.makeText(getApplicationContext(), "sucssesfl222", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
}*/
}
