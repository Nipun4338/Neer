package com.example.nogor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileActivity extends AppCompatActivity {

    ImageView settings;
    String user_address, user_name, user_email, user_phone, user_password;
    Button postad, lookforad, lookforbuy, signout;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        postad=findViewById(R.id.postad);
        lookforad=findViewById(R.id.lookforad);
        lookforbuy=findViewById(R.id.lookforbuy);
        signout=findViewById(R.id.signout);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        getuserdata();
        settings=findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), updateprofileActivity.class);
                intent.putExtra("name", user_name);
                intent.putExtra("address", user_address);
                intent.putExtra("email", user_email);
                intent.putExtra("phone", user_phone);
                intent.putExtra("password", user_password);
                startActivity(intent);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(profileActivity.this, loginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        postad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), post_an_addActivity.class);
                intent.putExtra("name", user_name);
                intent.putExtra("address", user_address);
                intent.putExtra("email", user_email);
                intent.putExtra("phone", user_phone);
                intent.putExtra("password", user_password);
                startActivity(intent);
            }
        });

        lookforad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this, my_ads_Activity.class));
            }
        });

        lookforbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this, look_for_houseActivity.class));
            }
        });

    }

    private void getuserdata() {
            Intent intent = getIntent();
            user_address = intent.getStringExtra("address");
            user_name = intent.getStringExtra("name");
            user_email = intent.getStringExtra("email");
            user_phone = intent.getStringExtra("phone");
            user_password = intent.getStringExtra("password");
    }
}