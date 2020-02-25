package com.example.chambeechat.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.chambeechat.R;
import com.example.chambeechat.data.Datos;

import java.util.List;

public class PostRegistro2Fragment extends Fragment {

    private Spinner sEstado;
    private Spinner sCiudad;

    public PostRegistro2Fragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_registro2, container, false);

        sEstado = view.findViewById(R.id.sEstado);
        sCiudad = view.findViewById(R.id.sCiudad);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View viewFragment, @Nullable Bundle savedInstanceState) {
        final List<String> estados = Datos.getEstados();
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(viewFragment.getContext(), R.layout.support_simple_spinner_dropdown_item, estados);

        sEstado.setAdapter(adapterEstados);
        sEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String estadoSeleccionado = estados.get(position);
                List<String> ciudades = Datos.getCiudades(estadoSeleccionado);
                ArrayAdapter<String> adapterCiudades = new ArrayAdapter<>(viewFragment.getContext(), R.layout.support_simple_spinner_dropdown_item, ciudades);

                sCiudad.setAdapter(adapterCiudades);
            }
        });
    }

    public String getDataEstado() {
        return sEstado.getSelectedItem().toString();
    }

    public String getDataCiudad() {
        return sCiudad.getSelectedItem().toString();
    }
}
