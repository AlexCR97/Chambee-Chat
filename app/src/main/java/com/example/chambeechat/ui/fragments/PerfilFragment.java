package com.example.chambeechat.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chambeechat.R;
import com.example.chambeechat.data.Datos;
import com.example.chambeechat.models.Usuario;
import com.example.chambeechat.services.FirebaseStorageService;
import com.example.chambeechat.ui.activities.EditarPerfilAcercaDeMiActivity;
import com.example.chambeechat.ui.activities.EditarPerfilEstadoActivity;
import com.example.chambeechat.ui.activities.EditarPerfilSituacionActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class PerfilFragment extends Fragment {

    public static final int REQUEST_CODE_CAMERA = 0;
    public static final int REQUEST_CODE_GALLERY = 1;

    private CircleImageView civImagenPerfilUsuario;
    private TextView tvNombreUsuario;
    private TextView tvLocacion;
    private TextView tvSituacion;
    private TextView tvEstado;
    private TextView tvAcercaDeMi;
    private ImageView ivEditarSituacion;
    private ImageView ivEditarEstado;
    private ImageView ivEditarAcercaDeMi;
    private ProgressDialog progressDialog;

    private Usuario usuario = Datos.getUsuario();

    private final FirebaseStorageService storageService = new FirebaseStorageService();

    public PerfilFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        civImagenPerfilUsuario = view.findViewById(R.id.civImagenPerfilUsuario);
        tvNombreUsuario = view.findViewById(R.id.tvNombreUsuario);
        tvLocacion = view.findViewById(R.id.tvLocacion);
        tvSituacion = view.findViewById(R.id.tvSituacion);
        tvEstado = view.findViewById(R.id.tvEstado);
        tvAcercaDeMi = view.findViewById(R.id.tvAcercaDeMi);
        ivEditarAcercaDeMi = view.findViewById(R.id.ivEditarAcercaDeMi);
        ivEditarEstado = view.findViewById(R.id.ivEditarEstado);
        ivEditarSituacion = view.findViewById(R.id.ivEditarSituacion);

        tvNombreUsuario.setText(usuario.getDisplayName());
        tvLocacion.setText(usuario.getLocacionCiudad() + ", " + usuario.getLocacionEstado());
        tvSituacion.setText(usuario.getSituacion());
        tvEstado.setText(usuario.getEstado());
        tvAcercaDeMi.setText(usuario.getAcercaDeMi());

        civImagenPerfilUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ivEditarSituacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditarPerfilSituacionActivity.class);
                startActivity(intent);
            }
        });

        ivEditarEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditarPerfilEstadoActivity.class);
                startActivity(intent);
            }
        });

        ivEditarAcercaDeMi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditarPerfilAcercaDeMiActivity.class);
                startActivity(intent);
            }
        });

        loadProfileImage();
    }

    private void loadProfileImage() {
        Uri profileImage = Datos.getImagenPerfil();

        // image is already loaded
        if (profileImage != null) {
            Glide.with(getContext())
                    .load(profileImage)
                    .into(civImagenPerfilUsuario);
        }
        // download image
        else {
            String imgPath = "img/" + usuario.getUid();

            storageService.downloadImage(imgPath, new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Datos.setImagenPerfil(uri);

                    Glide.with(getContext())
                            .load(Datos.getImagenPerfil())
                            .into(civImagenPerfilUsuario);
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("chambee", e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void selectImage() {
        // from gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
            case REQUEST_CODE_GALLERY: {
                if (resultCode == RESULT_OK) {
                    String path = "img/" + usuario.getUid();
                    Uri imageProfile = data.getData();
                    updateProfileImage(path, imageProfile);
                }
                break;
            }
        }
    }

    private void updateProfileImage(String path, final Uri img) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Subiendo imagen...");
        progressDialog.show();

        storageService.uploadImage(path, img, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();

                Datos.setImagenPerfil(img);
                loadProfileImage();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Log.e("chambee", e.getMessage());
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
