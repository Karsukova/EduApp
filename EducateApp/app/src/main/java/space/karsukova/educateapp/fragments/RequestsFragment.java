package space.karsukova.educateapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import space.karsukova.educateapp.FindGroupActivity;
import space.karsukova.educateapp.FindGroupActivityUser;
import space.karsukova.educateapp.R;
import space.karsukova.educateapp.adapters.AdapterGroupList;
import space.karsukova.educateapp.adapters.AdapterUserList;
import space.karsukova.educateapp.holder.search_Holder;
import space.karsukova.educateapp.utils.Groups;
import space.karsukova.educateapp.utils.User;

public class RequestsFragment extends Fragment {

    View view;
    private String groupId;
    private String userId;

    RecyclerView request_list;
    DatabaseReference databaseReference, userReference;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;


    private ArrayList<User> usersArrayList;
    private AdapterUserList adapterUserList;

    RecyclerView recyclerView;


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_requests, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        request_list = view.findViewById(R.id.rv_request_list);
        recyclerView = view.findViewById(R.id.rv_request_list);
        request_list.setHasFixedSize(true);
        request_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        FragmentActivity fragmentActivity = getActivity();
        groupId = getActivity().getIntent().getStringExtra("groupId");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        userReference = FirebaseDatabase.getInstance().getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();


        Request_group_list(groupId);

        return view;
    }

    private void Request_group_list(String s) {

        usersArrayList = new ArrayList<>();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Requests");
        final DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("users");
        reference.child(s).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    usersArrayList.size();
                    userId = (String) ds.child("uid").getValue();
                    refUser.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            usersArrayList.add(user);
                            adapterUserList = new AdapterUserList(getActivity(), usersArrayList);
                            recyclerView.setAdapter(adapterUserList);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}