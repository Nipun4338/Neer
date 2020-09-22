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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class updateprofileActivity extends AppCompatActivity {

    EditText name1, phone1, email1, password1, address1;
    TextView name2, phone2, email2, password2, address2;
    String name, phone, email, address, password, pic, url;
    Button update;
    DatabaseReference reference;
    String user_id, user_phone;
    private Uri filePath;
    ImageView dp;
    int flag=0;
    int sum=0;
    ArrayList<Uri> Imagelist= new ArrayList<Uri>();
    private final int PICK_IMAGE_REQUEST = 71;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference RootRefz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        getuserdata();
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        RootRefz = FirebaseDatabase.getInstance().getReference("users");

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

        name1.setHint(name);
        email1.setHint(email);
        address1.setHint(address);
        password1.setHint(password);
        storageReference = FirebaseStorage.getInstance().getReference("users/"+phone+"/pic");

        reference= FirebaseDatabase.getInstance().getReference("users");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*name2=findViewById(R.id.text);
                name1=findViewById(R.id.editname);
                email2=findViewById(R.id.text1);
                email1=findViewById(R.id.editemail);
                address2=findViewById(R.id.text2);
                address1=findViewById(R.id.editaddress);
                password2=findViewById(R.id.text3);
                password1=findViewById(R.id.editpassword);*/
                //update=findViewById(R.id.update);

                reference= FirebaseDatabase.getInstance().getReference("users");
                uploadImage();
                updatedata();
            }
        });

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (firebaseUser == null)
        {
            finish();
        }
        else
        {
            updateUserStatus("online");
        }
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

        RootRefz.child(firebaseUser.getUid()).child("userState")
                .updateChildren(onlineStateMap);

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
        isnamechanged();
        isemailchanged();
        isaddresschanged();
        ispasswordchanged();
        //Toast.makeText(updateprofileActivity.this, sum, Toast.LENGTH_SHORT).show();
        sum+=flag;
        if(sum>0)
        {
            Toast.makeText(updateprofileActivity.this, "Account data has been updated!", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(), profileActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("address", address);
            intent.putExtra("email", email);
            intent.putExtra("phone", phone);
            intent.putExtra("password", password);
            intent.putExtra("dp", pic);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(updateprofileActivity.this, "Account data is same and cannot be updated!", Toast.LENGTH_SHORT).show();
        }
    }

    private void isnamechanged()
    {
        if(name1.getText().toString()!=null && name1.getText().toString().length()>0 && !name.equals(name1.getText().toString()))
        {
            reference.child(phone).child("name").setValue(name1.getText().toString());
            //name2.setText(name1.getText().toString());
            name=name1.getText().toString();
            sum++;
        }
    }
    private void isemailchanged()
    {
        if(email1.getText().toString()!=null && email1.getText().toString().length()>0 && !email.equals(email1.getText().toString()))
        {
            reference.child(phone).child("email").setValue(email1.getText().toString());
            //email2.setText(email1.getText().toString());
            email=email1.getText().toString();
            sum++;
        }
    }
    private void isaddresschanged()
    {
        if(address1.getText().toString()!=null && address1.getText().toString().length()>0 && !address.equals(address1.getText().toString()))
        {
            reference.child(phone).child("address").setValue(address1.getText().toString());
            //address2.setText(address1.getText().toString());
            address=address1.getText().toString();
            sum++;
            //return true;
        }
    }
    private void ispasswordchanged()
    {
        if(password1.getText().toString()!=null && password1.getText().toString().length()>0 && !password.equals(password1.getText().toString()))
        {
            reference.child(phone).child("password").setValue(password1.getText().toString());
            //password2.setText(password1.getText().toString());
            password=password1.getText().toString();
            sum++;
            //return true;
        }
    }

    public void perform_action(View v)
    {
        Button tv= (Button) findViewById(R.id.addPhoto);

        //alter text of textview widget
        //tv.setText("Add a profile photo");

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
            Imagelist.add(filePath);
            dp.setImageURI(filePath);
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            flag=1;
            Uri individula_image=Imagelist.get(0);
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child(individula_image.getLastPathSegment());
            ref.putFile(individula_image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String y=String.valueOf(1);
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                pic = String.valueOf(uri);
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(phone).child("dp");
                                //reference.child("image").setValue(url);
                                reference.setValue(pic);
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