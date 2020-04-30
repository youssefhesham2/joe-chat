package com.example.joeschat.ui.chat_intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joeschat.R;
import com.example.joeschat.models.ChatModel;
import com.example.joeschat.models.UserModel;
import com.example.joeschat.ui.chat_activity.freinds.FreindsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// this intent use to send and display massage
public class Chat_intent extends AppCompatActivity {

    private UserModel userModel;
    private RecyclerView recyclerView;
    private CircleImageView imageView;
    private EditText massage_field;
    private ImageView send_button;
    private TextView textView;
    private String uid;
    private UserModel mymodel;
    private chatadapter chatadapter_;
    private List<ChatModel> chatModels=new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_intent);
        userModel=(UserModel)getIntent().getSerializableExtra("model");
        initalviews();
        inialfirebase();
        mymodel();
        getchat();
    }

    private void getchat() {
        //this massage to get chat and display in adapter
        databaseReference.child("chat").child(uid).child(userModel.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              chatModels.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    ChatModel chatModel=snapshot.getValue(ChatModel.class);
                    chatModels.add(chatModel);
                }
                chatadapter_=new chatadapter(chatModels);
                recyclerView.setAdapter(chatadapter_);
                if(chatModels.size()>1){
                    recyclerView.smoothScrollToPosition(chatModels.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        lastseen();
    }

    private void sendmassage() {
        // get massage from edit text and check it empty or no
        String massage=massage_field.getText().toString();
        //if empty i will return
        if(massage.isEmpty()){
            Toast.makeText(this, "please enter massage", Toast.LENGTH_SHORT).show();
           massage_field.requestFocus();
            return;
        }
        //else i will send massage
        send(massage);
    }

    private void send(String massage) {
        //get date
        SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
        String  currentTime = df.format(new Date());
        //get id for massage
        String key=databaseReference.child("chat").child(uid).push().getKey();
        //make caht model
        ChatModel chatModel=new ChatModel(massage,mymodel.getUserName(),mymodel.getImgeUrl(),uid,userModel.getUid(),currentTime,key,false);
        //send massage to database in my id
        databaseReference.child("chat").child(uid).child(userModel.getUid()).child(key).setValue(chatModel);
        //send massage to database in he id
        databaseReference.child("chat").child(userModel.getUid()).child(uid).child(key).setValue(chatModel);
        massage_field.setText("");
        usersinchat();
    }

    private void mymodel(){
        //this method get my model
        databaseReference.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mymodel=dataSnapshot.getValue(UserModel.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void inialfirebase() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    private void initalviews() {
        recyclerView=findViewById(R.id.recycler_chattt);
        massage_field=findViewById(R.id.massage);
        send_button=findViewById(R.id.send_button);
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendmassage();
            }
        });
       //its image and name of user i chat with you
        imageView=findViewById(R.id.chat_image);
        textView=findViewById(R.id.chat_name);

        Picasso.get().load(userModel.getImgeUrl()).into(imageView);
        textView.setText(userModel.getUserName());

    }

    class chatadapter extends RecyclerView.Adapter<chatadapter.chatviewholder> {
        List<ChatModel> chatModelss;

        public chatadapter(List<ChatModel> chatModelss) {
            this.chatModelss = chatModelss;
        }

        @NonNull
        @Override
        public chatadapter.chatviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.massage_item, parent, false);
            return new chatadapter.chatviewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final chatadapter.chatviewholder holder, int position) {
            ChatModel chatModel=chatModelss.get(position);

            holder.massage.setText(chatModel.getMassage());
            holder.time.setText(chatModel.getTime());
            //check if i am send it massage make massage in right
            if(chatModel.getSenderid().equals(uid)){
                holder.gravity.setGravity(Gravity.END);
                holder.back_color.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                holder.time.setTextColor(getResources().getColor(R.color.white));
                holder.massage.setTextColor(getResources().getColor(R.color.white));

            }
            if(position==chatModelss.size()-1){
                if(chatModel.isIsseen()){
                    holder.lastseen.setText("seen");
                }
                else {
                    holder.lastseen.setText("Delivered");
                    holder.lastseen.setTextColor(getResources().getColor(R.color.white));
                }
            }
           else {
                holder.lastseen.setVisibility(View.GONE);

            }
           if(!chatModel.getSenderid().equals(uid)){
               holder.lastseen.setVisibility(View.GONE);
           }
        }

        @Override
        public int getItemCount() {
            return chatModelss.size();
        }

        class chatviewholder extends RecyclerView.ViewHolder {
            TextView massage,time,lastseen;
            LinearLayout gravity,back_color;

            public chatviewholder(@NonNull View itemView) {
                super(itemView);
                massage = itemView.findViewById(R.id.massage);
                time = itemView.findViewById(R.id.time);
                gravity = itemView.findViewById(R.id.gravity);
                back_color = itemView.findViewById(R.id.backgroundcolor);
                lastseen = itemView.findViewById(R.id.seen);

            }
        }
    }

    void lastseen(){
       listener= databaseReference.child("chat").child(userModel.getUid()).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatModel chatModel=snapshot.getValue(ChatModel.class);
                     if(chatModel.getReceverid().equals(uid)&&chatModel.getSenderid().equals(userModel.getUid())){
                         Toast.makeText(Chat_intent.this, chatModel.getMassageid(), Toast.LENGTH_SHORT).show();
                         HashMap<String,Object> hashMap=new HashMap<>();
                         hashMap.put("isseen",true);
                         snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        databaseReference.child("chat").child(userModel.getUid()).child(uid).removeEventListener(listener);
        super.onPause();
        stuatus("offline");

    }

    void usersinchat(){
        databaseReference.child("my_users_was_chat").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child(userModel.getUid()).exists()){
                        DatabaseReference databaseReference1=snapshot.getRef();
                        databaseReference1.child(userModel.getUid()).removeValue();
                    }
                }

                String key=databaseReference.child("my_users_was_chat").child(uid).push().getKey();
                databaseReference.child("my_users_was_chat").child(uid).child(key).child(userModel.getUid()).setValue(userModel);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference.child("friends").child(userModel.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(dataSnapshot.child(uid).exists()){
                        Toast.makeText(Chat_intent.this, "yyy", Toast.LENGTH_SHORT).show();
                        databaseReference.child("my_users_was_chat").child(userModel.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                    if(snapshot.child(uid).exists()){
                                        DatabaseReference databaseReference1=snapshot.getRef();
                                        databaseReference1.child(uid).removeValue();
                                    }
                                }
                                String key=databaseReference.child("my_users_was_chat").child(uid).push().getKey();
                                databaseReference.child("my_users_was_chat").child(userModel.getUid()).child(key).child(uid).setValue(mymodel);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    void stuatus(String status){
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        stuatus("online");
    }

}
