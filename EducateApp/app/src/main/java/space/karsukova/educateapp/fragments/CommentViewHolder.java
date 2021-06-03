package space.karsukova.educateapp.fragments;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import space.karsukova.educateapp.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {

    CircleImageView profileImage;
    TextView username, comment;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profileImage_Comment);
        username = itemView.findViewById(R.id.usernameComment);
        comment = itemView.findViewById(R.id.commentsTV);
    }
}
