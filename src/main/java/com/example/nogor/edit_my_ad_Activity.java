package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class edit_my_ad_Activity extends AppCompatActivity {
    String key, user_phone;
    String areaName2, areasize2, rent_money2, dist2, description2, contact2, bergain2, post_image2="", result1="";
    EditText rent_money1, dist1, areaName1, areasize1, description1, contact1, bergain1;
    String user_address, user_name, user_email, user_password, user_dp;
    Button edit_ad1, delete_ad;
    private final int PICK_IMAGE_REQUEST = 71;
    ArrayList<Uri> Imagelist= new ArrayList<Uri>();
    private Uri filePath;
    ImageView tv;
    int sum=0;
    int flag=0;
    DatabaseReference reference;
    StorageReference storageReference;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    String[] items={"Yes", "No"};
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
        tv=findViewById(R.id.post_image1);
        delete_ad=findViewById(R.id.delete_ad);


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
                if(post_image2!=null && post_image2.length()>0)
                {
                    setimage(post_image2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        
        storageReference = FirebaseStorage.getInstance().getReference("users/ad/"+key);

        edit_ad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference= FirebaseDatabase.getInstance().getReference("users");
                uploadImage();
                updatedata();
            }
        });

        delete_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder=new AlertDialog.Builder(edit_my_ad_Activity.this);
                builder.setTitle("Are you confirm to remove your ad?");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result1=items[i];
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(result1=="Yes")
                        {
                            deletead(key);
                        }
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void deletead(String key) {
        final DatabaseReference rootRef1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reference2=rootRef1.child("users").child(user_phone).child("ad").child(key);
        DatabaseReference reference3=rootRef1.child("users").child("ad").child(key);
        reference2.removeValue();
        reference3.removeValue();
        Toast.makeText(edit_my_ad_Activity.this, "Ad is deleted!", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext(), my_ads_Activity.class);
        intent.putExtra("name", user_name);
        intent.putExtra("address", user_address);
        intent.putExtra("email", user_email);
        intent.putExtra("phone", user_phone);
        intent.putExtra("password", user_password);
        intent.putExtra("dp", user_dp);
        startActivity(intent);
        finish();
    }

    public void updatedata()
    {
        isareanamechanged();
        isareasizechanged();
        isrent_moneychanged();
        isdescriptionchanged();
        iscontactchanged();
        isbergainchanged();
        isdistchanged();
        sum+=flag;
        if(sum>0)
        {
            Toast.makeText(this, "Your ad has been updated!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), my_ads_Activity.class);
            intent.putExtra("name", user_name);
            intent.putExtra("address", user_address);
            intent.putExtra("email", user_email);
            intent.putExtra("phone", user_phone);
            intent.putExtra("password", user_password);
            intent.putExtra("dp", user_dp);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "Account data is same and cannot be updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private void isareanamechanged()
    {
        if(areaName1.getText().toString()!=null && areaName1.getText().toString().length()>0 && !areaName2.equals(areaName1.getText().toString()))
        {
            reference.child(user_phone).child("ad").child(key).child("areaName").setValue(areaName1.getText().toString());
            reference.child("ad").child(key).child("areaName").setValue(areaName1.getText().toString());
            reference.child(user_phone).child("ad").child(key).child("areaName1").setValue(areaName1.getText().toString().toLowerCase());
            reference.child("ad").child(key).child("areaName1").setValue(areaName1.getText().toString().toLowerCase());
            sum++;
        }
    }

    private void isareasizechanged()
    {
        if(areasize1.getText().toString()!=null && areasize1.getText().toString().length()>0 && !areasize2.equals(areasize1.getText().toString()))
        {
            reference.child(user_phone).child("ad").child(key).child("sizeOfhouse").setValue(areasize1.getText().toString());
            reference.child("ad").child(key).child("sizeOfhouse").setValue(areasize1.getText().toString());
            sum++;
        }
    }

    private void isrent_moneychanged()
    {
        if(rent_money1.getText().toString()!=null && rent_money1.getText().toString().length()>0 && !rent_money2.equals(rent_money1.getText().toString()))
        {
            reference.child(user_phone).child("ad").child(key).child("rentCharge").setValue(rent_money1.getText().toString());
            reference.child("ad").child(key).child("rentCharge").setValue(rent_money1.getText().toString());
            sum++;
        }
    }

    private void isdescriptionchanged()
    {
        if(description1.getText().toString()!=null && description1.getText().toString().length()>0 && !description2.equals(description1.getText().toString()))
        {
            reference.child(user_phone).child("ad").child(key).child("describeHouse").setValue(description1.getText().toString());
            reference.child("ad").child(key).child("describeHouse").setValue(description1.getText().toString());
            sum++;
        }
    }

    private void iscontactchanged()
    {
        if(contact1.getText().toString()!=null && contact1.getText().toString().length()>0 && !contact2.equals(contact1.getText().toString()))
        {
            reference.child(user_phone).child("ad").child(key).child("extraContact").setValue(contact1.getText().toString());
            reference.child("ad").child(key).child("extraContact").setValue(contact1.getText().toString());
            sum++;
        }
    }

    private void isbergainchanged()
    {
        if(bergain1.getText().toString()!=null && bergain1.getText().toString().length()>0 && !bergain2.equals(bergain1.getText().toString()))
        {
            reference.child(user_phone).child("ad").child(key).child("bergain").setValue(bergain1.getText().toString());
            reference.child("ad").child(key).child("bergain").setValue(bergain1.getText().toString());
            sum++;
        }
    }

    private void isdistchanged()
    {
        if(dist1.getText().toString()!=null && dist1.getText().toString().length()>0 && !dist2.equals(dist1.getText().toString()))
        {
            reference.child(user_phone).child("ad").child(key).child("district").setValue(dist1.getText().toString());
            reference.child("ad").child(key).child("district").setValue(dist1.getText().toString());
            sum++;
        }
    }

    private void getuserdata() {
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        user_address = intent.getStringExtra("address");
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");
        user_dp = intent.getStringExtra("dp");
    }

    public void perform_action(View v)
    {
        chooseImage();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Imagelist.add(filePath);
            tv.setImageURI(filePath);
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            {
                Uri individula_image=Imagelist.get(0);
                final StorageReference ref = storageReference.child(individula_image.getLastPathSegment());
                ref.putFile(individula_image)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                {
                                    String y=String.valueOf(1);
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url=String.valueOf(uri);
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user_phone).child("ad").child(key).child("image");
                                            //reference.child("image").setValue(url);
                                            reference.setValue(url);
                                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child("ad").child(key).child("image");
                                            reference1.setValue(url);
                                            flag=1;
                                        }
                                    });
                                }
                                //Toast.makeText(post_an_add_2Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //progressDialog.dismiss();
                                Toast.makeText(edit_my_ad_Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });
            }
            progressDialog.dismiss();
            //btnUpload.setVisibility(View.GONE);
        }
        else
        {

        }
    }
    public void setimage(String image)
    {
        //Picasso.with(ctx).load(image).into(image1);
        Glide.with(edit_my_ad_Activity.this)
                .load(image)
                .into(tv);
    }
}