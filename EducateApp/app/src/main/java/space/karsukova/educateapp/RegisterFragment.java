package space.karsukova.educateapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import space.karsukova.educateapp.utils.User;


public class RegisterFragment extends Fragment {

    public static final String TAG = "TAG";
    private User user;
    private String number;
    private DatabaseReference mDatabase;
    private FirebaseDatabase database;
    private static final String USERS = "users";
    EditText registerFullName, registerEmail, registerPassword, registerConfPass, phoneCC, phoneNumber;
    Button registerUserBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CheckBox isTeacherBox, isChildBox;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        registerFullName = v.findViewById(R.id.registerFullName);
        registerEmail = v.findViewById(R.id.registerEmail);
        registerPassword = v.findViewById(R.id.registerPassword);
        registerConfPass = v.findViewById(R.id.confPassword);
        phoneCC = v.findViewById(R.id.registerCC);
        phoneNumber = v.findViewById(R.id.registerPhoneNumber);
        registerUserBtn = v.findViewById(R.id.regButton);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference(USERS);
        isTeacherBox = v.findViewById(R.id.check_teacher);
        isChildBox = v.findViewById(R.id.check_child);

        isChildBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    isTeacherBox.setChecked(false);
                }
            }
        });
        isTeacherBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    isChildBox.setChecked(false);
                }
            }
        });


        registerUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerFullName.getText().toString().isEmpty()) {
                    registerFullName.setError(getText(R.string.name_miss));
                    return;
                }
                if (registerEmail.getText().toString().isEmpty()) {
                    registerEmail.setError(getText(R.string.email_miss));
                    return;
                }
                if (registerPassword.getText().toString().isEmpty()) {
                    registerPassword.setError(getText(R.string.pswd_miss));
                    return;
                }
                if (registerConfPass.getText().toString().isEmpty()) {
                    registerConfPass.setError(getText(R.string.conf_pswd_miss));
                    return;
                }
                if (phoneNumber.getText().toString().isEmpty()) {
                    phoneNumber.setError(getText(R.string.phone_miss));
                    return;
                }
                if(!(isTeacherBox.isChecked() || isChildBox.isChecked())){
                    Toast.makeText(getActivity(), R.string.select_type, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!registerPassword.getText().toString().equals(registerConfPass.getText().toString())) {
                    registerConfPass.setError(getText(R.string.pswd_dont_match));
                    return;
                }
                //user = new User(registerFullName.getText().toString(), registerEmail.getText().toString(),
                       // phoneNumber.getText().toString());
                fAuth.createUserWithEmailAndPassword(registerEmail.getText().toString(), registerPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser user = fAuth.getCurrentUser();
                                Intent phone = new Intent(getActivity(), VerifyPhone.class);
                                number = phoneCC.getText().toString() + phoneNumber.getText().toString();
                                phone.putExtra("number", number);
                                //updateUI(user);
                                //Toast.makeText(getActivity(), R.string.register_success, Toast.LENGTH_SHORT).show();
                                DocumentReference df = fStore.collection("Users").document(user.getUid());
                                Map<String,Object> userInfo = new HashMap<>();
                                userInfo.put("FullName", registerFullName.getText().toString());
                                userInfo.put("UserEmail", registerEmail.getText().toString());
                                userInfo.put("PhoneNumber", phoneCC.getText().toString() + phoneNumber.getText().toString());
                                if(isTeacherBox.isChecked()){
                                    userInfo.put("isAdmin", "1");
                                    phone.putExtra("isAdmin", "1");

                                }
                                if(isChildBox.isChecked()){
                                    userInfo.put("isUser", "1");
                                    phone.putExtra("isUser", "1");
                                }
                                df.set(userInfo);



                                startActivity(phone);
                                //Log.d(TAG, "onSuccess: " + "+" + phoneCC.getText().toString() +
                                        //phoneNumber.getText().toString());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            public void updateUI(FirebaseUser currentUser) {
                String keyid = mDatabase.push().getKey();
                mDatabase.child(keyid).setValue(user); //adding user info to database
            }

        });


        return v;
    }

}