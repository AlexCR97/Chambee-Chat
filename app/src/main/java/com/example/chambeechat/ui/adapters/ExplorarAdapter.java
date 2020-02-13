package com.example.chambeechat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chambeechat.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExplorarAdapter extends RecyclerView.Adapter<ExplorarAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civImagenPerfilUsuario;
        TextView tvNombreUsuario;
        TextView tvSituacionUsuario;
        TextView tvEstadoUsuario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civImagenPerfilUsuario = itemView.findViewById(R.id.civImagenPerfilUsuario);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvSituacionUsuario = itemView.findViewById(R.id.tvSituacionUsuario);
            tvEstadoUsuario = itemView.findViewById(R.id.tvEstadoUsuario);
        }
    }

    private Context context;
    private List<Object> items;

    public ExplorarAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_explorar, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
