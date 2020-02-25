package com.example.chambeechat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chambeechat.R;
import com.example.chambeechat.data.Datos;
import com.example.chambeechat.models.Mensaje;
import com.example.chambeechat.models.Usuario;
import com.example.chambeechat.services.FirebaseStorageService;
import com.example.chambeechat.services.FirestoreService;
import com.example.chambeechat.ui.adapters.ExplorarAdapter;
import com.example.chambeechat.ui.adapters.MensajesAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajesActivity extends AppCompatActivity {

    private ImageView ivRegresar;
    private CircleImageView civImagenPerfilUsuario;
    private TextView tvDisplayNameReceiver;
    private RecyclerView rvMensajes;
    private EditText etMensaje;
    private ImageView ivEnviarMensaje;

    private Usuario usuarioReceiver;
    private Map<String, Uri> profileImages = new HashMap<>();

    private FirebaseStorageService storageService = new FirebaseStorageService();
    private FirestoreService firestoreService = new FirestoreService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        ivRegresar = findViewById(R.id.ivRegresar);
        civImagenPerfilUsuario = findViewById(R.id.civImagenPerfilUsuario);
        tvDisplayNameReceiver = findViewById(R.id.tvDisplayNameReceiver);
        rvMensajes = findViewById(R.id.rvMensajes);
        etMensaje = findViewById(R.id.etMensaje);
        ivEnviarMensaje = findViewById(R.id.ivEnviarMensaje);

        ivRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMensaje.getText().toString();

                if (message.isEmpty()) {
                    return;
                }

                String sender = Datos.getUsuario().getUid();
                String receiver = usuarioReceiver.getUid();

                sendMessage(sender, receiver, message);
            }
        });

        // get sender and receiver
        getSenderAndReceiver();
    }

    private void getSenderAndReceiver() {
        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            Toast.makeText(MensajesActivity.this, "Error al obtener receiver", Toast.LENGTH_LONG).show();
            return;
        }

        String receiver = bundle.getString("receiver");

        firestoreService.getUsuario(receiver, new FirestoreService.OnDataParsedListener<Usuario>() {
            @Override
            public void onParsed(Usuario parsedData) {
                usuarioReceiver = parsedData;
                fillData();
                loadReceiverProfileImage();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MensajesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fillData() {
        String senderUid = Datos.getUsuario().getUid();
        String receiverUid = usuarioReceiver.getUid();

        tvDisplayNameReceiver.setText(usuarioReceiver.getDisplayName());

        firestoreService.getMessagesFromChat(senderUid, receiverUid, new FirestoreService.OnDataParsedListener<List<Mensaje>>() {
            @Override
            public void onParsed(List<Mensaje> parsedData) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MensajesActivity.this);
                linearLayoutManager.setStackFromEnd(true);

                MensajesAdapter adapter = new MensajesAdapter(MensajesActivity.this, parsedData);
                rvMensajes.setLayoutManager(linearLayoutManager);
                rvMensajes.setAdapter(adapter);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MensajesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadReceiverProfileImage() {
        if (usuarioReceiver == null)
            return;

        final String imgPath = "img/" + usuarioReceiver.getUid();

        // image is already downloaded
        if (profileImages.containsKey(imgPath)) {
            Glide.with(MensajesActivity.this)
                    .load(profileImages.get(imgPath))
                    .into(civImagenPerfilUsuario);
        }
        // download image
        else {
            storageService.downloadImage(imgPath, new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    profileImages.put(imgPath, uri);

                    Glide.with(MensajesActivity.this)
                            .load(profileImages.get(imgPath))
                            .into(civImagenPerfilUsuario);
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("chambee", e.getMessage());
                    Toast.makeText(MensajesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void sendMessage(String sender, String receiver, String message) {
        etMensaje.setText("");

        Log.e("chambee", "Sending message");

        firestoreService.sendMessage(sender, receiver, message, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("chambee", "Message sent");

                fillData();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MensajesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
