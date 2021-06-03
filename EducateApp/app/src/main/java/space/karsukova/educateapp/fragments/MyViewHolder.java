package space.karsukova.educateapp.fragments;

import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import space.karsukova.educateapp.R;

public class MyViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    ImageView postImage, commentsImage, commentSend;
    TextView username, timeAgo, postDesc, commentCounter;
    EditText inputComments;
    public static RecyclerView recyclerView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profileImagePost);
        postImage = itemView.findViewById(R.id.postImage);
        username = itemView.findViewById(R.id.profileUsernamePost);
        timeAgo = itemView.findViewById(R.id.timeAgo);
        postDesc = itemView.findViewById(R.id.postDesc);
        commentsImage = itemView.findViewById(R.id.imageComment);
        commentCounter = itemView.findViewById(R.id.commentCounter);
        inputComments = itemView.findViewById(R.id.inputComments);
        commentSend = itemView.findViewById(R.id.sendComment);
        recyclerView = itemView.findViewById(R.id.recyclerViewComments);
    }

    public void counComments(String postKey, final String uid, DatabaseReference commentRef) {
        commentRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int totalComments = (int) snapshot.getChildrenCount();
                    commentCounter.setText(totalComments  + "");
                }
                else {
                    commentCounter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
