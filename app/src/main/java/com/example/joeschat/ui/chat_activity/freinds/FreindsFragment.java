package com.example.joeschat.ui.chat_activity.freinds;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.example.joeschat.ui.chat_intent.Chat_intent;
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

public class FreindsFragment extends Fragment {

    RecyclerView recyclerView;
    View view;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<UserModel> userModelss;
    String myid;
    friendsadapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view = inflater.inflate(R.layout.fragment_freinds, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intialviews();
        intialfirbse();
    }

    private void intialfirbse() {
        userModelss = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = firebaseDatabase.getReference().child("friends").child(myid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModelss.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    UserModel userModel = snapshot.getValue(UserModel.class);

                    assert userModel != null;
                    assert myid != null;
                 /*   if (!userModel.getUid().equals(myid)) {
                    }*/
                    userModelss.add(userModel);

                }
                adapter = new friendsadapter(userModelss);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void intialviews() {
        recyclerView = view.findViewById(R.id.recyclerviewforfriend);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
    }

    class friendsadapter extends RecyclerView.Adapter<friendsadapter.friendviewholde> {
        List<UserModel> userModels;

        public friendsadapter(List<UserModel> userModels) {
            this.userModels = userModels;
        }

        @NonNull
        @Override
        public friendsadapter.friendviewholde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.friends_item, parent, false);
            return new friendsadapter.friendviewholde(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final friendsadapter.friendviewholde holder, int position) {
            final UserModel userModel = userModels.get(position);

            if (userModel.getImgeUrl() != null) {
                Picasso.get().load(userModel.getImgeUrl()).into(holder.circleImageView);
            } else {
                holder.circleImageView.setImageResource(R.drawable.ic_launcher_foreground);
            }

            holder.friend_name.setText(userModel.getUserName());
            holder.friend_age.setText(userModel.getAgee());
            holder.friend_addrese.setText(userModel.getAddarese());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity().getApplicationContext(), Chat_intent.class);
                    intent.putExtra("model",userModel);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userModels.size();
        }

        class friendviewholde extends RecyclerView.ViewHolder {
            CircleImageView circleImageView;
            TextView friend_name, friend_age, friend_addrese;

            public friendviewholde(@NonNull View itemView) {
                super(itemView);
                circleImageView = itemView.findViewById(R.id.friend_profile);
                friend_name = itemView.findViewById(R.id.friend_name);
                friend_age = itemView.findViewById(R.id.friend_age);
                friend_addrese = itemView.findViewById(R.id.friend_addrese);

            }
        }
    }

}
