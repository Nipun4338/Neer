package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edit_my_ad_Activity extends AppCompatActivity {
    String key, user_phone;
    String areaName2, areasize2, rent_money2, dist2, description2, contact2, bergain2, post_image2;
    EditText rent_money1, dist1, areaName1, areasize1, description1, contact1, bergain1;
    Button edit_ad1;
    ImageView post_image1;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_ad_);
        getuserdata();
        rent_money1=findViewById(R.id.rent_money1);
        dist1=findViewById(R.id.dist1);
        areaName1=findViewById(R.id.areaName1);
        areasize1=findViewById(R.id.areasize1);
        description1=findViewById(R.id.description1);
        contact1=findViewById(R.id.contact1);
        bergain1=findViewById(R.id.bergain1);
        edit_ad1=findViewById(R.id.edit_ad1);

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference1=rootRef.child("users").child(user_phone).child("ad").child(key);
        reference1.keepSynced(true);

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //if (snapshot.hasChild(phone)) {
                areaName2 = snapshot.child("areaName").getValue(String.class);
                areaName1.setHint("Area: "+areaName2);
                rent_money2 = snapshot.child("rentCharge").getValue(String.class);
                rent_money1.setHint(rent_money2);
                areasize2 = snapshot.child("sizeOfhouse").getValue(String.class);
                areasize1.setHint("Area-size: "+areasize2);
                description2 = snapshot.child("describeHouse").getValue(String.class);
                description1.setHint("Description: "+description2);
                contact2 = snapshot.child("extraContact").getValue(String.class);
                contact1.setHint("Contact: "+contact2);
                bergain2 = snapshot.child("bergain").getValue(String.class);
                bergain1.setHint("Bergain: "+bergain2);
                dist2 = snapshot.child("district").getValue(String.class);
                dist1.setHint("District: "+dist2);
                post_image2=snapshot.child("image").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toast.makeText(edit_my_ad_Activity.this, areaName2, Toast.LENGTH_SHORT).show();

        edit_ad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference= FirebaseDatabase.getInstance().getReference("users");
                updatedata();
            }
        });

    }

    public void updatedata()
    {
        if(isareanamechanged() || isareasizechanged() || isrent_moneychanged() || isdescriptionchanged() || iscontactchanged() || isbergainchanged() || isdistchanged())
        {
            Toast.makeText(this, "Your ad has been updated!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(edit_my_ad_Activity.this, profileActivity.class));
        }
        else
        {
            Toast.makeText(this, "Account data is same and cannot be updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isareanamechanged()
    {
        if(!areaName2.equals(areaName1.getText().toString()) && areaName1.getText().toString().length()>0)
        {
            reference.child(user_phone).child("ad").child(key).child("areaName").setValue(areaName1.getText().toString());
            reference.child("ad").child(key).child("areaName").setValue(areaName1.getText().toString());
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isareasizechanged()
    {
        if(!areasize2.equals(areasize1.getText().toString()) && areasize1.getText().toString().length()>0)
        {
            reference.child(user_phone).child("ad").child(key).child("sizeOfhouse").setValue(areasize1.getText().toString());
            reference.child("ad").child(key).child("sizeOfhouse").setValue(areasize1.getText().toString());
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isrent_moneychanged()
    {
        if(!rent_money2.equals(rent_money1.getText().toString()) && rent_money1.getText().toString().length()>0)
        {
            reference.child(user_phone).child("ad").child(key).child("rentCharge").setValue(rent_money1.getText().toString());
            reference.child("ad").child(key).child("rentCharge").setValue(rent_money1.getText().toString());
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isdescriptionchanged()
    {
        if(!description2.equals(description1.getText().toString()) && description1.getText().toString().length()>0)
        {
            reference.child(user_phone).child("ad").child(key).child("describeHouse").setValue(description1.getText().toString());
            reference.child("ad").child(key).child("describeHouse").setValue(description1.getText().toString());
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean iscontactchanged()
    {
        if(!contact2.equals(contact1.getText().toString()) && contact1.getText().toString().length()>0)
        {
            reference.child(user_phone).child("ad").child(key).child("extraContact").setValue(contact1.getText().toString());
            reference.child("ad").child(key).child("extraContact").setValue(contact1.getText().toString());
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isbergainchanged()
    {
        if(!bergain2.equals(bergain1.getText().toString()) && bergain1.getText().toString().length()>0)
        {
            reference.child(user_phone).child("ad").child(key).child("bergain").setValue(bergain1.getText().toString());
            reference.child("ad").child(key).child("bergain").setValue(bergain1.getText().toString());
            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isdistchanged()
    {
        if(!dist2.equals(dist1.getText().toString()) && dist1.getText().toString().length()>0)
        {
            reference.child(user_phone).child("ad").child(key).child("district").setValue(dist1.getText().toString());
            reference.child("ad").child(key).child("district").setValue(dist1.getText().toString());
            return true;
        }
        else
        {
            return false;
        }
    }

    private void getuserdata() {
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        user_phone = intent.getStringExtra("phone");
    }


}