package space.karsukova.educateapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import space.karsukova.educateapp.R;
import space.karsukova.educateapp.ViewRequestItemInfo;
import space.karsukova.educateapp.utils.Groups;

public class AdapterGroupListUser extends RecyclerView.Adapter<AdapterGroupListUser.HolderGroupList> {

    private Context context;
    private ArrayList<Groups> groups;
    private String groupId;


    public AdapterGroupListUser(Context context, ArrayList<Groups> groups) {
        this.context = context;
        this.groups = groups;
    }

    @NonNull
    @Override
    public HolderGroupList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_view_find_group, parent, false);

        return new  HolderGroupList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupList holder, int position) {
        Groups groupsList = groups.get(position);
        final String groupId = groupsList.getGroupId();
        String groupTitle = groupsList.getGropTitle();
        String groupDescription = groupsList.getGroupDescription();
        String groupIcon = groupsList.getGroupIcon();

        holder.groupTitle.setText(groupTitle);
        holder.description.setText(groupDescription);
        try{
            Picasso.get().load(groupIcon).placeholder(R.drawable.ic_group).into(holder.groupIcon);


        }catch (Exception e){
            holder.groupIcon.setImageResource(R.drawable.ic_group);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewRequestItemInfo.class);
                intent.putExtra("groupId", groupId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class HolderGroupList extends RecyclerView.ViewHolder{

        private ImageView groupIcon;
        private TextView groupTitle;
        private TextView description;

        public HolderGroupList(@NonNull View itemView){
            super(itemView);
            groupIcon = itemView.findViewById(R.id.groupImage);
            groupTitle = itemView.findViewById(R.id.groupTitle);
            description = itemView.findViewById(R.id.description);
        }
    }
}
