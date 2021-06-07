package space.karsukova.educateapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class SuperAdminActivity extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice, addGalleryImage, addPdf, profile, faculty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        uploadNotice = findViewById(R.id.addnotice);
        addGalleryImage = findViewById(R.id.addGalleryImage);
        addPdf = findViewById(R.id.addDoc);
        faculty = findViewById(R.id.faculty);
        profile = findViewById(R.id.profile);
        uploadNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addPdf.setOnClickListener(this);
        profile.setOnClickListener(this);
        faculty.setOnClickListener(this);

        Button logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.addnotice:
                 intent = new Intent(SuperAdminActivity.this, UploadNotice.class);
                startActivity(intent);
                break;
            case R.id.addGalleryImage:
                 intent = new Intent(SuperAdminActivity.this, UploadImage.class);
                startActivity(intent);
                break;
            case R.id.addDoc:
                intent = new Intent(SuperAdminActivity.this, UploadDocument.class);
                startActivity(intent);
                break;
            case R.id.profile:
                intent = new Intent(SuperAdminActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.faculty:
                intent = new Intent(SuperAdminActivity.this, FindGroupActivity.class);
                startActivity(intent);
                break;

        }
    }
}