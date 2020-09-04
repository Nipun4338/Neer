package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class my_ads_Activity extends AppCompatActivity {

    private RecyclerView myView;
    private DatabaseReference myReference;

    String key1, user_phone, user_address, user_name, user_email, user_password, user_dp;
    Button edit_ad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads_);
        getuserdata();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_myads);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        //getuserdata();
                        Intent intent = new Intent(getApplicationContext(), profileActivity.class);
                        intent.putExtra("name", user_name);
                        intent.putExtra("address", user_address);
                        intent.putExtra("email", user_email);
                        intent.putExtra("phone", user_phone);
                        intent.putExtra("password", user_password);
                        intent.putExtra("dp", user_dp);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.action_myads:
                        return true;
                    case R.id.action_allads:
                        //getuserdata();
                        Intent intent1 = new Intent(getApplicationContext(), all_ads_Activity.class);
                        intent1.putExtra("name", user_name);
                        intent1.putExtra("address", user_address);
                        intent1.putExtra("email", user_email);
                        intent1.putExtra("phone", user_phone);
                        intent1.putExtra("password", user_password);
                        intent1.putExtra("dp", user_dp);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        myReference= FirebaseDatabase.getInstance().getReference("users").child(user_phone).child("ad");
        myReference.keepSynced(true);

        myView=findViewById(R.id.myrecycleview);
        myView.setHasFixedSize(true);
        myView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog, BlogViewHolder>
                (Blog.class, R.layout.blog_row, BlogViewHolder.class, myReference) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position)
            {
                viewHolder.setdistrictname(model.getDistrict());
                viewHolder.setadditionaldetails(model.getDescribeHouse());
                viewHolder.setbergain(model.getBergain());
                viewHolder.setcontact(model.getExtraContact());
                viewHolder.setrent(model.getRentCharge());
                viewHolder.setsize(model.getSizeOfhouse());
                viewHolder.setareaname(model.getAreaName());
                viewHolder.setimage(getApplicationContext(), model.getImage());
                final String key2=model.getKey();
                final String user_phone=model.getUser_phone();

                viewHolder.edit_ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), edit_my_ad_Activity.class);
                        intent.putExtra("key", key2);
                        intent.putExtra("name", user_name);
                        intent.putExtra("address", user_address);
                        intent.putExtra("email", user_email);
                        intent.putExtra("phone", user_phone);
                        intent.putExtra("password", user_password);
                        intent.putExtra("dp", user_dp);
                        startActivity(intent);
                    }
                });
            }
        };

        myView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        Button edit_ad;
        public BlogViewHolder(View itemView)
        {
            super(itemView);
            mview=itemView;
            this.edit_ad=itemView.findViewById(R.id.edit_ad);
        }

        public void setdistrictname(String district)
        {
            TextView dist=mview.findViewById(R.id.dist);
            dist.setText("District:- "+district);
        }
        public void setrent(String rent)
        {
            TextView rent1=mview.findViewById(R.id.rent_money);
            rent1.setText(rent+" ৳");
        }
        public void setadditionaldetails(String details)
        {
            TextView additionaldetails=mview.findViewById(R.id.description);
            additionaldetails.setText("Details:- "+details);
        }
        public void setcontact(String contact)
        {
            TextView contact1=mview.findViewById(R.id.contact);
            contact1.setText("Contact:- "+contact);
        }
        public void setbergain(String bergain)
        {
            TextView bergain1=mview.findViewById(R.id.bergain);
            bergain1.setText("Bergain:- "+bergain);
        }
        public void setsize(String size)
        {
            TextView size1=mview.findViewById(R.id.areasize);
            size1.setText("Area size:- "+size);
        }
        public void setareaname(String areaname1)
        {
            TextView areaname=mview.findViewById(R.id.areaNamex);
            areaname.setText("Area Name:- "+areaname1);
        }
        public void setimage(Context ctx, String image)
        {
            ImageView image1=mview.findViewById(R.id.post_image);
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
    }
}