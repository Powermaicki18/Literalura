package com.example.aluracursos.Literalura.repository;

import com.example.aluracursos.Literalura.model.Idioma;
import com.example.aluracursos.Literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILibroRepository extends JpaRepository<Libro, Long> {

    // Buscar libro por título exacto (para evitar duplicados)
    Optional<Libro> findByTituloIgnoreCase(String titulo);

    // Consulta principal del challenge: Libros por idioma
    @Query("SELECT l FROM Libro l JOIN l.idioma i WHERE i = :idioma ORDER BY l.titulo ASC")
    List<Libro> encontrarLibrosPorIdioma(@Param("idioma") Idioma idioma);

    // Libros ordenados por título (usando método derivado)
    List<Libro> findAllByOrderByTituloAsc();

    // Libros ordenados por número de descargas (más descargados primero)
    List<Libro> findAllByOrderByNumeroDescargasDesc();

    // ==================== CONSULTAS SIMPLIFICADAS PARA ESTADÍSTICAS ====================

    // Promedio de descargas de todos los libros
    @Query("SELECT AVG(l.numeroDescargas) FROM Libro l WHERE l.numeroDescargas IS NOT NULL")
    Double promedioDescargas();

    // Libro más descargado
    @Query("SELECT MAX(l.numeroDescargas) FROM Libro l WHERE l.numeroDescargas IS NOT NULL")
    Double maximoDescargas();

    // Libro menos descargado
    @Query("SELECT MIN(l.numeroDescargas) FROM Libro l WHERE l.numeroDescargas IS NOT NULL")
    Double minimoDescargas();

    // Total de descargas de todos los libros
    @Query("SELECT SUM(l.numeroDescargas) FROM Libro l WHERE l.numeroDescargas IS NOT NULL")
    Double totalDescargas();

    // Contar libros por idioma
    @Query("SELECT COUNT(l) FROM Libro l JOIN l.idioma i WHERE i = :idioma")
    Long contarLibrosPorIdioma(@Param("idioma") Idioma idioma);

    // Top 10 libros más descargados
    @Query("SELECT l FROM Libro l WHERE l.numeroDescargas IS NOT NULL ORDER BY l.numeroDescargas DESC")
    List<Libro> encontrarLibrosMasDescargados();

    // Contar total de libros
    @Query("SELECT COUNT(l) FROM Libro l")
    Long contarTotalLibros();

    // Idiomas únicos en la base de datos - CONSULTA SIMPLIFICADA
    @Query("SELECT DISTINCT i FROM Libro l JOIN l.idioma i")
    List<Idioma> encontrarIdiomasUnicos();
}