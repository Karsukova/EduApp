package space.karsukova.educateapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindGroupViewHolder extends RecyclerView.ViewHolder {

    CircleImageView groupImage;
    TextView groupName, description;
    public FindGroupViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
