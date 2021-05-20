package space.karsukova.educateapp.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import space.karsukova.educateapp.R;

public class search_Holder extends RecyclerView.ViewHolder {
    View view;

    public search_Holder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
    }
    public void setName(String name){
        TextView userName = view.findViewById(R.id.groupTitle);
        userName.setText(name);
    }
}
