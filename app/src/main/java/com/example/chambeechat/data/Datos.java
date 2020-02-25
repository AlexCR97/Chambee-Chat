package com.example.chambeechat.data;

import android.net.Uri;

import com.example.chambeechat.models.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Datos {

    private static Map<String, List<String>> estados = new HashMap<>();
    static {
        estados.put("Tamaulipas", Arrays.asList(
                "El Mante",
                "Tampico",
                "Madero",
                "Victoria"
        ));

        estados.put("Nuevo Leon", Arrays.asList(
                "Monterrey"
        ));
    }

    private static Usuario usuario;
    private static Uri imagenPerfil;

    public static List<String> getEstados() {
        return new ArrayList<>(estados.keySet());
    }

    public static List<String> getCiudades(String estado) {
        if (!estados.containsKey(estado)) {
            return new ArrayList<>();
        }

        return estados.get(estado);
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Datos.usuario = usuario;
    }

    public static Uri getImagenPerfil() {
        return imagenPerfil;
    }

    public static void setImagenPerfil(Uri imagenPerfil) {
        Datos.imagenPerfil = imagenPerfil;
    }
}
