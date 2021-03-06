package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class forgotten_verifyActivity extends AppCompatActivity {


        private EditText forgottencode;
        private Button forgottenverify;
        ProgressBar progressBar;
        String verificationCodeBySystem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_verify);

            forgottencode=findViewById(R.id.forgotten_verify);
            forgottenverify=findViewById(R.id.forgottensignsubmit);
            progressBar=findViewById(R.id.progress_bar);

            String phone = getIntent().getStringExtra("phoneNo");
            sendverificationtouser(phone);
            forgottenverify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String c=forgottencode.getText().toString();
                    if(c.isEmpty())
                    {
                        forgottencode.setError("Invalid OTP...");
                        forgottencode.requestFocus();
                        return;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    verifyCode(c);
                }
            });

        }

        private void sendverificationtouser(String phone) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+880"+phone,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }

        private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCodeBySystem = s;
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(forgotten_verifyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        private void verifyCode(String codebyuser)
        {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codebyuser);
            signinuser(credential);
        }

        private void signinuser(PhoneAuthCredential credential) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.signInWithCredential(credential).addOnCompleteListener(forgotten_verifyActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        /*startActivity(new Intent(verifyActivity.this, registerActivity.class));*/
                        String phone = getIntent().getStringExtra("phoneNo");
                        Intent intent = new Intent(getApplicationContext(), forgotten_change_passwordActivity.class);
                        intent.putExtra("phoneNo", phone);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(forgotten_verifyActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
}