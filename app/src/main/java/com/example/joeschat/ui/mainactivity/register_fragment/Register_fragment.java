package com.example.joeschat.ui.mainactivity.register_fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joeschat.R;
import com.example.joeschat.models.UserModel;
import com.example.joeschat.ui.chat_activity.ChatActivity;
import com.example.joeschat.ui.mainactivity.MainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register_fragment extends Fragment {
    View view;
    Context context;
    EditText username,email,password,confirmPassword,age,addrese;
    CircleImageView profilee_img;
    ProgressDialog dialog;
    Button finsh;
    Uri image_uri;
    FirebaseDatabase firebaseDatabase;
    Task<Void> databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_register_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intialviews();
        onslectImage();
        intialfirebase();
        onfinshclick();

    }

    private void onfinshclick() {
        finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void intialfirebase() {
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
    }

    private void onslectImage() {
        profilee_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent , 505 );
            }
        });
    }
    private void intialviews() {
        profilee_img=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.username);
        age=view.findViewById(R.id.age_filed);
        addrese=view.findViewById(R.id.address_filed);
        finsh=view.findViewById(R.id.finsh);

        dialog=new ProgressDialog(context);
        dialog.setMessage("plese wait...");
        dialog.setCancelable(false);
    }
    
    public void register() {
        String usernamee=username.getText().toString();
     /*   String emaill=email.getText().toString();
        String paswword=password.getText().toString();
        String confirmpaswword=confirmPassword.getText().toString();*/
        String agee=age.getText().toString();
        String add=addrese.getText().toString();

        if(image_uri==null){
            Toast.makeText(context, "please select photo", Toast.LENGTH_SHORT).show();
            return;
        }
        if(usernamee.isEmpty()){
            Toast.makeText(context, "please enter username", Toast.LENGTH_SHORT).show();
            return;
        }
       /* if(emaill.isEmpty()){
            Toast.makeText(this, "please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(paswword.isEmpty()){
            Toast.makeText(this, "please password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!confirmpaswword.equals(paswword)){
            Toast.makeText(this, "pawword dosent match", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(agee.isEmpty()){
            Toast.makeText(context, "please enter age", Toast.LENGTH_SHORT).show();
            return;
        }
        if(add.isEmpty()){
            Toast.makeText(context, "please enter addrese", Toast.LENGTH_SHORT).show();
            return;
        }

        savedata(image_uri,usernamee,agee,add);
    }

    private void savedata(Uri image_uri, String usernamee, String agee, String add) {
        dialog.show();
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        uploadphototostorage(image_uri,usernamee,uid,agee,add);
    }

    private void uploadphototostorage(Uri image_uri, final String usernamee, final String userid, final String agee, final String add) {
        storageReference=firebaseStorage.getReference().child("users_image/"+image_uri.getLastPathSegment());
        UploadTask uploadTask=storageReference.putFile(image_uri);
        Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                String image_urL=task.getResult().toString();
                uploadtodatabase(image_urL,usernamee,userid,agee,add);
            }
        });
    }

    void uploadtodatabase(String image_urL,String usernamee,String userid,String agee,String add){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        String phone = preferences.getString("phone", "");
        UserModel userModel=new UserModel(phone,image_urL,usernamee,agee,add,userid,"offline");
        databaseReference=firebaseDatabase.getReference().child("users").child(userid).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    Intent intent=new Intent(getActivity(), ChatActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
                else {
                    dialog.dismiss();
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==505&&resultCode==getActivity().RESULT_OK&&data!=null){
            image_uri=data.getData();
            Picasso.get().load(image_uri).into(profilee_img);
    }
    }
}
