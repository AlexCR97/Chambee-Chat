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

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civImagenPerfilUsuario;
        TextView tvNombreUsuario;
        TextView tvUltimoMensaje;
        TextView tvFecha;
        TextView tvCantidadSinLeer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civImagenPerfilUsuario = itemView.findViewById(R.id.civImagenPerfilUsuario);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvUltimoMensaje = itemView.findViewById(R.id.tvUltimoMensaje);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCantidadSinLeer = itemView.findViewById(R.id.tvCantidadSinLeer);
        }
    }

    private Context context;
    private List<Object> items;

    public ChatsAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
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
