package space.karsukova.educateapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import space.karsukova.educateapp.AdminActivity;
import space.karsukova.educateapp.SuperAdminActivity;
import space.karsukova.educateapp.LoginActivity;
import space.karsukova.educateapp.MainActivity;
import space.karsukova.educateapp.R;

public class LoginFragment extends Fragment {

    Button loginBtn, forgotPasswordBtn;

    EditText username, password;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_login, container, false);

        username = v.findViewById(R.id.loginEmail);
        password = v.findViewById(R.id.loginPassword);
        loginBtn= v.findViewById(R.id.loginbtn);
        forgotPasswordBtn = v.findViewById(R.id.forgotPasswordBtn);
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        reset_alert = new AlertDialog.Builder(getActivity());

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v = inflater.inflate(R.layout.reset_pop, null);

                final View finalV = v;
                reset_alert.setTitle(R.string.reset_pswd_quest)
                        .setMessage(R.string.reset_link)
                        .setPositiveButton(R.string.reset, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText email = finalV.findViewById(R.id.reset_email_pop);
                                if(email.getText().toString().isEmpty()){
                                    email.setError(getText(R.string.required_field));
                                    return;
                                }
                                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), R.string.reset_email_sent, Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), R.string.exception, Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }).setNegativeButton(R.string.cancel, null)
                        .setView(v)
                        .create().show();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().isEmpty()){
                    username.setError(getText(R.string.email_miss));
                    return;
                }
                if(password.getText().toString().isEmpty()){
                    password.setError(getText(R.string.pswd_miss));
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(username.getText().toString(),
                        password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkUserAccessLevel(authResult.getUser().getUid());
                        //startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), R.string.exception_pswd,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return v;
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess"+ documentSnapshot.getData());
                if(documentSnapshot.getString("isSuperAdmin")!=null){
                    startActivity(new Intent(getActivity(), SuperAdminActivity.class));
                    return;

                }
                if(documentSnapshot.getString("isAdmin")!=null){
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    return;

                }
                if (documentSnapshot.getString("isUser") != null){
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    return;
                }
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("isSuperAdmin")!=null){
                        startActivity(new Intent(getActivity(), SuperAdminActivity.class));
                        return;

                    }
                    if(documentSnapshot.getString("isAdmin")!=null){
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        return;

                    }
                    if (documentSnapshot.getString("isUser") != null){
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        return;
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
            });
        }
    }


}