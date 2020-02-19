package com.example.chambeechat.firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreService {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public Task<Void> addUser(String email, String password) {
        String id = firestore.collection("users").document().getId();

        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("email", email);
        user.put("password", password);

        return firestore.collection("users")
                .document(id)
                .set(user);
    }

    public Task<QuerySnapshot> getUsers() {
        return firestore.collection("users").get();
    }

    public Task<DocumentSnapshot> getChatsFromUser(String id) {
        return firestore.collection("chats")
                .document(id)
                .get();
    }

    public void transactionSendMessage(final String sender, final String reciever, final String message, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
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
}











