package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TabLayout tabs;
    TabItem loginTabItem, registerTabItem;
    ViewPager pager;
    PagerAdapter adapter;
    TextView verifyMsg;
    Button verifuEmailBtn;
    FirebaseAuth auth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logoutBtn);
        verifyMsg=findViewById(R.id.verifyEmailMsg);
        verifuEmailBtn = findViewById(R.id.verifyEmailBtn);
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();



        if(!auth.getCurrentUser().isEmailVerified()){
            verifuEmailBtn.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
        }
        verifuEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, R.string.verification_email_sent, Toast.LENGTH_SHORT).show();
                        verifuEmailBtn.setVisibility(View.GONE);
                        verifyMsg.setVisibility(View.GONE);
                    }
                });
            }
        });

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.resetUserPassword){
            startActivity(new Intent(getApplicationContext(), ResetPassword.class));
        }

        if(item.getItemId() == R.id.updateEmailMenu){
            final View view = inflater.inflate(R.layout.reset_pop, null);
            reset_alert.setTitle(R.string.update_email)
                    .setMessage(R.string.enter_new_email)
                    .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText email = view.findViewById(R.id.reset_email_pop);
                            if(email.getText().toString().isEmpty()){
                                email.setError(getText(R.string.required_field));
                                return;
                            }
                            FirebaseUser user = auth.getCurrentUser();
                            user.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, R.string.email_updated, Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, R.string.exception, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton(R.string.cancel, null)
                    .setView(view)
                    .create().show();
        }
        if(item.getItemId() == R.id.deletaAccountMenu){
            reset_alert.setTitle(R.string.del_question)
                    .setMessage(R.string.sure_question)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = auth.getCurrentUser();
                            user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, R.string.account_del, Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, R.string.exception, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).setNegativeButton(R.string.cancel, null)
                    .create().show();

        }
        return super.onOptionsItemSelected(item);
    }
}