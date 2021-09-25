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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class post_an_addActivity extends AppCompatActivity {
    EditText areaname, detailedareaname, sizeofhouse, rentcharge, describehouse, extracontact;
    String user_address, user_name, user_email, user_phone, user_password, user_dp;
    Switch switch1;
    Button rent;
    String text;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference RootRefz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_an_add);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        RootRefz = FirebaseDatabase.getInstance().getReference("users");

        sizeofhouse=findViewById(R.id.sizeofhouse);
        areaname=findViewById(R.id.areaname);
        rentcharge=findViewById(R.id.rentcharge);
        describehouse=findViewById(R.id.describehouse);
        extracontact=findViewById(R.id.extracontact);
        switch1=findViewById(R.id.switch1);
        rent=findViewById(R.id.rent);
        detailedareaname=findViewById(R.id.detailedareaname);

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
              String  areaName1=areaName.toLowerCase();
              String  detailedareaName=detailedareaname.getText().toString();
              String  sizeOfhouse=sizeofhouse.getText().toString();
              String  rentCharge=rentcharge.getText().toString();
              String  describeHouse=describehouse.getText().toString();
              String  extraContact=extracontact.getText().toString();
              String distRict=text;
              String district1=distRict.toLowerCase();
              String bergain;
              if(switch1.isChecked())
              {
                  bergain="Yes";
              }
              else
              {
                  bergain="No";
              }

              if(areaName.length()==0 || sizeOfhouse.length()==0 || rentCharge.length()==0 || describeHouse.length()==0 || extraContact.length()==0 || detailedareaName.length()==0)
              {
                  if(areaName.length()==0)
                  {
                      areaname.setError("Field cannot be empty");
                      areaname.requestFocus();
                      return;
                  }
                  if(detailedareaName.length()==0)
                  {
                      detailedareaname.setError("Field cannot be empty");
                      detailedareaname.requestFocus();
                      return;
                  }
                  if(sizeOfhouse.length()==0)
                  {
                      sizeofhouse.setError("Field cannot be empty");
                      sizeofhouse.requestFocus();
                      return;
                  }
                  if(rentCharge.length()==0)
                  {
                      rentcharge.setError("Field cannot be empty");
                      rentcharge.requestFocus();
                      return;
                  }
                  if(describeHouse.length()==0)
                  {
                      describehouse.setError("Field cannot be empty");
                      describehouse.requestFocus();
                      return;
                  }
                  if(extraContact.length()==0)
                  {
                      extracontact.setError("Field cannot be empty");
                      extracontact.requestFocus();
                      return;
                  }
              }
                String key="";
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user_phone).child("ad"); //user profile ad
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child("ad");  //universal ad
                key=reference1.push().getKey();
                String x=district1+"_"+areaName1;
                User user=new User(areaName, areaName1, detailedareaName, sizeOfhouse, rentCharge, describeHouse, extraContact, distRict, district1, bergain, key, user_phone, user_name, user_dp, x);
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
        user_address = intent.getStringExtra("address");
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");
        user_dp = intent.getStringExtra("dp");
    }
}