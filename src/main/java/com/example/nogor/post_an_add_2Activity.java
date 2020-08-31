package com.example.nogor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class post_an_add_2Activity extends AppCompatActivity {


    private Button btnChoose, btnUpload;
    private ImageView imageView1, imageView2, imageView3, imageView4;


    ArrayList<Uri> Imagelist= new ArrayList<Uri>();
    private Uri filePath;

    int x=0;
    private final int PICK_IMAGE_REQUEST = 71;
    String user_id, user_phone;

    StorageReference storageReference;

    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_an_add_2);

        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3=findViewById(R.id.imageView3);
        imageView4=findViewById(R.id.imageView4);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("userid");
        user_phone=intent.getStringExtra("phone");
        storageReference = FirebaseStorage.getInstance().getReference("users/ad/"+user_id);


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x++;
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }


    private String getExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
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
            if(x==1)
            {
                imageView1.setImageURI(filePath);
            }
            if(x==2)
            {
                imageView2.setImageURI(filePath);
            }
            if(x==3)
            {
                imageView3.setImageURI(filePath);
            }
            if(x==4)
            {
                imageView4.setImageURI(filePath);
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            for(x=0; x<Imagelist.size(); x++)
            {
                Uri individula_image=Imagelist.get(x);
                final StorageReference ref = storageReference.child(individula_image.getLastPathSegment());
                ref.putFile(individula_image)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                if(x==1)
                                {
                                    String y=String.valueOf(x);
                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String url=String.valueOf(uri);
                                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user_phone).child("ad").child(user_id).child("image");
                                            //reference.child("image").setValue(url);
                                            reference.setValue(url);
                                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child("ad").child(user_id).child("image");
                                            reference1.setValue(url);
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
                                Toast.makeText(post_an_add_2Activity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(post_an_add_2Activity.this, "Ad successfully added!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), my_ads_Activity.class);
            intent.putExtra("phone", user_phone);
            intent.putExtra("key", user_id);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(post_an_add_2Activity.this, "Ad successfully added!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), my_ads_Activity.class);
            intent.putExtra("phone", user_phone);
            intent.putExtra("key", user_id);
            startActivity(intent);
            finish();
        }
    }
}