package com.example.chambeechat.services;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chambeechat.models.Mensaje;
import com.example.chambeechat.models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreService {

    public interface OnDataParsedListener<T> {
        void onParsed(T parsedData);
    }

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public void getChatsFromUser(String uid, final OnDataParsedListener<List<Usuario>> onDataParsedListener, final OnFailureListener onFailureListener) {
        firestore.collection("chats")
        .document(uid)
        .get()
        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<String> uids = (List<String>) documentSnapshot.get("withUsers");

                getUsuariosFromUidList(uids, new OnSuccessListener<List<Usuario>>() {
                    @Override
                    public void onSuccess(List<Usuario> usuarios) {
                        onDataParsedListener.onParsed(usuarios);
                    }
                }, onFailureListener);
            }
        })
        .addOnFailureListener(onFailureListener);
    }

    public void getMessagesFromChat(final String senderUid, final String receiverUid, final OnDataParsedListener<List<Mensaje>> onDataParsedListener, OnFailureListener onFailureListener) {
        firestore.collection("messages")
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Mensaje> mensajeList = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Mensaje mensaje = documentSnapshot.toObject(Mensaje.class);

                    if ((mensaje.getSender().equals(senderUid) && mensaje.getReciever().equals(receiverUid)) ||
                        (mensaje.getSender().equals(receiverUid) && mensaje.getReciever().equals(senderUid))) {
                        mensajeList.add(mensaje);
                    }
                }

                Collections.sort(mensajeList, new Comparator<Mensaje>() {
                    @Override
                    public int compare(Mensaje o1, Mensaje o2) {
                        return o1.getTime().compareTo(o2.getTime());
                    }
                });

                onDataParsedListener.onParsed(mensajeList);
            }
        })
        .addOnFailureListener(onFailureListener);
    }

    public Task<QuerySnapshot> getUsers() {
        return firestore.collection("users").get();
    }

    public void getUsuario(String id, final OnDataParsedListener<Usuario> onDataParsedListener, OnFailureListener onFailureListener) {
        firestore.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Usuario usuario = documentSnapshot.toObject(Usuario.class);
                        onDataParsedListener.onParsed(usuario);
                    }
                })
                .addOnFailureListener(onFailureListener);
    }

    public void getUsuarios(final OnDataParsedListener<List<Usuario>> onDataParsedListener, OnFailureListener onFailureListener) {
        firestore.collection("users")
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Usuario> usuarios = new ArrayList<>();

                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    usuarios.add(snapshot.toObject(Usuario.class));
                }

                onDataParsedListener.onParsed(usuarios);
            }
        })
        .addOnFailureListener(onFailureListener);
    }

    public void getUsuariosExcept(final String uid, final OnDataParsedListener<List<Usuario>> onDataParsedListener, OnFailureListener onFailureListener) {
        firestore.collection("users")
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Usuario> usuarios = new ArrayList<>();

                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    Usuario usuario = snapshot.toObject(Usuario.class);

                    if (!usuario.getUid().equals(uid)) {
                        usuarios.add(usuario);
                    }
                }

                onDataParsedListener.onParsed(usuarios);
            }
        })
        .addOnFailureListener(onFailureListener);
    }

    public void getUsuariosFromUidList(final List<String> uids, OnSuccessListener<List<Usuario>> onSuccessListener, OnFailureListener onFailureListener) {
        final CollectionReference usersCollectionRef = firestore.collection("users");

        firestore.runTransaction(new Transaction.Function<List<Usuario>>() {
            @Nullable
            @Override
            public List<Usuario> apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                List<Usuario> usuarios = new ArrayList<>();

                for (String uid : uids) {
                    DocumentSnapshot userDocSnapshot = transaction.get(usersCollectionRef.document(uid));
                    Usuario usuario = userDocSnapshot.toObject(Usuario.class);

                    usuarios.add(usuario);
                }

                return usuarios;
            }
        })
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener);
    }

    public void sendMessage(final String sender, final String reciever, final String message, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        final DocumentReference messagesDocument = firestore.collection("messages").document();
        final DocumentReference chatsSenderDocument = firestore.collection("chats").document(sender);
        final DocumentReference chatsRecieverDocument = firestore.collection("chats").document(reciever);

        firestore.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                // read al necessary data before writing
                DocumentSnapshot chatsSenderSnapshot = transaction.get(chatsSenderDocument);
                DocumentSnapshot chatsRecieverSnapshot = transaction.get(chatsRecieverDocument);
                boolean chatsSenderExists = chatsSenderSnapshot.exists();
                boolean chatsRecieverExists = chatsRecieverSnapshot.exists();

                // add message to messages collection
                Map<String, Object> messageDocumentData = new HashMap<>();
                messageDocumentData.put("sender", sender);
                messageDocumentData.put("reciever", reciever);
                messageDocumentData.put("message", message);
                messageDocumentData.put("time", Calendar.getInstance().getTime());

                transaction.set(messagesDocument, messageDocumentData);

                // if chats on sender side exist, update
                if (chatsSenderExists) {
                    transaction.update(chatsSenderDocument, "withUsers", FieldValue.arrayUnion(reciever));
                }
                // if chats on sender side DO NOT exist, create
                else {
                    Map<String, List<String>> chatsSenderData = new HashMap<>();
                    chatsSenderData.put("withUsers", Collections.singletonList(reciever));
                    transaction.set(chatsSenderDocument, chatsSenderData);
                }

                // if chats on receiver side exist, update
                if (chatsRecieverExists) {
                    transaction.update(chatsRecieverDocument, "withUsers", FieldValue.arrayUnion(sender));
                }
                // if chats on receiver side DO NOT exist, create
                else {
                    Map<String, List<String>> chatsRecieverData = new HashMap<>();
                    chatsRecieverData.put("withUsers", Collections.singletonList(sender));
                    transaction.set(chatsRecieverDocument, chatsRecieverData);
                }

                return null;
            }
        })
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener);
    }

    public void updateUsuario(Usuario usuario, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
        firestore.collection("users")
        .document(usuario.getUid())
        .set(usuario)
        .addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener);
    }



    public void updateUsuario(final Usuario usuario, String imgPath, Uri img, final OnSuccessListener<Void> onSuccessListener, final OnFailureListener onFailureListener) {
        uploadImage(imgPath, img, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                updateUsuario(usuario, onSuccessListener, onFailureListener);
            }
        }, onFailureListener);
    }

    private void uploadImage(String path, Uri image, OnSuccessListener<UploadTask.TaskSnapshot> onSuccessListener, OnFailureListener onFailureListener) {
        firebaseStorage.getReference()
                .child(path)
                .putFile(image)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }
}











