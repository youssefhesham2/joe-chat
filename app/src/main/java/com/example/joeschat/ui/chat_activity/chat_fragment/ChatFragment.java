package com.example.joeschat.ui.chat_activity.chat_fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joeschat.R;
import com.example.joeschat.models.ChatModel;
import com.example.joeschat.models.UserModel;
import com.example.joeschat.ui.chat_activity.freinds.FreindsFragment;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatFragment extends Fragment{
    Context context;
    View view;
    RecyclerView recyclerView;
    String uid,lastmassagee;
    List<UserModel> userModels=new ArrayList<>();
    ChaListAdapter chaListAdapter;
    String status=null;
    UserModel userModel1;
   // private AdView mAdView;
//private InterstitialAd interstitialAd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     context=getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*AdView adView = new AdView(getContext());

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        interstitialAd=new InterstitialAd(getContext());
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                getActivity().finish()
            }
        });*/

        intialviews();
        getlistchat();
    }

    private void getlistchat() {
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        // get list chat from firebase
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("my_users_was_chat").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModels.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                   for(DataSnapshot snapshot1:snapshot.getChildren()){
                       UserModel userModel=snapshot1.getValue(UserModel.class);
                       userModels.add(userModel);
                   }
                }
                chaListAdapter=new ChaListAdapter(userModels);
                recyclerView.setAdapter(chaListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void intialviews() {
        recyclerView=view.findViewById(R.id.recyclerview_list_chat);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
    }


    private class ChaListAdapter extends RecyclerView.Adapter<ChaListAdapter.ChatListViewHolder> {
        List<UserModel> userModels;

        public ChaListAdapter(List<UserModel> userModels) {
            this.userModels = userModels;
        }

        @NonNull
        @Override
        public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.chat_list_item, parent, false);
            return new ChatListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ChatListViewHolder holder, int position) {
            final UserModel userModel = userModels.get((userModels.size() - 1) - position);

            if (userModel.getImgeUrl() != null) {
                Picasso.get().load(userModel.getImgeUrl()).into(holder.circleImageView);
            } else {
                holder.circleImageView.setImageResource(R.drawable.ic_launcher_foreground);
            }

            holder.chat_friend_name.setText(userModel.getUserName());
            lastmassage(userModel.getUid(), holder.last_massage);

            FirebaseDatabase.getInstance().getReference().child("users").child(userModel.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userModel1 = dataSnapshot.getValue(UserModel.class);
                    status = userModel1.getStatus();
                        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
                        if (status.equals("online")) {
                            holder.online.setVisibility(View.VISIBLE);
                            holder.offline.setVisibility(View.GONE);
                        } else if (status.equals("offline")) {
                            holder.online.setVisibility(View.GONE);
                            holder.offline.setVisibility(View.VISIBLE);
                        } else {
                            holder.online.setVisibility(View.GONE);
                            holder.offline.setVisibility(View.GONE);
                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
          /*  if (status != null) {

            if (status.equals("online")) {
                holder.online.setVisibility(View.VISIBLE);
                holder.offline.setVisibility(View.GONE);
            } else if (status.equals("offline")) {
                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility(View.VISIBLE);
            } else {
                holder.online.setVisibility(View.GONE);
                holder.offline.setVisibility(View.GONE);
            }
            }*/
            //  holder.last_massage.setText(userModel.get);
        }


        @Override
        public int getItemCount() {
            return userModels.size();
        }

        class ChatListViewHolder extends RecyclerView.ViewHolder{
            CircleImageView circleImageView,online,offline;
            TextView chat_friend_name, last_massage;
            public ChatListViewHolder(@NonNull View itemView) {
                super(itemView);
                circleImageView = itemView.findViewById(R.id.chat_list_imag);
                online = itemView.findViewById(R.id.online);
                offline = itemView.findViewById(R.id.offline);
                chat_friend_name = itemView.findViewById(R.id.chat_friend_name);
                last_massage = itemView.findViewById(R.id.last_massage);

            }
        }
    }

    private void lastmassage(String he_id, final TextView lastmassage){
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("chat").child(uid).child(he_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long size=dataSnapshot.getChildrenCount();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatModel chatModel=snapshot.getValue(ChatModel.class);
                    lastmassagee=chatModel.getMassage();
                }
                if(lastmassagee!=null){
                    lastmassage.setText(lastmassagee);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
   /* public void showInterstitial(){
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        showInterstitial();
    }*/

   /* void stuatus(String status){
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        final HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);

        FirebaseDatabase.getInstance().getReference().child("my_users_was_chat").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    if(snapshot.child(userModel.getUid()).exists()){
                        DatabaseReference databaseReference1=snapshot.getRef();
                        databaseReference1.child(userModel.getUid()).updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(hashMap);
    }

    @Override
    public void onResume() {
        super.onResume();
        stuatus("online");
    }

    @Override
    public void onPause() {
        super.onPause();
        stuatus("offline");
    }*/

}

