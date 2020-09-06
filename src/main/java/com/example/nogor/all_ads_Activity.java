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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class all_ads_Activity extends AppCompatActivity {

    private RecyclerView myView;
    private DatabaseReference myReference;
    private FirebaseRecyclerAdapter<Blog1,BlogViewHolder> adapter;
    String user_phone, user_address, user_name, user_email, user_password, user_dp;
    Button message;
    FirebaseRecyclerOptions<Blog1> options, options1;
    Query query;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_ads_);
        getuserdata();

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference("users");
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

        query = FirebaseDatabase.getInstance().getReference("users").child("ad");
        query.keepSynced(true);
        options = new FirebaseRecyclerOptions.Builder<Blog1>()
                .setQuery(query,Blog1.class)
                .build();

        myView=findViewById(R.id.myrecycleview1);
        myView.setHasFixedSize(true);
        myView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void firebaseSearch(String search)
    {
        search=search.toLowerCase();
        Query firebaseSearchQuery=myReference.orderByChild("areaName1").startAt(search).endAt(search+"\uf8ff");
        options1 = new FirebaseRecyclerOptions.Builder<Blog1>()
                .setQuery(firebaseSearchQuery,Blog1.class)
                .build();

        adapter =new FirebaseRecyclerAdapter<Blog1, all_ads_Activity.BlogViewHolder>
                (options1) {
            @Override
            public all_ads_Activity.BlogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new all_ads_Activity.BlogViewHolder(LayoutInflater.from(all_ads_Activity.this)
                        .inflate(R.layout.blog_rows1_layout,viewGroup,false));
            }
            @Override
            protected void onBindViewHolder(@NonNull all_ads_Activity.BlogViewHolder viewHolder, int position, @NonNull Blog1 model)
            {
                viewHolder.setdistrictname(model.getDistrict());
                viewHolder.setadditionaldetails(model.getDescribeHouse());
                viewHolder.setbergain(model.getBergain());
                viewHolder.setcontact(model.getExtraContact());
                viewHolder.setrent(model.getRentCharge());
                viewHolder.setsize(model.getSizeOfhouse());
                viewHolder.setareaname(model.getAreaName());
                if(model.getImage()!=null && model.getImage().length()>0)
                {
                    viewHolder.setimage(getApplicationContext(), model.getImage());
                }

                final String key2=model.getKey();
                final String user_phone=model.getUser_phone();
                viewHolder.message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), message_Activity.class);
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
        adapter.startListening();
        myView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseUser == null)
        {
            finish();
        }
        else
        {
            updateUserStatus("online");
        }

        adapter=new FirebaseRecyclerAdapter<Blog1, all_ads_Activity.BlogViewHolder>
                (options) {
            @Override
            public all_ads_Activity.BlogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new all_ads_Activity.BlogViewHolder(LayoutInflater.from(all_ads_Activity.this)
                        .inflate(R.layout.blog_rows1_layout,viewGroup,false));
            }
            @Override
            protected void onBindViewHolder(@NonNull all_ads_Activity.BlogViewHolder viewHolder, int position, @NonNull Blog1 model)
            {
                viewHolder.setdistrictname(model.getDistrict());
                viewHolder.setadditionaldetails(model.getDescribeHouse());
                viewHolder.setbergain(model.getBergain());
                viewHolder.setcontact(model.getExtraContact());
                viewHolder.setrent(model.getRentCharge());
                viewHolder.setsize(model.getSizeOfhouse());
                viewHolder.setareaname(model.getAreaName());
                if(model.getImage()!=null && model.getImage().length()>0)
                {
                    viewHolder.setimage(getApplicationContext(), model.getImage());
                }

                final String key2=model.getKey();
                final DatabaseReference rootRefx = FirebaseDatabase.getInstance().getReference("users");
                final String[] phoneFromDB = new String[1];
                final String[] nameFromDB = new String[1];
                final String[] dpFromDB = new String[1];
                DatabaseReference reference=rootRefx.child("ad").child(key2);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //if (snapshot.hasChild(phone)) {
                        phoneFromDB[0] = snapshot.child("user_phone").getValue(String.class);
                        nameFromDB[0] = snapshot.child("user_name").getValue(String.class);
                        dpFromDB[0] = snapshot.child("user_dp").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                final String user_phone=model.getUser_phone();
                viewHolder.message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), message_Activity.class);
                        intent.putExtra("key", key2);
                        intent.putExtra("name", user_name);
                        intent.putExtra("address", user_address);
                        intent.putExtra("email", user_email);
                        intent.putExtra("phone", user_phone);
                        intent.putExtra("password", user_password);
                        intent.putExtra("dp", user_dp);
                        intent.putExtra("customer_phone", phoneFromDB[0]);
                        intent.putExtra("customer_name", nameFromDB[0]);
                        intent.putExtra("customer_dp", dpFromDB[0]);
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.startListening();
        myView.setAdapter(adapter);
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

    public static class BlogViewHolder extends RecyclerView.ViewHolder
    {
        View mview;
        Button message;
        public BlogViewHolder(View itemView)
        {
            super(itemView);
            mview=itemView;
            this.message=itemView.findViewById(R.id.message);
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