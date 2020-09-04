package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import androidx.appcompat.widget.SearchView;
import com.squareup.picasso.Picasso;

public class all_ads_Activity extends AppCompatActivity {

    private RecyclerView myView;
    private DatabaseReference myReference;
    String user_phone, user_address, user_name, user_email, user_password, user_dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_ads_);
        getuserdata();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.action_allads);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        //getuserdata();
                        Intent intent=new Intent(getApplicationContext(), profileActivity.class);
                        intent.putExtra("name", user_name);
                        intent.putExtra("address", user_address);
                        intent.putExtra("email", user_email);
                        intent.putExtra("phone", user_phone);
                        intent.putExtra("password", user_password);
                        intent.putExtra("dp", user_dp);
                        startActivity(intent);
                        overridePendingTransition(0,0);
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
                        return true;
                }
                return false;
            }
        });

        myReference= FirebaseDatabase.getInstance().getReference("users").child("ad");
        myReference.keepSynced(true);

        myView=findViewById(R.id.myrecycleview1);
        myView.setHasFixedSize(true);
        myView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void firebaseSearch(String search)
    {
        search=search.toLowerCase();
        Query firebaseSearchQuery=myReference.orderByChild("areaName1").startAt(search).endAt(search+"\uf8ff");

        FirebaseRecyclerAdapter<Blog1, BlogViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog1, BlogViewHolder>
                (Blog1.class, R.layout.blog_rows1_layout, BlogViewHolder.class, firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog1 model, int position)
            {
                viewHolder.setdistrictname(model.getDistrict());
                viewHolder.setadditionaldetails(model.getDescribeHouse());
                viewHolder.setbergain(model.getBergain());
                viewHolder.setcontact(model.getExtraContact());
                viewHolder.setrent(model.getRentCharge());
                viewHolder.setsize(model.getSizeOfhouse());
                viewHolder.setareaname(model.getAreaName());
                viewHolder.setimage(getApplicationContext(), model.getImage());
            }
        };

        myView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog1, BlogViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Blog1, BlogViewHolder>
                (Blog1.class, R.layout.blog_rows1_layout, BlogViewHolder.class, myReference) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog1 model, int position)
            {
                viewHolder.setdistrictname(model.getDistrict());
                viewHolder.setadditionaldetails(model.getDescribeHouse());
                viewHolder.setbergain(model.getBergain());
                viewHolder.setcontact(model.getExtraContact());
                viewHolder.setrent(model.getRentCharge());
                viewHolder.setsize(model.getSizeOfhouse());
                viewHolder.setareaname(model.getAreaName());
                viewHolder.setimage(getApplicationContext(), model.getImage());
            }
        };

        myView.setAdapter(firebaseRecyclerAdapter);
    }
    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        public BlogViewHolder(View itemView)
        {
            super(itemView);
            mview=itemView;
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

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                firebaseSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                firebaseSearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
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