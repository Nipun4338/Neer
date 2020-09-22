package com.example.nogor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class show_ImageActivity extends AppCompatActivity {

    String user_address, user_name, user_email, user_phone, user_password, user_dp, image;
    ImageView image1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__image);

        image1=findViewById(R.id.show);
        Glide.with(this).load(image).into(image1);
    }

    private void getuserdata() {
        Intent intent = getIntent();
        user_address = intent.getStringExtra("address");
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");
        user_dp = intent.getStringExtra("dp");
        image=intent.getStringExtra("image");
    }
}