package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class signActivity extends AppCompatActivity {

    private EditText number;
    private Button getcode;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        number = findViewById(R.id.number);
        getcode = findViewById(R.id.verifycode);
        getcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=number.getText().toString();
                if(phone.isEmpty())
                {
                    number.setError("Invalid number...");
                    number.requestFocus();
                    return;
                }
                phone=phone.substring(1);
                if(phone.isEmpty())
                {
                    number.setError("Invalid number...");
                    number.requestFocus();
                    return;
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                Query checkUser = reference.orderByChild("phone").equalTo(phone);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            number.setError("Account already exists!");
                            number.requestFocus();
                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(), verifyActivity.class);
                            intent.putExtra("phoneNo", phone);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                /*Toast.makeText(this, "Your account was created successfully!", Toast.LENGTH_SHORT).show();*/
            }
        });
    }
}