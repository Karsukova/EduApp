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
import space.karsukova.educateapp.utils.User;

public class AdapterUserList extends RecyclerView.Adapter<AdapterUserList.HolderUserList> {

    private Context context;
    private ArrayList<User> users;
    private String groupId;

    public AdapterUserList(Context context, ArrayList<User> users, String groupId) {
        this.context = context;
        this.users = users;
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public HolderUserList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_view_find_group, parent, false);

        return new HolderUserList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderUserList holder, int position) {
        User usersList = users.get(position);
        final String userId = usersList.getId();
        String FullName = usersList.getFullName();
        String UserIcon = usersList.getUserIcon();
        holder.description.setVisibility(View.GONE);

        try{
            Picasso.get().load(UserIcon).placeholder(R.drawable.ic_group).into(holder.userIcon);
        }catch (Exception e){
            holder.userIcon.setImageResource(R.drawable.ic_group);

        }

        holder.userName.setText(FullName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewRequestItemInfo.class);
                intent.putExtra("userId", userId);
                intent.putExtra("groupId", groupId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class HolderUserList extends RecyclerView.ViewHolder{

        private TextView userName, description;
        private ImageView userIcon;

        public HolderUserList(@NonNull View itemView){
            super(itemView);
            userName = itemView.findViewById(R.id.groupTitle);
            userIcon = itemView.findViewById(R.id.groupImage);
            description = itemView.findViewById(R.id.description);

        }
    }
}
