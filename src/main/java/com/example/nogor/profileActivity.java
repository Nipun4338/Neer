package com.example.nogor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class profileActivity extends AppCompatActivity {

    ImageView settings, dp;
    String user_address, user_name, user_email, user_phone, user_password, user_dp;
    Button postad, lookforad, lookforbuy, signout;
    TextView name;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        postad=findViewById(R.id.postad);
        lookforad=findViewById(R.id.lookforad);
        lookforbuy=findViewById(R.id.lookforbuy);
        signout=findViewById(R.id.signout);
        name=findViewById(R.id.textView6);
        dp=findViewById(R.id.dp);

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
                intent.putExtra("dp", user_dp);
                startActivity(intent);
            }
        });
        name.setText(user_name+"!");
        if(user_dp.length()>0)
        {
            setimage(user_dp);
        }

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
                intent.putExtra("dp", user_dp);
                startActivity(intent);
            }
        });

        lookforad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this, all_ads_Activity.class));
            }
        });

        lookforbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this, look_for_houseActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    private void getuserdata() {
            Intent intent = getIntent();
            user_address = intent.getStringExtra("address");
            user_name = intent.getStringExtra("name");
            user_email = intent.getStringExtra("email");
            user_phone = intent.getStringExtra("phone");
            user_password = intent.getStringExtra("password");
            user_dp = intent.getStringExtra("dp");
    }

    public void setimage(String image)
    {
        //Picasso.with(ctx).load(image).into(image1);
        Glide.with(profileActivity.this)
                .load(image)
                .into(dp);
    }

    public void perform_action(View v)
    {
        TextView tv= (TextView) findViewById(R.id.myad);
        Intent intent = new Intent(getApplicationContext(), my_ads_Activity.class);
        intent.putExtra("phone", user_phone);
        startActivity(intent);
    }
}