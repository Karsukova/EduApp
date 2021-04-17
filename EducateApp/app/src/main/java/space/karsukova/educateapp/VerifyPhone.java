package space.karsukova.educateapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {
    public String Number_entered_by_user,code_by_system, isAdmin, isUser;
    public CardView verify;
    public TextView resend;
    public PinView otp;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    PhoneAuthProvider.ForceResendingToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        Intent intent =getIntent();
        Number_entered_by_user=intent.getStringExtra("number");
        isAdmin = intent.getStringExtra("isAdmin");
        isUser = intent.getStringExtra("isUser");

        Toast.makeText(this, Number_entered_by_user, Toast.LENGTH_SHORT).show();
        verify=findViewById(R.id.verifybutton);
        resend=findViewById(R.id.resend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resend_otp(Number_entered_by_user);
            }
        });
        otp=findViewById(R.id.pinview);
        send_code_to_user(Number_entered_by_user);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_code();
            }
        });

    }

    private void resend_otp(String number_entered_by_user) {
        send_code_to_user(number_entered_by_user);
    }

    private void check_code() {
        String user_entered_otp =otp.getText().toString();
        if (user_entered_otp.isEmpty() || user_entered_otp.length()<6){
            Toast.makeText(this, R.string.OTP_fail, Toast.LENGTH_SHORT).show();
            return;
        }
        finish_everything(user_entered_otp);
    }

    private void send_code_to_user(String number_entered_by_user) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number_entered_by_user,
                60,
                TimeUnit.SECONDS,
                this,
                mCallback
        );
    }
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback =new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            code_by_system=s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code !=null){
                finish_everything(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhone.this, R.string.error, Toast.LENGTH_SHORT).show();

        }
    };

    private void finish_everything(String code) {
        otp.setText(code);
        PhoneAuthCredential credential =PhoneAuthProvider.getCredential(code_by_system,code);
        sign_in(credential);
    }

    private void sign_in(PhoneAuthCredential credential) {
        FirebaseAuth auth =FirebaseAuth.getInstance();
        auth.signInWithCredential(credential).addOnCompleteListener(VerifyPhone.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(VerifyPhone.this, R.string.phone_success, Toast.LENGTH_SHORT).show();
                    if(isAdmin!=null){
                        startActivity(new Intent(VerifyPhone.this, AdminActivity.class));
                        return;

                    }
                    if (isUser!= null){
                        startActivity(new Intent(VerifyPhone.this, MainActivity.class));
                        return;
                    }
                }
                else{
                    Toast.makeText(VerifyPhone.this, R.string.error, Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}