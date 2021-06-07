package space.karsukova.educateapp.ui.gallery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import space.karsukova.educateapp.GalleryViewHolder;
import space.karsukova.educateapp.R;
import space.karsukova.educateapp.adapters.GalleryAdapter;
import space.karsukova.educateapp.fragments.MyViewHolder;
import space.karsukova.educateapp.utils.Gallery;
import space.karsukova.educateapp.utils.Post;
import space.karsukova.educateapp.utils.User;

public class GalleryFragment extends Fragment {

    //RecyclerView convoRecycler, other
    DatabaseReference galleryRef;
    RecyclerView estafRV, lyznaRV, osenRV;
    GalleryAdapter adapter;
    TextView album;
    private ArrayList<Gallery> galleryArrayList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryRef = FirebaseDatabase.getInstance().getReference().child("gallery");
        estafRV = view.findViewById(R.id.estaf_recycler);
        lyznaRV = view.findViewById(R.id.lyzna_recycler);
        osenRV = view.findViewById(R.id.fantazii_recycler);
        getEstafImage();
        getLyznaImage();
        getOsenImage();
        return view;
    }

    private void getOsenImage() {
        galleryRef.child("Конкурс 'Осенние фантазии'").addValueEventListener(new ValueEventListener() {
            List<String> imagelist = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String data = (String) ds.getValue();
                    imagelist.add(data);
                }
                adapter = new GalleryAdapter(getContext(), imagelist);
                osenRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
                osenRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getLyznaImage() {
        galleryRef.child("Лыжня России 2021").addValueEventListener(new ValueEventListener() {
            List<String> imagelist = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String data = (String) ds.getValue();
                    imagelist.add(data);
                }
                adapter = new GalleryAdapter(getContext(), imagelist);
                lyznaRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
                lyznaRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getEstafImage() {
        galleryRef.child("Эстафета").addValueEventListener(new ValueEventListener() {
            List<String> imagelist = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String data = (String) ds.getValue();
                    imagelist.add(data);
                }
                adapter = new GalleryAdapter(getContext(), imagelist);
                estafRV.setLayoutManager(new GridLayoutManager(getContext(), 3));
                estafRV.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
    }


}