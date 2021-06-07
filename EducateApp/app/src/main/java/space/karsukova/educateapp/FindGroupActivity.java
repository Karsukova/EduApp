package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

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
        FloatingActionButton fab_btn = findViewById(R.id.fab_btn);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups");
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();



        LoadGroups("");
        fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateGroup.class));
                finish();
            }
        });


    }

    private void LoadGroups(final String s) {

        groupsArrayList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        final Query query = reference.orderByChild("Groups").startAt(s).endAt(s+"\uf8ff");
                reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupsArrayList.size();
                for (DataSnapshot ds: snapshot.getChildren()){

                    if(ds.child("Participants").child(firebaseAuth.getUid()).exists()){

                        Groups groups = ds.getValue(Groups.class);
                        if (groups.getGropTitle().toLowerCase().contains(s.toLowerCase()) ||
                        groups.getGroupDescription().toLowerCase().contains(s.toLowerCase())){
                            groupsArrayList.add(groups);
                        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadGroups(newText);
                return false;
            }
        });
        return true;
    }
}