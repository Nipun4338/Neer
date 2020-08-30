package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class all_ads_Activity extends AppCompatActivity {

    private RecyclerView myView;
    private DatabaseReference myReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_ads_);

        myReference= FirebaseDatabase.getInstance().getReference("users").child("ad");
        myReference.keepSynced(true);

        myView=findViewById(R.id.myrecycleview1);
        myView.setHasFixedSize(true);
        myView.setLayoutManager(new LinearLayoutManager(this));


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
}