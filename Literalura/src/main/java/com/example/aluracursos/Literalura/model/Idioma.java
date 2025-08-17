package com.example.aluracursos.Literalura.model;

public enum Idioma {
    ESPANOL("es", "Español"),
    INGLES("en", "Inglés"),
    FRANCES("fr", "Francés"),
    PORTUGUES("pt", "Portugués"),
    ITALIANO("it", "Italiano"),
    ALEMAN("de", "Alemán");

    private String codigoIdioma;
    private String nombreIdioma;

    Idioma(String codigoIdioma, String nombreIdioma) {
        this.codigoIdioma = codigoIdioma;
        this.nombreIdioma = nombreIdioma;
    }

    public static Idioma fromString(String texto) {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.codigoIdioma.equalsIgnoreCase(texto)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningún idioma encontrado: " + texto);
    }

    public String getCodigoIdioma() {
        return codigoIdioma;
    }

    public String getNombreIdioma() {
        return nombreIdioma;
    }
}