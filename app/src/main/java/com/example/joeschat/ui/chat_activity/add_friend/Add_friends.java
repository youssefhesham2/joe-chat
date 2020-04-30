package com.example.joeschat.ui.chat_activity.add_friend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joeschat.R;
import com.example.joeschat.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_friends extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<UserModel> userModelss;
    HeAdapter adapter;
    UserModel mymodel;
     String myid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        intialview();
        intialfirbse();
    }


    private void intialfirbse() {
        userModelss=new ArrayList<>();
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("users");
        myid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModelss.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    UserModel userModel = snapshot.getValue(UserModel.class);

                    assert userModel != null;
                      assert myid != null;
                    if (!userModel.getUid().equals(myid)) {
                        userModelss.add(userModel);
                    }

                    else{
                        mymodel=userModel;

                    }
                }
                adapter = new HeAdapter(userModelss);
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void intialview() {
        recyclerView=findViewById(R.id.recyclerview2);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
         recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
    }

    class HeAdapter extends RecyclerView.Adapter<HeAdapter.friendviewholde>{
        List<UserModel> userModels;

        public HeAdapter(List<UserModel> userModels) {
            this.userModels = userModels;
        }

        @NonNull
        @Override
        public friendviewholde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_fried_item,parent,false);
            return new friendviewholde(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final friendviewholde holder, int position) {
            final UserModel userModel=userModels.get(position);
            Toast.makeText(getApplicationContext(), "aaaaa", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "aaaaa", Toast.LENGTH_LONG).show();

            if(userModel.getImgeUrl()!=null){
                Picasso.get().load(userModel.getImgeUrl()).into(holder.circleImageView);
            }
            else {
                holder.circleImageView.setImageResource(R.drawable.ic_launcher_foreground);
            }

            holder.he_name.setText(userModel.getUserName());
            holder.he_age.setText(userModel.getAgee());
            holder.he_addrese.setText(userModel.getAddarese());

            DatabaseReference databaseReference2=firebaseDatabase.getReference();
            databaseReference2.child("requests").child(userModel.getUid()).child(myid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModel userModel1=dataSnapshot.getValue(UserModel.class);
                    if(userModel1==null){
                        holder.addfriend.setVisibility(View.VISIBLE);
                        holder.CancelRequest.setVisibility(View.GONE);
                        firebaseDatabase.getReference().child("friends").child(myid).child(userModel.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                UserModel userModel2=dataSnapshot.getValue(UserModel.class);
                                if(userModel2==null){
                                    holder.addfriend.setVisibility(View.VISIBLE);
                                    holder.CancelRequest.setVisibility(View.GONE);
                                }
                                else {
                                    holder.addfriend.setVisibility(View.GONE);
                                    holder.CancelRequest.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                    else {
                        holder.addfriend.setVisibility(View.GONE);
                        holder.CancelRequest.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            holder.addfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference2=firebaseDatabase.getReference();
                    databaseReference2.child("requests").child(userModel.getUid()).child(myid).setValue(mymodel);
                    holder.addfriend.setVisibility(View.GONE);
                    holder.CancelRequest.setVisibility(View.VISIBLE);
                }
            });

            holder.CancelRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference databaseReference2=firebaseDatabase.getReference();
                    databaseReference2.child("requests").child(userModel.getUid()).child(myid).removeValue();
                    holder.addfriend.setVisibility(View.VISIBLE);
                    holder.CancelRequest.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        class friendviewholde extends RecyclerView.ViewHolder{
            CircleImageView circleImageView;
            TextView he_name,he_age,he_addrese;
            Button addfriend,CancelRequest;
            public friendviewholde(@NonNull View itemView) {
                super(itemView);
                circleImageView=itemView.findViewById(R.id.he_profile);
                he_name=itemView.findViewById(R.id.he_name);
                he_age=itemView.findViewById(R.id.he_age);
                he_addrese=itemView.findViewById(R.id.he_addrese);
                addfriend=itemView.findViewById(R.id.add_friend);
                CancelRequest=itemView.findViewById(R.id.cancel_request);

            }
        }
    }
}
