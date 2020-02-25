package com.example.chambeechat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chambeechat.R;
import com.example.chambeechat.data.Datos;
import com.example.chambeechat.models.Usuario;
import com.example.chambeechat.services.FirebaseAuthService;
import com.example.chambeechat.services.FirebaseStorageService;
import com.example.chambeechat.services.FirestoreService;
import com.example.chambeechat.ui.fragments.PostRegistro1Fragment;
import com.example.chambeechat.ui.fragments.PostRegistro2Fragment;
import com.example.chambeechat.ui.fragments.PostRegistro3Fragment;
import com.example.chambeechat.validators.NonEmptyStringValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

public class PostRegistroActivity extends AppCompatActivity {

    private Button bAnterior;
    private Button bSiguiente;
    private TextView tvFragmentCount;
    private ProgressDialog progressDialog;
    private Fragment postRegistro1Fragment = new PostRegistro1Fragment();
    private Fragment postRegistro2Fragment = new PostRegistro2Fragment();
    private Fragment postRegistro3Fragment = new PostRegistro3Fragment();

    private int currentFragment = 1;
    private final int fragmentCount = 3;

    private Uri dataImagenPerfil;
    private String dataNombreUsuario;
    private String dataLocacionEstado;
    private String dataLocacionCiudad;
    private String dataSituacion;
    private String dataEstado;
    private String dataAcercaDeMi;

    private FirebaseAuthService authService = new FirebaseAuthService();
    private FirebaseStorageService storageService = new FirebaseStorageService();
    private FirestoreService firestoreService = new FirestoreService();

    private FirebaseUser firebaseUser;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registro);

        bAnterior = findViewById(R.id.bAnterior);
        bSiguiente = findViewById(R.id.bSiguiente);
        tvFragmentCount = findViewById(R.id.tvFragmentCount);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, postRegistro1Fragment)
                .commit();

        bAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(-1);
            }
        });

        bSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(1);
            }
        });

        firebaseUser = authService.getCurrentUser();
        Log.e("chambee", firebaseUser.toString());

        firestoreService.getUsuario(firebaseUser.getUid(), new FirestoreService.OnDataParsedListener<Usuario>() {
            @Override
            public void onParsed(Usuario parsedData) {
                usuario = parsedData;
                Log.e("chambee", usuario.toString());
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("chambee", e.getMessage());
            }
        });
    }

    private void switchFragment(int direction) {
        if (direction == -1) {
            if (currentFragment == 1) {
                return;
            }
        }
        else if (direction == 1) {
            if (!validateFragmentSwitch(currentFragment)) {
                return;
            }

            if (currentFragment == fragmentCount) {
                onNextActivity();
                return;
            }
        }

        currentFragment += direction;
        tvFragmentCount.setText(currentFragment + " / " + fragmentCount);
        Fragment fragment;

        switch (currentFragment) {
            case 1: {
                fragment = postRegistro1Fragment;
                break;
            }

            case 2: {
                fragment = postRegistro2Fragment;
                break;
            }

            case 3: {
                fragment = postRegistro3Fragment;
                break;
            }

            default: {
                fragment = postRegistro1Fragment;
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }

    private boolean validateFragmentSwitch(int fragment) {
        NonEmptyStringValidator nonEmptyStringValidator = new NonEmptyStringValidator();

        switch (fragment) {
            case 1: {
                PostRegistro1Fragment postRegistro1Fragment = (PostRegistro1Fragment) this.postRegistro1Fragment;
                dataImagenPerfil = postRegistro1Fragment.getDataImagenPerfil();
                dataNombreUsuario = postRegistro1Fragment.getDataNombreUsuario();

                if (!nonEmptyStringValidator.validate(dataNombreUsuario)) {
                    Toast.makeText(PostRegistroActivity.this, "Debes ingresar un nombre de usuario", Toast.LENGTH_LONG).show();
                    return false;
                }

                break;
            }

            case 2: {
                PostRegistro2Fragment postRegistro2Fragment = (PostRegistro2Fragment) this.postRegistro2Fragment;
                dataLocacionEstado = postRegistro2Fragment.getDataEstado();
                dataLocacionCiudad = postRegistro2Fragment.getDataCiudad();
                break;
            }

            case 3: {
                PostRegistro3Fragment postRegistro3Fragment = (PostRegistro3Fragment) this.postRegistro3Fragment;
                dataSituacion = postRegistro3Fragment.getDataSituacion();
                dataEstado = postRegistro3Fragment.getDataEstado();
                dataAcercaDeMi = postRegistro3Fragment.getDataAcercaDeMi();
                break;
            }
        }

        return true;
    }

    private void onNextActivity() {
        progressDialog = new ProgressDialog(PostRegistroActivity.this);
        progressDialog.setTitle("Espera un momento...");
        progressDialog.show();

        usuario.setDisplayName(dataNombreUsuario);
        usuario.setLocacionEstado(dataLocacionEstado);
        usuario.setLocacionCiudad(dataLocacionCiudad);
        usuario.setSituacion(dataSituacion);
        usuario.setEstado(dataEstado);
        usuario.setAcercaDeMi(dataAcercaDeMi);

        if (dataImagenPerfil == null) {
            updateUserProfileWithoutImage();
        } else {
            updateUserProfileWithImage();
        }
    }

    private void updateUserProfileWithoutImage() {
        firestoreService.updateUsuario(usuario, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();

                Datos.setUsuario(usuario);

                Intent intent = new Intent(PostRegistroActivity.this, InicioActivity.class);
                startActivity(intent);
                finish();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(PostRegistroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateUserProfileWithImage() {
        String imgPath = "img/" + usuario.getUid();

        firestoreService.updateUsuario(usuario, imgPath, dataImagenPerfil, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();

                Datos.setUsuario(usuario);

                Intent intent = new Intent(PostRegistroActivity.this, InicioActivity.class);
                startActivity(intent);
                finish();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(PostRegistroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}







