package com.example.chambeechat.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chambeechat.R;
import com.example.chambeechat.data.Datos;
import com.example.chambeechat.models.Usuario;
import com.example.chambeechat.services.FirestoreService;
import com.example.chambeechat.ui.adapters.ChatsAdapter;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView rvChats;

    private FirestoreService firestoreService = new FirestoreService();

    public ChatsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        rvChats = view.findViewById(R.id.rvChats);

        firestoreService.getChatsFromUser(Datos.getUsuario().getUid(), new FirestoreService.OnDataParsedListener<List<Usuario>>() {
            @Override
            public void onParsed(List<Usuario> parsedData) {
                ChatsAdapter adapter = new ChatsAdapter(view.getContext(), parsedData);

                rvChats.setLayoutManager(new LinearLayoutManager(view.getContext()));
                rvChats.setAdapter(adapter);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
