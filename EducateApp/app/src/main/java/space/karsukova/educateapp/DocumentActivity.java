package space.karsukova.educateapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import space.karsukova.educateapp.adapters.DocumentAdapter;
import space.karsukova.educateapp.utils.Document;

public class DocumentActivity extends AppCompatActivity {

    private RecyclerView documentRecycler;
    private DatabaseReference reference;
    private List<Document> list;
    private DocumentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        documentRecycler = findViewById(R.id.documentReccycler);
        reference = FirebaseDatabase.getInstance().getReference().child("doc");


        getData();
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot ds: snapshot.getChildren()){
                    Document document = ds.getValue(Document.class);
                    list.add(document);
                }
                adapter = new DocumentAdapter(DocumentActivity.this, list);
                documentRecycler.setLayoutManager(new LinearLayoutManager(DocumentActivity.this));
                documentRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DocumentActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}