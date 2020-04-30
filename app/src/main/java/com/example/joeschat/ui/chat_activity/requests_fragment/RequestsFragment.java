package com.example.joeschat.ui.chat_activity.requests_fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joeschat.R;
import com.example.joeschat.models.UserModel;
import com.example.joeschat.ui.chat_activity.add_friend.Add_friends;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestsFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String myid;
    List<UserModel> userModelss;
    HeAdapter adapter;
    String he_id;
    UserModel mymodel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_requests, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intialview();
        intialfirebase();
        requests();
    }
    private void requests() {
        userModelss=new ArrayList<>();
        myid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child("requests").child(myid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModelss.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    UserModel userModel = snapshot.getValue(UserModel.class);
                    assert userModel != null;
                    assert myid != null;
                    userModelss.add(userModel);

                }
                adapter = new HeAdapter(userModelss);
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void intialfirebase() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();
    }


    private void intialview() {
        recyclerView=view.findViewById(R.id.recyclerview3);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity().getApplicationContext(),RecyclerView.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(),DividerItemDecoration.VERTICAL));
    }

    class HeAdapter extends RecyclerView.Adapter<HeAdapter.friendviewholde>{
        List<UserModel> userModels;

        public HeAdapter(List<UserModel> userModels) {
            this.userModels = userModels;
        }

        @NonNull
        @Override
        public HeAdapter.friendviewholde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.requests_item,parent,false);
            return new HeAdapter.friendviewholde(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final HeAdapter.friendviewholde holder, int position) {
            final UserModel userModel=userModels.get(position);
            if(userModel.getImgeUrl()!=null){
                Picasso.get().load(userModel.getImgeUrl()).into(holder.circleImageView);
            }
            else {
                holder.circleImageView.setImageResource(R.drawable.ic_launcher_foreground);
            }

            holder.req_name.setText(userModel.getUserName());
            holder.req_age.setText(userModel.getAgee());
            holder.req_addrese.setText(userModel.getAddarese());
            holder.accept_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //add he to me
                    databaseReference.child("friends").child(myid).child(userModel.getUid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "you and "+userModel.getUserName()+" now are friend", Toast.LENGTH_LONG).show();
                            DatabaseReference databaseReference2=firebaseDatabase.getReference();
                            databaseReference2.child("requests").child(userModel.getUid()).child(myid).removeValue();

                            //get my model
                            databaseReference.child("users").child(myid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mymodel=dataSnapshot.getValue(UserModel.class);
                                    //add me to he
                                    databaseReference.child("friends").child(userModel.getUid()).child(myid).setValue(mymodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                }
            });


        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        class friendviewholde extends RecyclerView.ViewHolder{
            CircleImageView circleImageView;
            TextView req_name,req_age,req_addrese;
            Button accept_friend,CancelRequest;
            public friendviewholde(@NonNull View itemView) {
                super(itemView);
                circleImageView=itemView.findViewById(R.id.req_profile);
                req_name=itemView.findViewById(R.id.req_name);
                req_age=itemView.findViewById(R.id.req_age);
                req_addrese=itemView.findViewById(R.id.req_addrese);
                accept_friend=itemView.findViewById(R.id.accept_request);
                CancelRequest=itemView.findViewById(R.id.cancel_request2);

            }
        }
    }
}
