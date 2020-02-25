package com.example.chambeechat.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.chambeechat.R;
import com.example.chambeechat.data.Situacion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostRegistro3Fragment extends Fragment {

    private Spinner sSituacion;
    private EditText etEstado;
    private EditText etAcercaDeMi;

    public PostRegistro3Fragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post_registro3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sSituacion = view.findViewById(R.id.sSituacion);
        etEstado = view.findViewById(R.id.etEstado);
        etAcercaDeMi = view.findViewById(R.id.etAcercaDeMi);

        Situacion[] situaciones = Situacion.values();
        List<String> situacionesList = new ArrayList<>();

        for (Situacion situacion : situaciones) {
            situacionesList.add(situacion.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), R.layout.support_simple_spinner_dropdown_item, situacionesList);

        sSituacion.setAdapter(adapter);
    }

    public String getDataSituacion() {
        return sSituacion.getSelectedItem().toString();
    }

    public String getDataEstado() {
        return etEstado.getText().toString();
    }

    public String getDataAcercaDeMi() {
        return etAcercaDeMi.getText().toString();
    }
}
