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
import com.example.chambeechat.ui.adapters.ExplorarAdapter;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.List;

public class ExplorarFragment extends Fragment {

    private RecyclerView rvPersonas;
    private RecyclerView rvContratando;
    private RecyclerView rvBuscandoTrabajo;

    private FirestoreService firestoreService = new FirestoreService();

    public ExplorarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explorar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        rvPersonas = view.findViewById(R.id.rvPersonas);
        rvContratando = view.findViewById(R.id.rvContratando);
        rvBuscandoTrabajo = view.findViewById(R.id.rvBuscandoTrabajo);

        firestoreService.getUsuariosExcept(Datos.getUsuario().getUid(), new FirestoreService.OnDataParsedListener<List<Usuario>>() {
            @Override
            public void onParsed(List<Usuario> parsedData) {
                ExplorarAdapter explorarAdapter = new ExplorarAdapter(view.getContext(), parsedData);
                rvPersonas.setLayoutManager(new LinearLayoutManager(view.getContext()));
                rvPersonas.setAdapter(explorarAdapter);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
