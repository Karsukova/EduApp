package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import space.karsukova.educateapp.adapters.AdapterGroupList;
import space.karsukova.educateapp.utils.Groups;

public class FindGroupActivity extends AppCompatActivity {

    private ArrayList<Groups> groupsArrayList;
    private AdapterGroupList adapterGroupList;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);
        recyclerView = findViewById(R.id.recyclerViewGroups);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FloatingActionButton fab_btn = findViewById(R.id.fab_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups");
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        LoadGroups();
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateGroup.class));
                finish();
            }
        });


    }

    private void LoadGroups() {
        groupsArrayList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupsArrayList.size();
                for (DataSnapshot ds: snapshot.getChildren()){

                    if(ds.child("Participants").child(firebaseAuth.getUid()).exists()){
                        Groups groups = ds.getValue(Groups.class);
                        groupsArrayList.add(groups);
                    }
                }
                adapterGroupList = new AdapterGroupList(FindGroupActivity.this, groupsArrayList);
                recyclerView.setAdapter(adapterGroupList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}