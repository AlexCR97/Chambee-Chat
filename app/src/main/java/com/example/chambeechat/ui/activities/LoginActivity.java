package com.example.chambeechat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chambeechat.R;
import com.example.chambeechat.data.Datos;
import com.example.chambeechat.models.Usuario;
import com.example.chambeechat.services.FirebaseAuthService;
import com.example.chambeechat.services.FirestoreService;
import com.example.chambeechat.validators.EmailValidator;
import com.example.chambeechat.validators.NonEmptyStringValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etContrasena;
    private Button bIniciarSesion;
    private TextView tvOlvideContrasena;
    private ImageView ivGoogle;
    private TextView tvRegistrate;
    private ProgressDialog progressDialog;

    private final FirebaseAuthService authService = new FirebaseAuthService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        bIniciarSesion = findViewById(R.id.bIniciarSesion);
        tvOlvideContrasena = findViewById(R.id.tvOlvideContrasena);
        ivGoogle = findViewById(R.id.ivGoogle);
        tvRegistrate = findViewById(R.id.tvRegistrate);

        bIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etCorreo.getText().toString();
                String password = etContrasena.getText().toString();
                //String email = "karla@gmail.com";
                //String password = "123456";

                if (validateCredentials(email, password)) {
                    signIn(email, password);
                }
            }
        });

        tvRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean validateCredentials(String email, String password) {
        // validate email
        EmailValidator emailValidator = new EmailValidator();

        if (!emailValidator.validate(email)) {
            Toast.makeText(this, "El correo no es valido", Toast.LENGTH_LONG).show();
            return false;
        }

        // validate passsword
        NonEmptyStringValidator nonEmptyStringValidator = new NonEmptyStringValidator();

        if (!nonEmptyStringValidator.validate(password)) {
            Toast.makeText(this, "Ingresa tu contraseña", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void signIn(String email, String password) {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Iniciando sesion...");
        progressDialog.show();

        authService.signInWithEmail(email, password, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                saveUserCacheDataAndGoToHomePage();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveUserCacheDataAndGoToHomePage() {
        FirebaseAuthService authService = new FirebaseAuthService();
        FirestoreService firestoreService = new FirestoreService();

        FirebaseUser firebaseUser = authService.getCurrentUser();
        firestoreService.getUsuario(firebaseUser.getUid(), new FirestoreService.OnDataParsedListener<Usuario>() {
            @Override
            public void onParsed(Usuario parsedData) {
                progressDialog.dismiss();

                Datos.setUsuario(parsedData);

                Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
                startActivity(intent);
                finish();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
