package com.example.joeschat.ui.chat_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joeschat.R;
import com.example.joeschat.models.UserModel;
import com.example.joeschat.ui.chat_activity.add_friend.Add_friends;
import com.example.joeschat.ui.chat_activity.chat_fragment.ChatFragment;
import com.example.joeschat.ui.chat_activity.freinds.FreindsFragment;
import com.example.joeschat.ui.chat_activity.requests_fragment.RequestsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
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

public class ChatActivity extends AppCompatActivity {
    CircleImageView imageView;
    TextView user_name;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FloatingActionButton floatingActionButton=findViewById(R.id.folatingactionbutton);
        floatingActionButton.setVisibility(View.VISIBLE);

        intialviews();
    }

    private void intialviews() {
       SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("login",1);
        editor.commit();

        imageView=findViewById(R.id.profile_image2);
        user_name=findViewById(R.id.username2);
        TabLayout tabLayout=findViewById(R.id.table_layout);
        ViewPager viewPager=findViewById(R.id.viewpager);

        final FloatingActionButton floatingActionButton=findViewById(R.id.folatingactionbutton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivity.this, Add_friends.class);
                floatingActionButton.setVisibility(View.GONE);
                startActivity(intent);
            }
        });
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel userModel=dataSnapshot.getValue(UserModel.class);
                user_name.setText(userModel.getUserName());
                if(userModel.getImgeUrl().isEmpty()){
                    imageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
                }
                else {
                    Picasso.get().load(userModel.getImgeUrl()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Viewpagadapter viewpagadapter=new Viewpagadapter(getSupportFragmentManager());
        viewpagadapter.addfragments(new ChatFragment(),"Chats");
        viewpagadapter.addfragments(new FreindsFragment(),"Friends");
        viewpagadapter.addfragments(new RequestsFragment(),"Requests");

        viewPager.setAdapter(viewpagadapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class Viewpagadapter extends FragmentPagerAdapter{
        List<Fragment> fragments;
        List<String> title;

        public Viewpagadapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.title = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addfragments(Fragment fragment,String titlee){
            fragments.add(fragment);
            title.add(titlee);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        stuatus("offline");
    }
}
