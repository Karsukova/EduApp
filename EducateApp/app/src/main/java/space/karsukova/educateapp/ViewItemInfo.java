package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import space.karsukova.educateapp.utils.Groups;

public class ViewItemInfo extends AppCompatActivity {

    private String groupId, userId;
    DatabaseReference reference, requestReference, participantReference;
    private ImageView groupIconIv;
    private TextView groupTitleTv;
    private TextView descriptionTv;
    private Button performBtn, declineBtn;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String CurrentState = "nothing_happen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);

        requestReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        reference = FirebaseDatabase.getInstance().getReference().child("Groups");
        participantReference = FirebaseDatabase.getInstance().getReference().child("Groups").child("Paricipants");
        groupIconIv = findViewById(R.id.groupIcon);
        groupTitleTv = findViewById(R.id.groupTitle);
        descriptionTv = findViewById(R.id.groupDescription);
        performBtn = findViewById(R.id.btnPerform);
        declineBtn = findViewById(R.id.btnDecline);

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        userId = intent.getStringExtra("userId");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (userId == null ) {
            loadGroupInfo();
        } else {
            loadUserInfo();
        }

        performBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction(groupId);
            }
        });
        CheckUserExistance(userId);
        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFromPartisipant(userId);
            }
        });
    }

    private void DeleteFromPartisipant(String userId) {
        if (CurrentState.equals("participant")){
            reference = FirebaseDatabase.getInstance().getReference("Groups");
            reference.child(groupId).child("Participants").child(firebaseAuth.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ViewItemInfo.this, R.string.user_deleted, Toast.LENGTH_SHORT).show();
                        CurrentState = "nothing_happen";
                        performBtn.setText(R.string.send_request);
                        declineBtn.setVisibility(View.GONE);
                        performBtn.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        if (CurrentState.equals("he_sent_pending")){
            requestReference.child(groupId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ViewItemInfo.this, R.string.decline_req, Toast.LENGTH_SHORT).show();
                        CurrentState = "he_sent_decline";
                        performBtn.setVisibility(View.GONE);
                        declineBtn.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    private void CheckUserExistance(final String userId) {
        reference.child(groupId).child("Participants").child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CurrentState = "participant";
                    performBtn.setVisibility(View.GONE);
                    declineBtn.setVisibility(View.VISIBLE);
                    declineBtn.setText(R.string.del_from_group);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestReference.child(groupId).child(firebaseAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "I_sent_pending";
                        performBtn.setText(R.string.decline);
                        performBtn.setVisibility(View.VISIBLE);
                        declineBtn.setVisibility(View.GONE);
                    }
                    if (snapshot.child("status").getValue().toString().equals("decline")){
                        CurrentState = "I_sent_decline";
                        performBtn.setText(R.string.decline);
                        performBtn.setVisibility(View.VISIBLE);
                        declineBtn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(userId != null) {
            requestReference.child(groupId).child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.child("status").getValue().toString().equals("pending")) {
                            CurrentState = "he_sent_pending";
                            performBtn.setText(R.string.accept_request);
                            declineBtn.setText(R.string.cancel_user_req);
                            performBtn.setVisibility(View.VISIBLE);
                            declineBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if (CurrentState.equals("nothing_happen")){
            CurrentState = "nothing_happen";
            performBtn.setText(R.string.send_request);
            performBtn.setVisibility(View.VISIBLE);
            declineBtn.setVisibility(View.GONE);
        }
    }




    private void PerformAction(final String groupId) {
        if (CurrentState.equals("nothing_happen")) {
            HashMap hashMap = new HashMap();
            hashMap.put("uid", "" + firebaseUser.getUid());
            hashMap.put("status", "pending");
            requestReference.child(groupId).child(firebaseUser.getUid()).updateChildren(hashMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ViewItemInfo.this, R.string.req_sent, Toast.LENGTH_SHORT).show();
                                declineBtn.setVisibility(View.GONE);
                                CurrentState = "I_sent_pending";
                                performBtn.setText(R.string.decline);
                            } else {
                                Toast.makeText(ViewItemInfo.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        if (CurrentState.equals("I_sent_pending") || CurrentState.equals("I_sent_decline")) {
            requestReference.child(groupId).child(firebaseUser.getUid()).removeValue().addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ViewItemInfo.this, R.string.cancel_request, Toast.LENGTH_SHORT).show();
                                CurrentState = "nothing_happen";
                                performBtn.setText(R.string.send_request);
                                declineBtn.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(ViewItemInfo.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        if (CurrentState.equals("he_sent_pending")) {
            requestReference.child(groupId).child(userId).removeValue().addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                HashMap<String, String> hashMap1 = new HashMap<>();
                                hashMap1.put("uid", userId);
                                hashMap1.put("role", "participant");
                                hashMap1.put("timestamp", groupId);
                                reference = FirebaseDatabase.getInstance().getReference("Groups");
                                reference.child(groupId).child("Participants").child(userId).setValue(hashMap1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ViewItemInfo.this, R.string.user_added, Toast.LENGTH_SHORT).show();
                                                CurrentState = "participant";
                                                performBtn.setVisibility(View.GONE);
                                                declineBtn.setText(R.string.delete_user);
                                                declineBtn.setVisibility(View.VISIBLE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                            }
                        }
                    });
        }
        if (CurrentState.equals("participant")) {

        }
    }

    private void loadUserInfo() {
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.orderByChild("Id").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String fullName = "" + ds.child("FullName").getValue();
                            String userIcon = "" + ds.child("UserIcon").getValue();

                            groupTitleTv.setText(fullName);
                            descriptionTv.setVisibility(View.GONE);
                            try {
                                Picasso.get().load(userIcon).placeholder(R.drawable.ic_group).into(groupIconIv);
                            } catch (Exception e) {
                                groupIconIv.setImageResource(R.drawable.ic_group);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadGroupInfo() {
        reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.orderByChild("groupId").equalTo(groupId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String gropTitle = "" + ds.child("gropTitle").getValue();
                            String groupDescription = "" + ds.child("groupDescription").getValue();
                            String groupIcon = "" + ds.child("groupIcon").getValue();
                            String timestamp = "" + ds.child("timestamp").getValue();
                            String createdBy = "" + ds.child("createdBy").getValue();

                            groupTitleTv.setText(gropTitle);
                            descriptionTv.setText(groupDescription);
                            try {
                                Picasso.get().load(groupIcon).placeholder(R.drawable.ic_group).into(groupIconIv);
                            } catch (Exception e) {
                                groupIconIv.setImageResource(R.drawable.ic_group);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}