package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class profileActivity extends AppCompatActivity {

    ImageView settings, dp;
    String user_address, user_name, user_email, user_phone, user_password, user_dp;
    Button postad, lookforad, lookforbuy, signout;
    TextView name;
    private DatabaseReference RootRef, RootRef2;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getuserdata();

        postad=findViewById(R.id.postad);
        lookforad=findViewById(R.id.lookforad);
        lookforbuy=findViewById(R.id.lookforbuy);
        signout=findViewById(R.id.signout);
        name=findViewById(R.id.textView5);
        dp=findViewById(R.id.dp);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference("users");

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
        name.setText("Welcome "+user_name+"!");
        RootRef2 = FirebaseDatabase.getInstance().getReference("users");
        RootRef2.child(user_phone)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //if (dataSnapshot.child("userState").hasChild("state"))
                        {
                            user_dp = dataSnapshot.child("dp").getValue().toString();
                            if(user_dp!=null && user_dp.length()>0)
                            {
                                setimage(user_dp);
                            }
                        }
                        /*else
                        {
                            userLastSeen.setText("offline");
                        }*/
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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
                intent.putExtra("dp", user_dp);
                startActivity(intent);
            }
        });

        lookforad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), all_ads_Activity.class);
                intent.putExtra("phone", user_phone);
                startActivity(intent);
            }
        });

        lookforbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this, look_for_houseActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        return true;
                    case R.id.action_myads:
                        //getuserdata();
                        Intent intent2=new Intent(getApplicationContext(), my_ads_Activity.class);
                        intent2.putExtra("name", user_name);
                        intent2.putExtra("address", user_address);
                        intent2.putExtra("email", user_email);
                        intent2.putExtra("phone", user_phone);
                        intent2.putExtra("password", user_password);
                        intent2.putExtra("dp", user_dp);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.action_allads:
                        //getuserdata();
                        Intent intent1=new Intent(getApplicationContext(), all_ads_Activity.class);
                        intent1.putExtra("name", user_name);
                        intent1.putExtra("address", user_address);
                        intent1.putExtra("email", user_email);
                        intent1.putExtra("phone", user_phone);
                        intent1.putExtra("password", user_password);
                        intent1.putExtra("dp", user_dp);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (firebaseUser == null)
        {
            finish();
        }
        else
        {
            updateUserStatus("online");

        }
    }


    @Override
    protected void onStop()
    {
        super.onStop();

        if (firebaseUser != null)
        {
            updateUserStatus("offline");
        }
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (firebaseUser != null)
        {
            updateUserStatus("offline");
        }
    }

    private void updateUserStatus(String state)
    {
        String saveCurrentTime, saveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        HashMap<String, Object> onlineStateMap = new HashMap<>();
        onlineStateMap.put("time", saveCurrentTime);
        onlineStateMap.put("date", saveCurrentDate);
        onlineStateMap.put("state", state);

        RootRef.child(firebaseUser.getUid()).child("userState")
                .updateChildren(onlineStateMap);

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

}