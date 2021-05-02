package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice, addGalleryImage, addPdf, profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        uploadNotice = findViewById(R.id.addnotice);
        addGalleryImage = findViewById(R.id.addGalleryImage);
        addPdf = findViewById(R.id.addDoc);
        profile = findViewById(R.id.profile);
        uploadNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addPdf.setOnClickListener(this);
        profile.setOnClickListener(this);

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
                 intent = new Intent(AdminActivity.this, UploadNotice.class);
                startActivity(intent);
                break;
            case R.id.addGalleryImage:
                 intent = new Intent(AdminActivity.this, UploadImage.class);
                startActivity(intent);
                break;
            case R.id.addDoc:
                intent = new Intent(AdminActivity.this, UploadDocument.class);
                startActivity(intent);
                break;
            case R.id.profile:
                intent = new Intent(AdminActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

        }
    }
}