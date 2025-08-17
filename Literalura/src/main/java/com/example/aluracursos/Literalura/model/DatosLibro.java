package com.example.aluracursos.Literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autores,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("download_count") Double numeroDescargas
) {}