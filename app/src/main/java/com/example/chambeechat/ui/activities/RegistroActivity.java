package com.example.chambeechat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chambeechat.R;
import com.example.chambeechat.services.FirebaseAuthService;
import com.example.chambeechat.validators.EmailValidator;
import com.example.chambeechat.validators.PasswordValidator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class RegistroActivity extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etContrasena;
    private EditText etConfirmarContrasena;
    private Button bRegistrarse;
    private TextView tvIniciarSesion;
    private ProgressDialog progressDialog;

    private final FirebaseAuthService authService = new FirebaseAuthService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        bRegistrarse = findViewById(R.id.bRegistrarse);
        tvIniciarSesion = findViewById(R.id.tvIniciarSesion);

        bRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        tvIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void validateData() {
        String email = etCorreo.getText().toString();
        String password = etContrasena.getText().toString();
        String passwordConfirm = etConfirmarContrasena.getText().toString();

        // validate email
        EmailValidator emailValidator = new EmailValidator();

        if (!emailValidator.validate(email)) {
            Toast.makeText(this, "El correo no es valido", Toast.LENGTH_LONG).show();
            return;
        }

        // validate passsword
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            return;
        }

        PasswordValidator passwordValidator = new PasswordValidator();

        if (!passwordValidator.validate(password)) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_LONG).show();
            return;
        }

        // check email availability
        checkEmailAvailability(email, password);
    }

    private void checkEmailAvailability(final String email, final String password) {
        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setTitle("Comprobando datos...");
        progressDialog.show();

        authService.isEmailAvailable(email, new FirebaseAuthService.OnEmailAvailableCheck() {
            @Override
            public void onComplete(boolean isEmailAvailable) {
                progressDialog.dismiss();

                // if email is available, sign up
                if (isEmailAvailable) {
                    signUp(email, password);
                }
                // if email is NOT available, reject
                else {
                    Toast.makeText(RegistroActivity.this, "Correo no disponible. Intenta con otro.", Toast.LENGTH_LONG).show();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegistroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void signUp(String email, String password) {
        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setTitle("Registrando...");
        progressDialog.show();

        authService.signUpWithEmail(email, password, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();

                Intent intent = new Intent(RegistroActivity.this, PostRegistroActivity.class);
                startActivity(intent);
                finish();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegistroActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
