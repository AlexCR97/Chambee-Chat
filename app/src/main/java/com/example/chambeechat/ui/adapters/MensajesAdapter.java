package com.example.chambeechat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chambeechat.R;
import com.example.chambeechat.data.Datos;
import com.example.chambeechat.models.Mensaje;

import java.util.List;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMensaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMensaje = itemView.findViewById(R.id.tvMensaje);
        }
    }

    public static final int MENSAJE_IZQ = 0;
    public static final int MENSAJE_DER = 1;

    private Context context;
    private List<Mensaje> items;

    public MensajesAdapter(Context context, List<Mensaje> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MensajesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MENSAJE_IZQ) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_mensaje_izq, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_mensaje_der, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MensajesAdapter.ViewHolder holder, int position) {
        Mensaje mensaje = items.get(position);

        holder.tvMensaje.setText(mensaje.getMessage());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje = items.get(position);

        if (mensaje.getSender().equals(Datos.getUsuario().getUid())) {
            return MENSAJE_DER;
        }
        else {
            return MENSAJE_IZQ;
        }
    }
}
