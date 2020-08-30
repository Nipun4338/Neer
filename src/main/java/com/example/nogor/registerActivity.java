package com.example.nogor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerActivity extends AppCompatActivity {

    EditText name, password, email, address;
    Button signup;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=findViewById(R.id.name);
        password=findViewById(R.id.password);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        signup=findViewById(R.id.signup);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("users");



                String name1=name.getText().toString();
                String password1=password.getText().toString();
                String email1=email.getText().toString();
                String address1=address.getText().toString();

                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

                if(name1.length()==0 || password1.length()==0 || address1.length()==0)
                {
                    if(name1.length()==0)
                    {
                        name.setError("Field cannot be empty");
                        name.requestFocus();
                        return;
                    }
                    if(password1.length()==0)
                    {
                        password.setError("Field cannot be empty");
                        password.requestFocus();
                        return;
                    }
                    if(address1.length()==0)
                    {
                        address.setError("Field cannot be empty");
                        address.requestFocus();
                        return;
                    }
                }
                else
                {
                    String phone = user.getUid();
                    userHelperClass helperClass=new userHelperClass(name1, password1, email1,address1, phone);
                    reference.child(phone).setValue(helperClass);
                    Toast.makeText(registerActivity.this, "Account Successfully Created!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), profileActivity.class);
                    intent.putExtra("name", name1);
                    intent.putExtra("address", address1);
                    intent.putExtra("email", email1);
                    intent.putExtra("phone", phone);
                    intent.putExtra("password", password1);
                    intent.putExtra("dp", "");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}