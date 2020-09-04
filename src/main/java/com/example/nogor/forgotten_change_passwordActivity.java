package com.example.nogor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class forgotten_change_passwordActivity extends AppCompatActivity {

    EditText forgottenpassword;
    Button forgottenpasswordsubmit;
    String phone;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_change_password);

        Intent intent = getIntent();
        phone = intent.getStringExtra("phoneNo");

        forgottenpassword=findViewById(R.id.forgottenpassword);
        forgottenpasswordsubmit=findViewById(R.id.forgottenpasswordsubmit);

        reference= FirebaseDatabase.getInstance().getReference("users");

        forgottenpasswordsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference= FirebaseDatabase.getInstance().getReference("users").child("phone");
                updatedata();
            }
        });
    }

    public void updatedata()
    {
            if(forgottenpassword==null || forgottenpassword.length()<=0)
            {
                forgottenpassword.setError("Invalid Password...");
            }
            else
            {
                reference.child("password").setValue(forgottenpassword.getText().toString());
                Toast.makeText(this, "Password has been updated!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(forgotten_change_passwordActivity.this, loginActivity.class));
            }
    }

}