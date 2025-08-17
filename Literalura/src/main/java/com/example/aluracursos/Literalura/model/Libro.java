package com.example.aluracursos.Literalura.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Idioma> idioma;

    private Double numeroDescargas;

    // Constructor por defecto
    public Libro() {}

    // Constructor desde DatosLibro (para mapear desde API)
    public Libro( DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.numeroDescargas = datosLibro.numeroDescargas();

        // Convertir strings de idioma a enum Idioma
        this.idioma = datosLibro.idioma().stream()
                .map(Idioma::fromString)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("********** LIBRO **********\n");
        sb.append("Título: ").append(titulo).append("\n");
        sb.append("Autor: ").append(autor != null ? autor.getNombre() : "Desconocido").append("\n");
        sb.append("Idioma(s): ");

        if (idioma != null && !idioma.isEmpty()) {
            sb.append(idioma.stream()
                    .map(Idioma::getNombreIdioma)
                    .collect(Collectors.joining(", ")));
        } else {
            sb.append("No especificado");
        }

        sb.append("\nNúmero de descargas: ").append(numeroDescargas != null ? numeroDescargas : 0).append("\n");
        sb.append("***************************");
        return sb.toString();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    public List<Idioma> getIdioma() { return idioma; }
    public void setIdiomas(List<Idioma> idioma) { this.idioma = idioma; }

    public Double getNumeroDescargas() { return numeroDescargas; }
    public void setNumeroDescargas(Double numeroDescargas) { this.numeroDescargas = numeroDescargas; }

}