package com.example.nogor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class post_an_addActivity extends AppCompatActivity {
    EditText areaname, sizeofhouse, rentcharge, describehouse, extracontact;
    String user_address, user_name, user_email, user_phone, user_password, user_dp;
    Switch switch1;
    Button rent;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_an_add);
        sizeofhouse=findViewById(R.id.sizeofhouse);
        areaname=findViewById(R.id.areaname);
        rentcharge=findViewById(R.id.rentcharge);
        describehouse=findViewById(R.id.describehouse);
        extracontact=findViewById(R.id.extracontact);
        switch1=findViewById(R.id.switch1);
        rent=findViewById(R.id.rent);


        final Spinner spinner=(Spinner)findViewById(R.id.districtname);

        String[] dist=getResources().getStringArray(R.array.bd_districts);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.spinnerlayout,R.id.txt, dist);

        spinner.setAdapter(adapter);
        getuserdata();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                text = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String  areaName=areaname.getText().toString();
              String areaName1=areaName.toLowerCase();
              String  sizeOfhouse=sizeofhouse.getText().toString();
              String  rentCharge=rentcharge.getText().toString();
              String  describeHouse=describehouse.getText().toString();
              String  extraContact=extracontact.getText().toString();
              String distRict=text;
              String bergain;
              if(switch1.isChecked())
              {
                  bergain="Yes";
              }
              else
              {
                  bergain="No";
              }
                String key="";
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user_phone).child("ad"); //user profile ad
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child("ad");  //universal ad
                key=reference1.push().getKey();
                User user=new User(areaName, areaName1, sizeOfhouse, rentCharge, describeHouse, extraContact, distRict, bergain, key, user_phone, user_name, user_dp);
                reference.child(key).setValue(user);
                reference1.child(key).setValue(user);

                Toast.makeText(post_an_addActivity.this, "Ad details successfully added!", Toast.LENGTH_SHORT).show();
                //startActivity();
                Intent intent = new Intent(getApplicationContext(), post_an_add_2Activity.class);
                intent.putExtra("userid", key);
                intent.putExtra("name", user_name);
                intent.putExtra("address", user_address);
                intent.putExtra("email", user_email);
                intent.putExtra("phone", user_phone);
                intent.putExtra("password", user_password);
                intent.putExtra("dp", user_dp);
                startActivity(intent);
                finish();
            }
        });
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