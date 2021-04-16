package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {
    EditText userPassword, userConfPassword;
    Button savePasswordBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        userPassword = findViewById(R.id.newUserPassword);
        userConfPassword = findViewById(R.id.newConfirmPass);
        user = FirebaseAuth.getInstance().getCurrentUser();
        savePasswordBtn = findViewById(R.id.resetPasswordBtn);
        savePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userPassword.getText().toString().isEmpty()){
                    userPassword.setError(getText(R.string.pswd_miss));
                    return;
                }
                if(userConfPassword.getText().toString().isEmpty()){
                    userConfPassword.setError(getText(R.string.conf_pswd_miss));
                    return;
                }

                if(!userPassword.getText().toString().equals(userConfPassword.getText().toString())){
                    userConfPassword.setError(getText(R.string.pswd_dont_match));
                    return;
                }
                user.updatePassword(userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ResetPassword.this, R.string.pswd_updated, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPassword.this, R.string.exception, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}