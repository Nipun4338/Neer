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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class check_all_messages extends AppCompatActivity {
    String user_address, user_name, user_email, user_phone, user_password, user_dp, user_key, customer_name, customer_phone, customer_dp;
    Query query;
    private DatabaseReference myReference;
    private FirebaseRecyclerAdapter<chat1, check_all_messages.BlogViewHolder> adapter;
    FirebaseRecyclerOptions<chat1> options;
    private RecyclerView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_all_messages);

        getuserdata();

        query = FirebaseDatabase.getInstance().getReference("History").child(user_phone).child(user_key);
        query.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<chat1>()
                .setQuery(query,chat1.class)
                .build();

        myView=findViewById(R.id.myrecycleview2);
        myView.setHasFixedSize(true);
        myView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter=new FirebaseRecyclerAdapter<chat1, check_all_messages.BlogViewHolder>
                (options) {
            @Override
            public check_all_messages.BlogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new check_all_messages.BlogViewHolder(LayoutInflater.from(check_all_messages.this)
                        .inflate(R.layout.chat_layout,viewGroup,false));
            }
            @Override
            protected void onBindViewHolder(@NonNull BlogViewHolder viewHolder, int position, @NonNull chat1 model)
            {
                viewHolder.setname(model.getName());
                if(model.getDp()!=null && model.getDp().length()>0)
                {
                    viewHolder.setimage(getApplicationContext(), model.getDp());
                }
                customer_phone=model.getId();
                customer_name=model.getName();
                customer_dp=model.getDp();

                //final String user_phone=model.getUser_phone();
                viewHolder.message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), message_Activity.class);
                        intent.putExtra("key", user_key);
                        intent.putExtra("name", user_name);
                        intent.putExtra("address", user_address);
                        intent.putExtra("email", user_email);
                        intent.putExtra("phone", user_phone);
                        intent.putExtra("password", user_password);
                        intent.putExtra("dp", user_dp);
                        intent.putExtra("customer_phone", customer_phone);
                        intent.putExtra("customer_name", customer_name);
                        intent.putExtra("customer_dp", customer_dp);
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.startListening();
        myView.setAdapter(adapter);
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        Button message;
        public BlogViewHolder(View itemView)
        {
            super(itemView);
            mview=itemView;
            this.message=itemView.findViewById(R.id.sent_messages);
        }

        public void setname(String Name)
        {
            TextView name=mview.findViewById(R.id.Name);
            name.setText(Name);
        }
        public void setimage(Context ctx, String image)
        {
            ImageView image1=mview.findViewById(R.id.profile_image);
            //Picasso.with(ctx).load(image).into(image1);
            Glide.with(ctx).load(image).into(image1);
        }
    }

    private void getuserdata() {
        Intent intent = getIntent();
        user_address = intent.getStringExtra("address");
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");
        user_dp = intent.getStringExtra("dp");
        user_key = intent.getStringExtra("key");
    }
}