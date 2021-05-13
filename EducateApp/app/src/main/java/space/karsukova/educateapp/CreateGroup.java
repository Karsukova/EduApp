package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class CreateGroup extends AppCompatActivity {

    private DatabaseReference reference;
    private StorageReference storageReference;
    FirebaseFirestore fStore;
    private Bitmap bitmap;
    ProgressDialog pd;
    String downloadUrl = "";

    private Button createGroup;
    private FirebaseAuth firebaseAuth;
    private ImageView groupImageView;
    private final int REQ = 1;
    private TextView groupName, description;
    private Uri uri = null;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        groupName = findViewById(R.id.groupTitle);
        description = findViewById(R.id.groupDescription);
        createGroup = findViewById(R.id.createGroup);
        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        groupImageView = findViewById(R.id.groupImageView);
        storageReference = FirebaseStorage.getInstance().getReference().child("Groups");
        pd = new ProgressDialog(this);
        final String g_timestamp = "" + System.currentTimeMillis();

        groupImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (uri == null) {
                    createGroup("" + g_timestamp,
                            "" + groupName,
                            "" + description,
                            "");
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] finalimg = baos.toByteArray();
                    final StorageReference filePath;
                    filePath = storageReference.child("Groups").child(finalimg + "jpg");
                    final UploadTask uploadTask = filePath.putBytes(finalimg);
                    uploadTask.addOnCompleteListener(CreateGroup.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                downloadUrl = String.valueOf(uri);
                                                createGroup("" + g_timestamp,
                                                        groupName.getText().toString(),
                                                         description.getText().toString(),
                                                        downloadUrl);
                                            }
                                        });
                                    }
                                });
                            } else {
                                pd.dismiss();
                                Toast.makeText(CreateGroup.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });


    }



    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }


    private void createGroup(final String g_timestamp, String groupTitle, String groupDescription, String groupIcon){
        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("groupId", "" + g_timestamp);
        hashMap.put("gropTitle", "" + groupTitle);
        hashMap.put("groupDescription", "" + groupDescription);
        hashMap.put("groupIcon", "" + groupIcon);
        hashMap.put("timeStamp", "" + g_timestamp);
        hashMap.put("createdBy", "" + firebaseAuth.getUid());


        reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(g_timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                HashMap<String,String> hashMap1 = new HashMap<>();
                hashMap1.put("uid", firebaseAuth.getUid());
                hashMap1.put("role", "creator");
                hashMap1.put("timestamp", g_timestamp);
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Groups");
                ref1.child(g_timestamp).child("Participants").child(firebaseAuth.getUid()).setValue(hashMap1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CreateGroup.this, "Group created", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
                Toast.makeText(CreateGroup.this, R.string.image_uploaded, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), FindGroupActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ &&resultCode == RESULT_OK){
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e){
                e.printStackTrace();
            }
            groupImageView.setImageBitmap(bitmap);
        }
    }


}