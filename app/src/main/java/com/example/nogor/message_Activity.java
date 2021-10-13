package com.example.nogor;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.nogor.Notification.ApiClient;
import com.example.nogor.Notification.NotificationApiService;
import com.example.nogor.Notification.Result;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class message_Activity extends AppCompatActivity {

    String key;
    String user_key, user_phone, user_address, user_name, user_email, user_password, user_dp, customer_name, customer_phone, customer_dp;
    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID;

    private TextView userName, userLastSeen;
    private CircleImageView userImage;

    private Toolbar ChatToolBar;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, RootRef1, RootRef2, NotificationRef;

    private FloatingActionButton SendMessageButton, SendFilesButton;
    private EditText MessageInputText;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView userMessagesList;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private DatabaseReference RootRefz;
    private String checker="", myUrl="";
    private StorageTask uploadTask;
    private Uri fileUri;
    private ProgressDialog loadingBar;


    private String saveCurrentTime, saveCurrentDate;
    //FirebaseRecyclerOptions<chatMessage> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_);

        getuserdata();
        mAuth = FirebaseAuth.getInstance();
        messageSenderID = mAuth.getCurrentUser().getUid();
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        RootRefz = FirebaseDatabase.getInstance().getReference("users");
        NotificationRef = FirebaseDatabase.getInstance().getReference("Notifications");

        RootRef = FirebaseDatabase.getInstance().getReference("Messages");
        RootRef1 = FirebaseDatabase.getInstance().getReference("History");
        RootRef2 = FirebaseDatabase.getInstance().getReference("users");
        RootRef.keepSynced(true);
        RootRef1.keepSynced(true);
        RootRef2.keepSynced(true);


        messageReceiverID = customer_phone;
        messageReceiverName = customer_name;
        messageReceiverImage = customer_dp;
        RootRef2.child(messageReceiverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //if (dataSnapshot.child("userState").hasChild("state"))
                        {
                            String dp1 = dataSnapshot.child("dp").getValue().toString();
                            messageReceiverImage=dp1;
                            userName.setText(messageReceiverName);
                            if(messageReceiverImage!=null && messageReceiverImage.length()>0)
                            {
                                Picasso.get().load(messageReceiverImage).placeholder(R.drawable.profile).into(userImage);
                            }
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


        IntializeControllers();


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SendMessage();
            }
        });

        DisplayLastSeen();


        SendFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[] = new CharSequence[]
                        {
                                "Images",
                                "PDF Files",
                                "Ms Word Files"
                        };
                AlertDialog.Builder builder=new AlertDialog.Builder(message_Activity.this);
                builder.setTitle("Select the file");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(i==0)
                        {
                            checker="image";

                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, "Select Image"), 438);
                        }
                        if(i==1)
                        {
                            checker="pdf";
                        }
                        if(i==2)
                        {
                            checker="docx";
                        }
                    }
                });
                builder.show();
            }
        });
        RootRef.child(messageSenderID).child(messageReceiverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        Messages messages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        messageAdapter.notifyDataSetChanged();

                        userMessagesList.smoothScrollToPosition(userMessagesList.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        /*NotificationRef.child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.hasChild("state"))
                        {
                            String state = dataSnapshot.child("status").getValue().toString();

                            if (!state.equals(null) && state.equals("unread"))
                            {
                                Toast.makeText(getApplicationContext(), "New Message!",
                                        Toast.LENGTH_LONG).show();
                                HashMap<String, String> chatnotification=new HashMap<>();
                                chatnotification.put("status", "read");
                                NotificationRef.child(firebaseUser.getUid())
                                        .setValue(chatnotification);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==438 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            loadingBar.setTitle("Sending File");
            loadingBar.setMessage("Please Wait. File is sending...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            fileUri=data.getData();

            if(!checker.equals("image"))
            {

            }
            else if(checker.equals("image"))
            {
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image Files");

                final String messageSenderRef = messageSenderID + "/" + messageReceiverID;
                final String messageReceiverRef = messageReceiverID + "/" + messageSenderID;

                DatabaseReference userMessageKeyRef = RootRef
                        .child(messageSenderID).child(messageReceiverID).push();

                DatabaseReference userMessageKeyRef1 = RootRef1.
                        child(messageReceiverID).child(user_key).child(messageSenderID);

                chat chat1=new chat(user_name, FirebaseAuth.getInstance().getUid(), user_dp);
                userMessageKeyRef1.setValue(chat1);

                final String messagePushID = userMessageKeyRef.getKey();

                final StorageReference filePath=storageReference.child(messagePushID+"."+"jpg");
                uploadTask=filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Uri downloadUrl=task.getResult();
                            myUrl=downloadUrl.toString();

                            Map messageTextBody = new HashMap();
                            messageTextBody.put("message", myUrl);
                            messageTextBody.put("name", fileUri.getLastPathSegment());
                            messageTextBody.put("type", checker);
                            messageTextBody.put("from", messageSenderID);
                            messageTextBody.put("to", messageReceiverID);
                            messageTextBody.put("messageID", messagePushID);
                            messageTextBody.put("time", saveCurrentTime);
                            messageTextBody.put("date", saveCurrentDate);

                            Map messageBodyDetails = new HashMap();
                            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
                            messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

                            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        loadingBar.dismiss();
                                        HashMap<String, String> chatnotification=new HashMap<>();
                                        chatnotification.put("from", messageSenderID);
                                        chatnotification.put("to", messageReceiverID);
                                        NotificationRef.child(messageReceiverID).push()
                                                .setValue(chatnotification);

                                        Toast.makeText(message_Activity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(message_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                    MessageInputText.setText("");
                                }
                            });
                        }
                    }
                });
            }
            else
            {
                loadingBar.dismiss();
                Toast.makeText(this, "Nothing selected, Error.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void DisplayLastSeen()
    {
        RootRef2.child(messageReceiverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //if (dataSnapshot.child("userState").hasChild("state"))
                        {
                            String state = dataSnapshot.child("userState").child("state").getValue().toString();
                            String date = dataSnapshot.child("userState").child("date").getValue().toString();
                            String time = dataSnapshot.child("userState").child("time").getValue().toString();

                            if (state.equals("online"))
                            {
                                userLastSeen.setText("online");
                            }
                            else if (state.equals("offline"))
                            {
                                userLastSeen.setText("Last Seen: " + date + " " + time);
                            }
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
    }

    private void IntializeControllers()
    {

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.custom_chat_bar, null);
        actionBar.setCustomView(actionBarView);

        userName = (TextView) findViewById(R.id.custom_profile_name);
        userLastSeen = (TextView) findViewById(R.id.custom_user_last_seen);
        userImage = (CircleImageView) findViewById(R.id.custom_profile_image);

        SendMessageButton = (FloatingActionButton) findViewById(R.id.send_message_btn);
        SendFilesButton = (FloatingActionButton) findViewById(R.id.send_files_btn);
        MessageInputText = (EditText) findViewById(R.id.input_message);

        messageAdapter = new MessageAdapter(messagesList);
        userMessagesList = (RecyclerView) findViewById(R.id.private_messages_list_of_users);
        linearLayoutManager = new LinearLayoutManager(this);
        userMessagesList.setLayoutManager(linearLayoutManager);
        userMessagesList.setAdapter(messageAdapter);


        loadingBar=new ProgressDialog(this);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currentTime.format(calendar.getTime());
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



    private void SendMessage()
    {
        String messageText = MessageInputText.getText().toString();

        if (TextUtils.isEmpty(messageText))
        {
            Toast.makeText(this, "first write your message...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //String chathistory = messageReceiverID + "/" + user_key + "/" + messageSenderID;
            String messageSenderRef = messageSenderID + "/" + messageReceiverID;
            String messageReceiverRef = messageReceiverID + "/" + messageSenderID;

            DatabaseReference userMessageKeyRef = RootRef
                    .child(messageSenderID).child(messageReceiverID).push();

            DatabaseReference userMessageKeyRef1 = RootRef1.
                    child(messageReceiverID).child(user_key).child(messageSenderID);

            chat chat1=new chat(user_name, FirebaseAuth.getInstance().getUid(), user_dp);
            userMessageKeyRef1.setValue(chat1);

            String messagePushID = userMessageKeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderID);
            messageTextBody.put("to", messageReceiverID);
            messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);

            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSenderRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put( messageReceiverRef + "/" + messagePushID, messageTextBody);

            RootRef.updateChildren(messageBodyDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {

                        final String[] devicetoken = new String[1];

                        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                devicetoken[0] =snapshot.child("users").child(messageReceiverID).child("device_token")
                                        .getValue(String.class);

                                //JsonObject payload = buildNotificationPayload(devicetoken[0]);
                                // send notification to receiver ID
                                String device=devicetoken[0];

                                //NotificationApiService api = new NotificationApiService();
                                Result result=new Result();
                                result.setTo(device);
                                HashMap<String, String> chatnotification=new HashMap<>();
                                chatnotification.put("from", messageSenderID);
                                chatnotification.put("to", messageReceiverID);
                                chatnotification.put("status", "unread");
                                /*NotificationRef.child(messageReceiverID).push()
                                        .setValue(chatnotification);*/
                                NotificationRef.child(messageReceiverID)
                                        .setValue(chatnotification);

                                ApiClient.getApiService().sendNotification(result)
                            .enqueue(new Callback<Result>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onResponse(Call<Result> call, Response<Result> response) {
                                                if (response.isSuccessful()) {
                                                    //Result result1=response.body();
                                                    //System.out.println(result1.getTo());
                                                    //String fcm=response.body().getAsJsonObject("to").toString();
                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                    String nowUser = user.getUid();
                                                    if(messageReceiverID.equals(nowUser))
                                                    {
                                                        Toast.makeText(getApplicationContext(), "New Message!",
                                                                Toast.LENGTH_LONG).show();

                                                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                                        String CHANNEL_ID=messageReceiverID;
                                                        NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_HIGH);
                                                        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,intent,0);
                                                        Notification notification=new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                                                                .setContentText("Heading")
                                                                .setContentTitle("You got a message!")
                                                                .setContentIntent(pendingIntent)
                                                                .addAction(android.R.drawable.sym_action_chat,"Message",pendingIntent)
                                                                .setChannelId(CHANNEL_ID)
                                                                .setSmallIcon(android.R.drawable.sym_action_chat)
                                                                .build();

                                                        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                        notificationManager.createNotificationChannel(notificationChannel);
                                                        notificationManager.notify(1,notification);
                                                    }
                                                }
                                            }
                                            @Override public void onFailure(Call<Result> call, Throwable t) {
                                            }

                                        });

                                Toast.makeText(message_Activity.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(message_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    MessageInputText.setText("");
                }
            });
        }
    }

    /*private JsonObject buildNotificationPayload(String id) {
        // compose notification json payload
        JsonObject payload = new JsonObject();
        payload.addProperty("to", id);
        // compose data payload here
        /*JsonObject data = new JsonObject();
        data.addProperty("title", "483");
        data.addProperty("message", "483");
        // add data payload
        payload.add("data", data);
        return payload;
    }*/

    private void getuserdata() {
        Intent intent = getIntent();
        user_address = intent.getStringExtra("address");
        user_name = intent.getStringExtra("name");
        user_email = intent.getStringExtra("email");
        user_phone = intent.getStringExtra("phone");
        user_password = intent.getStringExtra("password");
        user_dp = intent.getStringExtra("dp");
        user_key = intent.getStringExtra("key");
        customer_phone = intent.getStringExtra("customer_phone");
        customer_name = intent.getStringExtra("customer_name");
        customer_dp = intent.getStringExtra("customer_dp");
    }

}