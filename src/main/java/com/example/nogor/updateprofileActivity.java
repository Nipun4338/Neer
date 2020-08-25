package com.example.nogor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class updateprofileActivity extends AppCompatActivity {

    EditText name1, phone1, email1, password1, address1;
    TextView name2, phone2, email2, password2, address2;
    String name, phone, email, address, password;
    Button update;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        getuserdata();

        name2=findViewById(R.id.text);
        name1=findViewById(R.id.editname);
        email2=findViewById(R.id.text1);
        email1=findViewById(R.id.editemail);
        address2=findViewById(R.id.text2);
        address1=findViewById(R.id.editaddress);
        password2=findViewById(R.id.text3);
        password1=findViewById(R.id.editpassword);
        update=findViewById(R.id.update);

        name2.setText(name);
        email2.setText(email);
        address2.setText(address);
        password2.setText(password);

        reference= FirebaseDatabase.getInstance().getReference("users");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name2=findViewById(R.id.text);
                name1=findViewById(R.id.editname);
                email2=findViewById(R.id.text1);
                email1=findViewById(R.id.editemail);
                address2=findViewById(R.id.text2);
                address1=findViewById(R.id.editaddress);
                password2=findViewById(R.id.text3);
                password1=findViewById(R.id.editpassword);
                update=findViewById(R.id.update);

                reference= FirebaseDatabase.getInstance().getReference("users");
                updatedata();
            }
        });

    }

    private void getuserdata() {
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        password = intent.getStringExtra("password");
    }

    public void updatedata()
    {
        if(isnamechanged() || isemailchanged() || isaddresschanged() || ispasswordchanged())
        {
            Toast.makeText(this, "Account data has been updated!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Account data is same and cannot be updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isnamechanged()
    {
        if(!name.equals(name1.getText().toString()) && name1.getText().toString().length()>0)
        {
            reference.child(phone).child("name").setValue(name1.getText().toString());
            name2.setText(name1.getText().toString());
            name=name1.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isemailchanged()
    {
        if(!email.equals(email1.getText().toString()) && email1.getText().toString().length()>0)
        {
            reference.child(phone).child("email").setValue(email1.getText().toString());
            email2.setText(email1.getText().toString());
            email=email1.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isaddresschanged()
    {
        if(!address.equals(address1.getText().toString()) && address1.getText().toString().length()>0)
        {
            reference.child(phone).child("address").setValue(address1.getText().toString());
            address2.setText(address1.getText().toString());
            address=address1.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean ispasswordchanged()
    {
        if(!password.equals(password1.getText().toString()) && password1.getText().toString().length()>0)
        {
            reference.child(phone).child("password").setValue(password1.getText().toString());
            password2.setText(password1.getText().toString());
            password=password1.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
}