package com.example.aluracursos.Literalura.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    private Integer fechaNacimiento;
    private Integer fechaFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    // Constructor desde DatosAutor (para mapear desde API)
    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaFallecimiento = datosAutor.fechaFallecimiento();
    }

    public Autor() {}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("********** AUTOR **********\n");
        sb.append("Nombre: ").append(nombre).append("\n");
        sb.append("Año de nacimiento: ").append(fechaNacimiento != null ? fechaNacimiento : "Desconocido").append("\n");
        sb.append("Año de fallecimiento: ").append(fechaFallecimiento != null ? fechaFallecimiento : "Vivo").append("\n");

        if (!libros.isEmpty()) {
            sb.append("Libros: ");
            for (int i = 0; i < libros.size(); i++) {
                sb.append(libros.get(i).getTitulo());
                if (i < libros.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        sb.append("***************************");
        return sb.toString();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Integer fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Integer getFechaFallecimiento() { return fechaFallecimiento; }
    public void setFechaFallecimiento(Integer fechaFallecimiento) { this.fechaFallecimiento = fechaFallecimiento; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }
}