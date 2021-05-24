package space.karsukova.educateapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import space.karsukova.educateapp.R;
import space.karsukova.educateapp.utils.Document;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DoumentViewHolder> {


    private Context context;
    private List<Document> list;

    public DocumentAdapter(Context context, List<Document> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DoumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.document_item, parent, false);

        return new DoumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoumentViewHolder holder, final int position) {

        holder.documentName.setText(list.get(position).getDocTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, list.get(position).getDocTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.documentDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(list.get(position).getDocUrl()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DoumentViewHolder extends RecyclerView.ViewHolder {

        private TextView documentName;
        private ImageView documentDownload;

        public DoumentViewHolder(@NonNull View itemView) {
            super(itemView);
            documentName = itemView.findViewById(R.id.document_name);
            documentDownload = itemView.findViewById(R.id.document_download);
        }
    }
}
