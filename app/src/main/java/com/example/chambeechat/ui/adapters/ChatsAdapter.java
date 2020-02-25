package com.example.chambeechat.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chambeechat.R;
import com.example.chambeechat.models.Usuario;
import com.example.chambeechat.services.FirebaseStorageService;
import com.example.chambeechat.ui.activities.MensajesActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civImagenPerfilUsuario;
        TextView tvUid;
        TextView tvNombreUsuario;
        TextView tvUltimoMensaje;
        TextView tvFecha;
        TextView tvCantidadSinLeer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civImagenPerfilUsuario = itemView.findViewById(R.id.civImagenPerfilUsuario);
            tvUid = itemView.findViewById(R.id.tvUid);
            tvNombreUsuario = itemView.findViewById(R.id.tvNombreUsuario);
            tvUltimoMensaje = itemView.findViewById(R.id.tvUltimoMensaje);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCantidadSinLeer = itemView.findViewById(R.id.tvCantidadSinLeer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String receiver = tvUid.getText().toString();
                    goToChat(receiver);
                }
            });
        }

        private void goToChat(String receiver) {
            Intent intent = new Intent(context, MensajesActivity.class);
            intent.putExtra("receiver", receiver);

            context.startActivity(intent);
        }
    }

    private Context context;
    private List<Usuario> items;
    private Map<String, Uri> profileImages = new HashMap<>();

    private final FirebaseStorageService storageService = new FirebaseStorageService();

    public ChatsAdapter(Context context, List<Usuario> items) {
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Usuario usuario = items.get(position);

        holder.tvUid.setText(usuario.getUid());
        holder.tvNombreUsuario.setText(usuario.getDisplayName());

        // load profile image
        final String imgPath = "img/" + usuario.getUid();

        // image is already downloaded
        if (profileImages.containsKey(imgPath)) {
            Glide.with(context)
                    .load(profileImages.get(imgPath))
                    .into(holder.civImagenPerfilUsuario);
        }
        // download image
        else {
            storageService.downloadImage(imgPath, new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    profileImages.put(imgPath, uri);

                    Glide.with(context)
                            .load(profileImages.get(imgPath))
                            .into(holder.civImagenPerfilUsuario);
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("chambee", e.getMessage());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
