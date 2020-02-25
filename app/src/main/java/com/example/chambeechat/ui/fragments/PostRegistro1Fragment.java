package com.example.chambeechat.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.chambeechat.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class PostRegistro1Fragment extends Fragment {

    public static final int REQUEST_CODE_CAMERA = 0;
    public static final int REQUEST_CODE_GALLERY = 1;

    private CircleImageView civImagenPerfil;
    private EditText etNombreUsuario;

    private Uri dataImagePerfil;

    public PostRegistro1Fragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_registro1, container, false);

        civImagenPerfil = view.findViewById(R.id.civImagenPerfil);
        etNombreUsuario = view.findViewById(R.id.etNombreUsuario);

        civImagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        return view;
    }

    public Uri getDataImagenPerfil() {
        return dataImagePerfil;
    }

    public String getDataNombreUsuario() {
        return etNombreUsuario.getText().toString();
    }

    private void selectImage() {

        // from camera
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);*/

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
                    dataImagePerfil = data.getData();

                    Glide.with(PostRegistro1Fragment.this.getContext())
                    .load(dataImagePerfil)
                    .into(civImagenPerfil);
                }
                break;
            }
        }
    }
}
