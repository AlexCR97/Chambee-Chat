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
    private List<Object> items;

    public MensajesAdapter(Context context, List<Object> items) {
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

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return MENSAJE_IZQ;
        } else {
            return MENSAJE_DER;
        }
    }
}
