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

public class ViewGroupInfo extends AppCompatActivity {

    private String groupId;
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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        loadGroupInfo();

        performBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction(groupId);
            }
        });
    }

    private void PerformAction(String groupId) {
        if (CurrentState.equals("nothing_happen")){
            HashMap hashMap = new HashMap();
            hashMap.put("uid", "" + firebaseUser.getUid());
            hashMap.put("status", "pending");
            requestReference.child(groupId).child(firebaseUser.getUid()).updateChildren(hashMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                Toast.makeText(ViewGroupInfo.this, "you sent req", Toast.LENGTH_SHORT).show();
                                declineBtn.setVisibility(View.GONE);
                                CurrentState = "I_sent_pending";
                                performBtn.setText(R.string.decline);
                            } else {
                                Toast.makeText(ViewGroupInfo.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        if (CurrentState.equals("I_sent_pending")||CurrentState.equals("I_sent_decline")){
            requestReference.child(groupId).child(firebaseUser.getUid()).removeValue().addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ViewGroupInfo.this, R.string.cancel_request, Toast.LENGTH_SHORT).show();
                                CurrentState = "nothing_happen";
                                performBtn.setText(R.string.send_request);
                                declineBtn.setVisibility(View.GONE);

                            }
                            else {
                                Toast.makeText(ViewGroupInfo.this, R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    } );
        }
        if (CurrentState.equals("he_sent_pending")){
            requestReference.child(groupId).child(firebaseUser.getUid()).removeValue().addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void loadGroupInfo() {
        reference  = FirebaseDatabase.getInstance().getReference("Groups");
        reference.orderByChild("groupId").equalTo(groupId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String gropTitle = "" + ds.child("gropTitle").getValue();
                            String groupDescription = "" + ds.child("groupDescription").getValue();
                            String groupIcon = "" + ds.child("groupIcon").getValue();
                            String timestamp = "" + ds.child("timestamp").getValue();
                            String createdBy = "" + ds.child("createdBy").getValue();

                            groupTitleTv.setText(gropTitle);
                            descriptionTv.setText(groupDescription);
                            try{
                                Picasso.get().load(groupIcon).placeholder(R.drawable.ic_group).into(groupIconIv);
                            } catch (Exception e){
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