package com.example.chambeechat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chambeechat.R;
import com.example.chambeechat.data.Datos;
import com.example.chambeechat.models.Usuario;
import com.example.chambeechat.services.FirestoreService;
import com.example.chambeechat.validators.NonEmptyStringValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class EditarPerfilEstadoActivity extends AppCompatActivity {

    private ImageView ivRegresar;
    private Button bConfirmar;
    private EditText etEstado;

    private FirestoreService firestoreService = new FirestoreService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_estado);

        ivRegresar = findViewById(R.id.ivRegresar);
        bConfirmar = findViewById(R.id.bConfirmar);
        etEstado = findViewById(R.id.etEstado);

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
    }

    public void updateUsuario() {
        NonEmptyStringValidator nonEmptyStringValidator = new NonEmptyStringValidator();
        String estado = etEstado.getText().toString();

        if (!nonEmptyStringValidator.validate(estado)) {
            Toast.makeText(EditarPerfilEstadoActivity.this, "No puedes dejar el campo vacio", Toast.LENGTH_LONG).show();
            return;
        }

        Usuario usuario = Datos.getUsuario();
        usuario.setEstado(estado);

        firestoreService.updateUsuario(usuario, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditarPerfilEstadoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
