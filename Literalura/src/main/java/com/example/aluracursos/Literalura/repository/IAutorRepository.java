package com.example.aluracursos.Literalura.repository;

import com.example.aluracursos.Literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAutorRepository extends JpaRepository<Autor, Long> {

    // Buscar autor por nombre exacto (para evitar duplicados)
    Optional<Autor> findByNombreIgnoreCase(String nombre);

    // Consulta principal del challenge: Autores vivos en un año determinado
    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :ano AND (a.fechaFallecimiento > :ano OR a.fechaFallecimiento IS NULL)")
    List<Autor> encontrarAutoresVivosPorAno(@Param("ano") Integer ano);

    // Autores ordenados por nombre (usando método derivado)
    List<Autor> findAllByOrderByNombreAsc();
}