package space.karsukova.educateapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    CardView uploadNotice, addGalleryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        uploadNotice = findViewById(R.id.addnotice);
        addGalleryImage = findViewById(R.id.addGalleryImage);
        uploadNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
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

        }
    }
}