package com.example.chambeechat.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chambeechat.R;
import com.example.chambeechat.firebase.FirebaseAuthService;
import com.example.chambeechat.firebase.FirestoreService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreo;
    private EditText etContrasena;
    private Button bIniciarSesion;
    private TextView tvOlvideContrasena;
    private ImageView ivGoogle;
    private TextView tvRegistrate;

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
                //iniciarSesion();
                signUp();
                //listUsers();
                //addUser();
                //sendMessage();
                //listChatsFromUser();
            }
        });
    }

    private void iniciarSesion() {
        String correo = etCorreo.getText().toString();
        String contrasena = etContrasena.getText().toString();

        Intent intent = new Intent(LoginActivity.this, InicioActivity.class);
        startActivity(intent);
        finish();
    }

    private void signUp() {
        String correo = etCorreo.getText().toString();
        String contrasena = etContrasena.getText().toString();

        final FirebaseAuthService service = new FirebaseAuthService();

        service.isEmailAvailable(correo, new FirebaseAuthService.OnEmailAvailableCheck() {
            @Override
            public void onComplete(boolean isEmailAvailable) {
                if (isEmailAvailable) {
                    Log.e("chambee", "Email is available");
                }
                else {
                    Log.e("chambee", "Email is not available");
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("chambee", "Email is not available: " + e.getMessage());
            }
        });

        /*service.signUpWithEmail(correo, contrasena, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("chambee", "Success creating user");
                Log.e("chambee", "Current user: " + service.getCurrentUser().toString());
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("chambee", "Error listing users: " + e.getMessage());
            }
        });*/
    }

    private void listUsers() {
        FirestoreService firestoreService = new FirestoreService();
        firestoreService.getUsers().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.e("chambee", "Success listing users");

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Log.e("chambee", documentSnapshot.getId() + " -> " + documentSnapshot.getData());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("chambee", "Error listing users: " + e.getMessage());
            }
        });
    }

    private void addUser() {
        String correo = etCorreo.getText().toString();
        String contrasena = etContrasena.getText().toString();

        FirestoreService firestoreService = new FirestoreService();

        firestoreService.addUser(correo, contrasena)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("chambee", "User added!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("chambee", "Error adding user :(");
            }
        });
    }

    private void sendMessage() {
        String sender = "1y1M21WODQ1hjNAEl0R8";
        String reciever = "7P7ccZyxWsqZPT6e3CBo";
        String message = "Message from cr@gmail.com to jayson@gmail.com";

        FirestoreService service = new FirestoreService();

        Log.e("chambee", "Sending message on transaction...");

        service.transactionSendMessage(sender, reciever, message, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("chambee", "Message sent successfully");
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("chambee", "Error sending message: " + e.getMessage());
            }
        });
    }

    private void listChatsFromUser() {
        String userId = "24NHf4k0O5OijyTPCVho";

        FirestoreService service = new FirestoreService();

        service.getChatsFromUser(userId)
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.e("chambee", documentSnapshot.getId() + " -> " + documentSnapshot.getData());
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("chambee", "Error getting chats: " + e.getMessage());
            }
        });
    }
}
