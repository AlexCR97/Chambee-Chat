package com.example.chambeechat.data;

public enum Situacion {

    BUSCANDO_TRABAJO("Buscando trabajo"),
    CONTRATANDO("Contratando"),
    ;

    private String situation;

    Situacion(String situation) {
        this.situation = situation;
    }

    @Override
    public String toString() {
        return situation;
    }
}
