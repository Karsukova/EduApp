package space.karsukova.educateapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import space.karsukova.educateapp.R;
import space.karsukova.educateapp.utils.Comment;
import space.karsukova.educateapp.utils.Post;

public class ViewGroupMaterialFragment extends Fragment {
    
    private static final int REQ = 101;
    ImageView addPost, sendPost;
    private String groupId;
    FirebaseFirestore fStore;

    EditText inputPostDesc;
    Uri imageUri;
    DatabaseReference userRef, postRef, groupRef, CommentRef;
    ProgressDialog pd;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String profileImageView, username;
    FirebaseRecyclerAdapter<Post, MyViewHolder> adapter;
    FirebaseRecyclerOptions<Post> options;
    RecyclerView recyclerView;
    FirebaseRecyclerOptions<Comment> commmentOption;
    FirebaseRecyclerAdapter<Comment, CommentViewHolder> commentAdapter;


    View view;
    public ViewGroupMaterialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_group_material, container, false);
        addPost = view.findViewById(R.id.addImagePost);
        sendPost = view.findViewById(R.id.sendPost);
        inputPostDesc = view.findViewById(R.id.inputAddPost);
        fStore = FirebaseFirestore.getInstance();

        groupId = getActivity().getIntent().getStringExtra("groupId");
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        CommentRef = FirebaseDatabase.getInstance().getReference().child("Comments");
        postRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupId).child("Posts");
        pd = new ProgressDialog(getActivity());
        recyclerView = view.findViewById(R.id.recyclerViewGallery);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference().child("PostImages");
        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPost();
            }
        });
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQ);
            }
        });
        LoadPost();
        DocumentReference df = fStore.collection("Users").document(firebaseUser.getUid());
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess"+ documentSnapshot.getData());

                if (documentSnapshot.getString("isUser") != null){
                    addPost.setVisibility(View.GONE);
                    sendPost.setVisibility(View.GONE);
                    inputPostDesc.setVisibility(View.GONE);
                }
            }
        });
        
       
        return view;
    }

    private void LoadPost() {
        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(postRef, Post.class).build();
        adapter = new FirebaseRecyclerAdapter<Post, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i, @NonNull Post post) {
                final String postKey = getRef(i).getKey();
                myViewHolder.postDesc.setText(post.getPostDesc());
                myViewHolder.timeAgo.setText(post.getDate());
                myViewHolder.username.setText(post.getUsername());
                Picasso.get().load(post.getPostImageUrl()).into(myViewHolder.postImage);
                Picasso.get().load(post.getUserProfileImageUrl()).into(myViewHolder.profileImage);
                myViewHolder.counComments(postKey, firebaseUser.getUid(), CommentRef);
                myViewHolder.commentSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment = myViewHolder.inputComments.getText().toString();
                        if (comment.isEmpty()){
                            Toast.makeText(getActivity(), R.string.pls_write_smth, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            AddComment(myViewHolder, postKey, CommentRef, firebaseUser.getUid(), comment);
                        }
                    }
                });
                LoadComments(postKey);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_post, parent, false);

                return new MyViewHolder(view);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void LoadComments(String postKey) {
        MyViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commmentOption = new FirebaseRecyclerOptions.Builder<Comment>().setQuery(CommentRef.child(postKey), Comment.class).build();
        commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(commmentOption) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, int i, @NonNull Comment comment) {
                Picasso.get().load(comment.getProfileImageUrl()).into(commentViewHolder.profileImage);
                commentViewHolder.username.setText(comment.getUsername());
                commentViewHolder.comment.setText(comment.getComment());
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_comment, parent, false);
                return new CommentViewHolder(view);
            }
        };
        commentAdapter.startListening();
        MyViewHolder.recyclerView.setAdapter(commentAdapter);
    }

    private void AddComment(final MyViewHolder myViewHolder, String postKey, DatabaseReference commentRef, String uid, String comment) {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        final String strDate = formatter.format(date);HashMap hashMap = new HashMap();
        hashMap.put("date", strDate);
        hashMap.put("username", username);
        hashMap.put("profileImageUrl", profileImageView);
        hashMap.put("comment", comment);
        commentRef.child(postKey).push().updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), R.string.comment_added, Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    myViewHolder.inputComments.setText(null);
                }
                else {
                    Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == Activity.RESULT_OK && data!=null){
            imageUri = data.getData();
            addPost.setImageURI(imageUri);
            
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        userRef.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    profileImageView = snapshot.child("UserIcon").getValue().toString();
                    username = snapshot.child("FullName").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void AddPost() {
        final String postDesc = inputPostDesc.getText().toString();
        if(postDesc.isEmpty() || postDesc.length() < 3){
            inputPostDesc.setError(getText(R.string.pls_write_smth));
        }
        else if (imageUri == null){
            Toast.makeText(getActivity(), R.string.pls_upl_im, Toast.LENGTH_SHORT).show();
        } else {
            pd.setTitle(R.string.uploading);
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
            final String strDate = formatter.format(date);

            storageReference.child(firebaseUser.getUid()+strDate).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()){
                        storageReference.child(firebaseUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                HashMap hashMap = new HashMap();
                                hashMap.put("date", strDate);
                                hashMap.put("postImageUrl", uri.toString());
                                hashMap.put("postDesc", postDesc);
                                hashMap.put("userProfileImageUrl", profileImageView);
                                hashMap.put("username", username);
                                postRef.child(strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), R.string.post_added, Toast.LENGTH_SHORT).show();
                                            addPost.setImageResource(R.drawable.ic_image);
                                            inputPostDesc.setText("");
                                        } else {
                                            Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        pd.dismiss();
                        Toast.makeText(getActivity(), R.string.error, Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    }
}