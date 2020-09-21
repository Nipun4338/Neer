package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button login;
    private Button signup;
    ImageView example1;
    private static final long ONE_SECOND = 1000L;
    private static final int MISS_LIMIT = 1;
    int misses = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //example1=findViewById(R.id.imageView10);

        final Handler handler = new Handler();
        final Runnable timer = new Runnable() {
            @Override
            public void run() {
                // user too late: increment miss counter
                if (++misses >= MISS_LIMIT) {
                    //TODO miss limit reached
                    call();
                }
            }
        };
        handler.removeCallbacks(timer);
        handler.postDelayed(timer, ONE_SECOND);

        /*example1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(timer);
                startActivity(new Intent(MainActivity.this,aboutusActivity.class));
            }
        });*/

    }

    void call()
    {
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivity(new Intent(MainActivity.this, loginActivity.class));
            finish();
        }
        else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String phone = user.getUid();
            //example1.setText(phone);
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference reference=rootRef.child("users").child(phone);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //if (snapshot.hasChild(phone)) {
                    String nameFromDB = snapshot.child("name").getValue(String.class);
                    String addressFromDB = snapshot.child("address").getValue(String.class);
                    String phoneFromDB = snapshot.child("phone").getValue(String.class);
                    String emailFromDB = snapshot.child("email").getValue(String.class);
                    String passwordFromDB = snapshot.child("password").getValue(String.class);
                    String dpFromDB = snapshot.child("dp").getValue(String.class);

                    Intent intent = new Intent(getApplicationContext(), profileActivity.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("address", addressFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("phone", phoneFromDB);
                    intent.putExtra("password", passwordFromDB);
                    if(dpFromDB==null || dpFromDB.length()==0)
                    {
                        intent.putExtra("dp", "");
                    }
                    else
                    {
                        intent.putExtra("dp", dpFromDB);
                    }
                    if(nameFromDB.length()==0 || addressFromDB.length()==0 || phoneFromDB.length()==0)
                    {
                        Intent intent1 = new Intent(MainActivity.this, loginActivity.class);
                        startActivity(intent1);
                        finish();
                    }
                    Toast.makeText(MainActivity.this, "Welcome "+nameFromDB+"!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                    //} else {
                        /*Intent intent = new Intent(MainActivity.this, loginActivity.class);
                        startActivity(intent);
                        finish();
                    }*/
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    /*public void perform_action(View v)
    {
        ImageView tv= (ImageView) findViewById(R.id.imageView10);

        //alter text of textview widget
        //tv.setText("About Us");

        //assign the textview forecolor
        //tv.setTextColor(Color.GREEN);
        startActivity(new Intent(MainActivity.this,aboutusActivity.class));
    }*/

}