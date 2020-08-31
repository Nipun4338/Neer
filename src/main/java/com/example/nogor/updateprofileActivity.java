package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class updateprofileActivity extends AppCompatActivity {

    EditText name1, phone1, email1, password1, address1;
    TextView name2, phone2, email2, password2, address2;
    String name, phone, email, address, password, pic;
    Button update;
    DatabaseReference reference;
    String user_id, user_phone;
    private Uri filePath;
    ImageView dp;
    int flag=0;

    private final int PICK_IMAGE_REQUEST = 71;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        getuserdata();

        name2=findViewById(R.id.text);
        name1=findViewById(R.id.editname);
        email2=findViewById(R.id.text1);
        email1=findViewById(R.id.editemail);
        address2=findViewById(R.id.text2);
        address1=findViewById(R.id.editaddress);
        password2=findViewById(R.id.text3);
        password1=findViewById(R.id.editpassword);
        update=findViewById(R.id.update);
        dp=findViewById(R.id.dp);

        if(pic.length()>0)
        {
            setimage(pic);
        }

        name2.setText(name);
        email2.setText(email);
        address2.setText(address);
        password2.setText(password);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("userid");
        user_phone=intent.getStringExtra("phone");
        storageReference = FirebaseStorage.getInstance().getReference("users/"+user_phone+"/pic");

        reference= FirebaseDatabase.getInstance().getReference("users");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name2=findViewById(R.id.text);
                name1=findViewById(R.id.editname);
                email2=findViewById(R.id.text1);
                email1=findViewById(R.id.editemail);
                address2=findViewById(R.id.text2);
                address1=findViewById(R.id.editaddress);
                password2=findViewById(R.id.text3);
                password1=findViewById(R.id.editpassword);
                update=findViewById(R.id.update);

                reference= FirebaseDatabase.getInstance().getReference("users");
                updatedata();
                uploadImage();
            }
        });

    }

    private void getuserdata() {
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        phone = intent.getStringExtra("phone");
        password = intent.getStringExtra("password");
        pic = intent.getStringExtra("dp");
    }

    public void updatedata()
    {
        if(isnamechanged() || isemailchanged() || isaddresschanged() || ispasswordchanged() || flag==1)
        {
            Toast.makeText(this, "Account data has been updated!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Account data is same and cannot be updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isnamechanged()
    {
        if(!name.equals(name1.getText().toString()) && name1.getText().toString().length()>0)
        {
            reference.child(phone).child("name").setValue(name1.getText().toString());
            name2.setText(name1.getText().toString());
            name=name1.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isemailchanged()
    {
        if(!email.equals(email1.getText().toString()) && email1.getText().toString().length()>0)
        {
            reference.child(phone).child("email").setValue(email1.getText().toString());
            email2.setText(email1.getText().toString());
            email=email1.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean isaddresschanged()
    {
        if(!address.equals(address1.getText().toString()) && address1.getText().toString().length()>0)
        {
            reference.child(phone).child("address").setValue(address1.getText().toString());
            address2.setText(address1.getText().toString());
            address=address1.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
    private boolean ispasswordchanged()
    {
        if(!password.equals(password1.getText().toString()) && password1.getText().toString().length()>0)
        {
            reference.child(phone).child("password").setValue(password1.getText().toString());
            password2.setText(password1.getText().toString());
            password=password1.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }

    public void perform_action(View v)
    {
        TextView tv= (TextView) findViewById(R.id.pic);

        //alter text of textview widget
        tv.setText("Add a profile photo");

        //assign the textview forecolor
        //tv.setTextColor(Color.GREEN);
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
            dp.setImageURI(filePath);
            flag=1;
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            Uri individula_image = filePath;
            final StorageReference ref = storageReference.child(individula_image.getLastPathSegment());
            ref.putFile(individula_image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = String.valueOf(uri);
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user_phone).child("dp");
                                //reference.child("image").setValue(url);
                                reference.setValue(url);
                            }
                        });
                        //Toast.makeText(post_an_add_2Activity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //progressDialog.dismiss();
                        Toast.makeText(updateprofileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded " + (int) progress + "%");
                    }
                });
            progressDialog.dismiss();
        }
    }

    public void setimage(String image)
    {
        //Picasso.with(ctx).load(image).into(image1);
        Glide.with(updateprofileActivity.this)
                .load(image)
                .into(dp);
    }
}