package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class search_resultsActivity extends AppCompatActivity {

    String user_address, user_name, user_email, user_phone, user_password, user_dp, search, search_minium, search_maximum;
    private DatabaseReference myReference;
    private RecyclerView myView;
    Query query;
    FirebaseRecyclerOptions<Blog1> options, options1;
    private searchAdapter adapter;
    String detailedarea;
    private List<Blog1>blog1List;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        getuserdata();
        myView=findViewById(R.id.myrecycleview3);
        myView.setHasFixedSize(true);
        myView.setLayoutManager(new LinearLayoutManager(this));
        blog1List=new ArrayList<>();
        adapter=new searchAdapter(search_resultsActivity.this,blog1List);
        myView.setAdapter(adapter);

        myReference= FirebaseDatabase.getInstance().getReference("users").child("ad");
        myReference.keepSynced(true);
        query = FirebaseDatabase.getInstance().getReference("users").child("ad");
        query.keepSynced(true);


        /*Query firebaseSearchQuery1=myReference.orderByChild("rentCharge").startAt(search_minium).endAt(search_maximum);
        firebaseSearchQuery1.addListenerForSingleValueEvent(valueEventListener);*/

        Query firebaseSearchQuery=myReference.orderByChild("x").equalTo(search);
        firebaseSearchQuery.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            blog1List.clear();
            if(snapshot.exists()) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    //Toast.makeText(search_resultsActivity.this, "13", Toast.LENGTH_SHORT).show();
                    Blog1 blog1 = datasnapshot.getValue(Blog1.class);
                    int y=Integer.parseInt(blog1.getRentCharge());
                    int y1=Integer.parseInt(search_minium);
                    int y2=Integer.parseInt(search_maximum);
                    if(y>=y1 && y<=y2)
                    {
                        blog1List.add(blog1);
                    }
                    //Toast.makeText(search_resultsActivity.this, "14", Toast.LENGTH_SHORT).show();
                }
                adapter=new searchAdapter(search_resultsActivity.this, blog1List);
                myView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    private void getuserdata() {
        Intent intent = getIntent();
        user_address = intent.getStringExtra("address");
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");
        user_dp = intent.getStringExtra("dp");
        search=intent.getStringExtra("x");
        search_minium=intent.getStringExtra("searchminium");
        search_maximum=intent.getStringExtra("searchmaximum");
    }
}