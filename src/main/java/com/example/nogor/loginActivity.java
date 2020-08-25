package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.Nullable;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import android.util.Log;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    EditText phone, pass;
    Button log;
    String Phone;

    private static final String TAG = "LoginRegisterActivity";
    int AUTHUI_REQUEST_CODE = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //phone=findViewById(R.id.phone);
        //pass=findViewById(R.id.pass);
        log=findViewById(R.id.log);
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(loginActivity.this, profileActivity.class));
            this.finish();
        }

        /*log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });*/


    }

    public void handleLoginRegister(View view) {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                .setAlwaysShowSignInMethodScreen(true)
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, AUTHUI_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // We have signed in the user or we have a new user
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: " + user.toString());
                //Checking for User (New/Old)
                final String phone = user.getUid();
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //if (snapshot.hasChild(phone)) {
                        String nameFromDB = snapshot.child(phone).child("name").getValue(String.class);
                        String addressFromDB = snapshot.child(phone).child("address").getValue(String.class);
                        String phoneFromDB = snapshot.child(phone).child("phone").getValue(String.class);
                        String emailFromDB = snapshot.child(phone).child("email").getValue(String.class);
                        String passwordFromDB = snapshot.child(phone).child("password").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), profileActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("address", addressFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phone", phoneFromDB);
                        intent.putExtra("password", passwordFromDB);
                        if (phoneFromDB !=null && phoneFromDB.length()> 0) {
                            Toast.makeText(loginActivity.this, "Welcome "+nameFromDB+"!", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent1 = new Intent(loginActivity.this, registerActivity.class);
                            startActivity(intent1);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            } else {
                // Signing in failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request");
                } else {
                    Log.e(TAG, "onActivityResult: ", response.getError());
                }
            }
        }
    }









    private Boolean validateUsername() {
        Phone = phone.getText().toString();
        if (Phone.isEmpty()) {
            phone.setError("Field cannot be empty");
            phone.requestFocus();
            return false;
        } else {
            Phone=Phone.substring(1);
            if (Phone.isEmpty()) {
                phone.setError("Field cannot be empty");
                phone.requestFocus();
                return false;
            }
            phone.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = pass.getText().toString();
        if (val.isEmpty()) {
            pass.setError("Field cannot be empty");
            pass.requestFocus();
            return false;
        } else {
            pass.setError(null);
            return true;
        }
    }

    public void loginUser() {
        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private void isUser()
    {
        final String userEnteredUsername = Phone;
        final String userEnteredPassword = pass.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUser = reference.orderByChild("phone").equalTo(userEnteredUsername);

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {

                    phone.setError(null);
                    String passwordfromdb=snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if(passwordfromdb.equals(userEnteredPassword))
                    {
                        pass.setError(null);
                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String addressFromDB = snapshot.child(userEnteredUsername).child("address").getValue(String.class);
                        String phoneFromDB = snapshot.child(userEnteredUsername).child("phone").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), profileActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("address", addressFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("phone", phoneFromDB);
                        intent.putExtra("password", passwordfromdb);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        pass.setError("Wrong Password!");
                        pass.requestFocus();
                    }
                }
                else
                {
                    phone.setError("No such user exists!");
                    phone.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void perform_action(View v)
    {
        TextView tv= (TextView) findViewById(R.id.forgot);

        //alter text of textview widget
        //tv.setText("About Us");

        //assign the textview forecolor
        tv.setTextColor(Color.BLUE);
        startActivity(new Intent(loginActivity.this,forgotten_passwordActivity.class));
    }
}