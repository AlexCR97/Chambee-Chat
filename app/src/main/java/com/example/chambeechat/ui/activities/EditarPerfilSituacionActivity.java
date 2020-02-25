package com.example.chambeechat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chambeechat.R;
import com.example.chambeechat.data.Datos;
import com.example.chambeechat.data.Situacion;
import com.example.chambeechat.models.Usuario;
import com.example.chambeechat.services.FirestoreService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class EditarPerfilSituacionActivity extends AppCompatActivity {

    private ImageView ivRegresar;
    private Button bConfirmar;
    private Spinner sSituacion;

    private FirestoreService firestoreService = new FirestoreService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_situacion);

        ivRegresar = findViewById(R.id.ivRegresar);
        bConfirmar = findViewById(R.id.bConfirmar);
        sSituacion = findViewById(R.id.sSituacion);

        ivRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUsuario();
            }
        });

        Situacion[] situaciones = Situacion.values();
        List<String> situacionesList = new ArrayList<>();

        for (Situacion situacion : situaciones) {
            situacionesList.add(situacion.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, situacionesList);

        sSituacion.setAdapter(adapter);
    }

    public void updateUsuario() {
        String selectedSituacion = sSituacion.getSelectedItem().toString();

        Usuario usuario = Datos.getUsuario();
        usuario.setSituacion(selectedSituacion);

        firestoreService.updateUsuario(usuario, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarPerfilSituacionActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
