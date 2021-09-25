package com.example.nogor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.BlogViewHolder> {

    private Context mContext;
    private List<Blog1> blog1List=new ArrayList<>();
    String detailedarea;
    private DatabaseReference RootRef, RootRef2;
    String user_dp, user_name, user_address, user_email, user_password;

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("13");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_rows1_layout, parent, false);
        return new BlogViewHolder(view);
    }

    public searchAdapter(Context mContext, List<Blog1> blog1List) {
        this.mContext=mContext;
        this.blog1List = blog1List;
    }


    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder viewHolder, int position) {
        Blog1 blog1=blog1List.get(position);
        viewHolder.setdistrictname(blog1.getDistrict());
        viewHolder.setadditionaldetails(blog1.getDescribeHouse());
        viewHolder.setbergain(blog1.getBergain());
        viewHolder.setcontact(blog1.getExtraContact());
        viewHolder.setrent(blog1.getRentCharge());
        viewHolder.setsize(blog1.getSizeOfhouse());
        detailedarea=blog1.getDetailedareaName();
        viewHolder.setareaname(detailedarea, blog1.getAreaName());
        if(blog1.getImage()!=null && blog1.getImage().length()>0)
        {
            viewHolder.setimage(mContext, blog1.getImage());
        }
        final String key2=blog1.getKey();
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

        final String user_phone=blog1.getUser_phone();
        RootRef2 = FirebaseDatabase.getInstance().getReference("users");
        RootRef2.child(user_phone)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //if (dataSnapshot.child("userState").hasChild("state"))
                        {
                            user_dp = dataSnapshot.child("dp").getValue().toString();
                            user_email=dataSnapshot.child("email").getValue().toString();
                            user_address=dataSnapshot.child("address").getValue().toString();
                            user_password=dataSnapshot.child("password").getValue().toString();
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

        viewHolder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, message_Activity.class);
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
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println("13");
        return blog1List.size();
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
        public void setareaname(String detailedarea, String areaname1)
        {
            TextView areaname=mview.findViewById(R.id.areaNamex);
            areaname.setText("Area Name:- "+detailedarea+" , "+areaname1);
        }
        public void setimage(Context ctx, String image)
        {
            ImageView image1=mview.findViewById(R.id.post_image);
            //Picasso.with(ctx).load(image).into(image1);
            Glide.with(ctx).load(image).into(image1);
        }
    }
}
