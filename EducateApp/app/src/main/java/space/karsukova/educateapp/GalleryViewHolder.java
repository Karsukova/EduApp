package space.karsukova.educateapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryViewHolder extends RecyclerView.ViewHolder {

    public TextView album;
    public static RecyclerView recyclerView;
    public GalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        album = itemView.findViewById(R.id.albumName);
        recyclerView = itemView.findViewById(R.id.parent_recycler);
    }
}
