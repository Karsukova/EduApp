package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import space.karsukova.educateapp.utils.Groups;

public class FindGroupActivity extends AppCompatActivity {

    FirebaseRecyclerOptions<Groups> options;
    FirebaseRecyclerAdapter<Groups,FindGroupViewHolder> adapter;
    Toolbar toolbar;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_group);
        FloatingActionButton fab_btn = findViewById(R.id.fab_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("groups");
        storageReference = FirebaseStorage.getInstance().getReference();

        LoadGroups("");
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateGroup.class));
                finish();
            }
        });

    }

    private void LoadGroups(String s) {
        Query query = databaseReference.orderByChild("groupName").startAt(s).endAt(s+"\uf8ff");
        options = new FirebaseRecyclerOptions.Builder<Groups>().setQuery(query, Groups.class).build();
        adapter = new FirebaseRecyclerAdapter<Groups, FindGroupViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindGroupViewHolder findGroupViewHolder, int i, @NonNull Groups groups) {

            }

            @NonNull
            @Override
            public FindGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_find_group, parent,
                        false);

                return new FindGroupViewHolder(view);
            }
        };
    }

}