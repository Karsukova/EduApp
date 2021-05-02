package space.karsukova.educateapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


  import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {
        private static final int GALLERY_INTENT_CODE = 1023 ;
        TextView fullName,email,phone,verifyMsg;
        FirebaseAuth fAuth;
        FirebaseFirestore fStore;
        String userId;
        Button resendCode, deleteAccount;
        Button resetPassLocal,changeProfileImage;
        FirebaseUser user;
        ImageView profileImage;
        StorageReference storageReference;
         AlertDialog.Builder reset_alert;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);
            phone = findViewById(R.id.profilePhone);
            fullName = findViewById(R.id.profileName);
            email    = findViewById(R.id.profileEmail);
            resetPassLocal = findViewById(R.id.resetPassword);
            deleteAccount = findViewById(R.id.deleteAccount);
            profileImage = findViewById(R.id.profileImage);
            changeProfileImage = findViewById(R.id.changeProfile);
            reset_alert = new AlertDialog.Builder(this);


            fAuth = FirebaseAuth.getInstance();
            fStore = FirebaseFirestore.getInstance();
            storageReference = FirebaseStorage.getInstance().getReference();

            StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            });

            resendCode = findViewById(R.id.resendCode);
            verifyMsg = findViewById(R.id.verifyMsg);

            userId = fAuth.getCurrentUser().getUid();
            user = fAuth.getCurrentUser();

            DocumentReference documentReference = fStore.collection("Users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(documentSnapshot.exists()){
                        phone.setText(documentSnapshot.getString("PhoneNumber"));
                        fullName.setText(documentSnapshot.getString("FullName"));
                        email.setText(documentSnapshot.getString("UserEmail"));

                    }else {
                        Log.d("tag", "onEvent: Document do not exists");
                    }
                }
            });


            resetPassLocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ResetPassword.class));
                }
            });

            deleteAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reset_alert.setTitle(getText(R.string.del_question))
                            .setMessage(getText(R.string.sure_question))
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseUser user = fAuth.getCurrentUser();
                                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ProfileActivity.this, R.string.account_del, Toast.LENGTH_SHORT).show();
                                            fAuth.signOut();
                                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ProfileActivity.this, R.string.exception, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).setNegativeButton(R.string.cancel, null)
                            .create().show();
                }
            });

            changeProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open gallery
                    Intent i = new Intent(v.getContext(),EditProfileActivity.class);
                    i.putExtra("fullName",fullName.getText().toString());
                    i.putExtra("email",email.getText().toString());
                    i.putExtra("phone",phone.getText().toString());
                    startActivity(i);
                }
            });


        }




        public void logout(View view) {
            FirebaseAuth.getInstance().signOut();//logout
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }



}