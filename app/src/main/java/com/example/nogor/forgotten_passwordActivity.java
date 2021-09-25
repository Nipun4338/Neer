package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class forgotten_passwordActivity extends AppCompatActivity {

    private EditText forgottennumber;
    private Button forgottengetcode;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        forgottennumber = findViewById(R.id.forgottennumber);
        forgottengetcode = findViewById(R.id.forgottenverifycode);
        forgottengetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone=forgottennumber.getText().toString();
                if(phone.isEmpty())
                {
                    forgottennumber.setError("Invalid number...");
                    forgottennumber.requestFocus();
                    return;
                }
                phone=phone.substring(1);
                if(phone.isEmpty())
                {
                    forgottennumber.setError("Invalid number...");
                    forgottennumber.requestFocus();
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), forgotten_verifyActivity.class);
                intent.putExtra("phoneNo", phone);
                startActivity(intent);
                /*Toast.makeText(this, "Your account was created successfully!", Toast.LENGTH_SHORT).show();*/
            }
        });
    }
}