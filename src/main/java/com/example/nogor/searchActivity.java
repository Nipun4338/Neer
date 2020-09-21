package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class searchActivity extends AppCompatActivity {

    String user_address, user_name, user_email, user_phone, user_password, user_dp;
    EditText searchdistrict, searcharea, searchminium, searchmaximum;
    Button advancedsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchdistrict = findViewById(R.id.searchdistrict);
        searcharea = findViewById(R.id.searcharea);
        searchminium = findViewById(R.id.searchminimum);
        searchmaximum = findViewById(R.id.searchmaximum);
        advancedsearch = findViewById(R.id.advancedsearch);

        advancedsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchDistrict = searchdistrict.getText().toString().toLowerCase();
                String searchArea = searcharea.getText().toString().toLowerCase();
                String searchMinimum = searchminium.getText().toString();
                String searchMaximum = searchmaximum.getText().toString();

                if (searchArea.length() == 0 || searchDistrict.length() == 0 || searchMaximum.length() == 0 || searchMinimum.length() == 0) {
                    if (searchArea.length() == 0) {
                        searcharea.setError("Field cannot be empty");
                        searcharea.requestFocus();
                        return;
                    }
                    if (searchDistrict.length() == 0) {
                        searchdistrict.setError("Field cannot be empty");
                        searchdistrict.requestFocus();
                        return;
                    }
                    if (searchMaximum.length() == 0) {
                        searchmaximum.setError("Field cannot be empty");
                        searchmaximum.requestFocus();
                        return;
                    }
                    if (searchMinimum.length() == 0) {
                        searchminium.setError("Field cannot be empty");
                        searchminium.requestFocus();
                        return;
                    }
                }
                String search = searchDistrict + "_" + searchArea;
                Intent intent = new Intent(getApplicationContext(), search_resultsActivity.class);
                intent.putExtra("name", user_name);
                intent.putExtra("address", user_address);
                intent.putExtra("email", user_email);
                intent.putExtra("phone", user_phone);
                intent.putExtra("password", user_password);
                intent.putExtra("dp", user_dp);
                intent.putExtra("x", search);
                intent.putExtra("searchminium", searchMinimum);
                intent.putExtra("searchmaximum", searchMaximum);
                startActivity(intent);

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
        user_dp = intent.getStringExtra("dp");
    }
}
