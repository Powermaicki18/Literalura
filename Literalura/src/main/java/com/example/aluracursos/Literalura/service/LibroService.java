package com.example.aluracursos.Literalura.service;

import com.example.aluracursos.Literalura.model.*;
import com.example.aluracursos.Literalura.repository.IAutorRepository;
import com.example.aluracursos.Literalura.repository.ILibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService {

    @Autowired
    private ILibroRepository libroRepository;

    @Autowired
    private IAutorRepository autorRepository;

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private IConvierteDatos conversor;

    private final String URL_BASE = "https://gutendx.com/books/";

    // ==================== BUSCAR Y GUARDAR LIBRO DESDE API ====================
    public boolean buscarLibroPorTitulo(String titulo) {
        String urlBusqueda = URL_BASE + "?search=" + titulo.replace(" ", "+");

        try {
            String json = consumoAPI.obtenerDatos(urlBusqueda);
            Datos datosRespuesta = conversor.obtenerDatos(json, Datos.class);

            if (datosRespuesta.resultados().isEmpty()) {
                System.out.println("❌ No se encontraron libros con ese título.");
                return false;
            }

            DatosLibro datosLibro = datosRespuesta.resultados().get(0);

            Optional<Libro> libroExistente = libroRepository.findByTituloIgnoreCase(datosLibro.titulo());
            if (libroExistente.isPresent()) {
                System.out.println("📚 El libro '" + datosLibro.titulo() + "' ya está registrado en la base de datos.");
                System.out.println(libroExistente.get());
                return false;
            }

            Autor autor = null;
            if (!datosLibro.autores().isEmpty()) {
                DatosAutor datosAutor = datosLibro.autores().get(0);

                Optional<Autor> autorExistente = autorRepository.findByNombreIgnoreCase(datosAutor.nombre());

                if (autorExistente.isPresent()) {
                    autor = autorExistente.get();
                } else {
                    autor = new Autor(datosAutor);
                    autor = autorRepository.save(autor);
                }
            }

            Libro nuevoLibro = new Libro(datosLibro);
            nuevoLibro.setAutor(autor);

            libroRepository.save(nuevoLibro);

            System.out.println("✅ Libro guardado exitosamente:");
            System.out.println(nuevoLibro);

            return true;

        } catch (Exception e) {
            System.out.println("❌ Error al buscar el libro: " + e.getMessage());
            e.printStackTrace(); // Para ver el error completo
            return false;
        }
    }

    // ==================== LISTAR TODOS LOS LIBROS ====================
    public void listarLibrosRegistrados() {
        try {
            List<Libro> libros = libroRepository.findAllByOrderByTituloAsc();

            if (libros.isEmpty()) {
                System.out.println("📚 No hay libros registrados en la base de datos.");
                return;
            }

            System.out.println("\n========== LIBROS REGISTRADOS ==========");
            libros.forEach(System.out::println);
            System.out.println("\n📊 Total de libros: " + libros.size());

        } catch (Exception e) {
            System.out.println("❌ Error al obtener los libros: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== LISTAR TODOS LOS AUTORES ====================
    public void listarAutoresRegistrados() {
        try {
            List<Autor> autores = autorRepository.findAllByOrderByNombreAsc();

            if (autores.isEmpty()) {
                System.out.println("👤 No hay autores registrados en la base de datos.");
                return;
            }

            System.out.println("\n========== AUTORES REGISTRADOS ==========");
            autores.forEach(System.out::println);
            System.out.println("\n📊 Total de autores: " + autores.size());

        } catch (Exception e) {
            System.out.println("❌ Error al obtener los autores: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== AUTORES VIVOS EN DETERMINADO AÑO ====================
    public void listarAutoresVivosPorAno(Integer ano) {
        if (ano == null || ano < 0 || ano > 2024) {
            System.out.println("❌ Por favor ingresa un año válido.");
            return;
        }

        try {
            List<Autor> autoresVivos = autorRepository.encontrarAutoresVivosPorAno(ano);

            if (autoresVivos.isEmpty()) {
                System.out.println("👤 No se encontraron autores vivos en el año " + ano + ".");
                return;
            }

            System.out.println("\n========== AUTORES VIVOS EN " + ano + " ==========");
            autoresVivos.forEach(System.out::println);
            System.out.println("\n📊 Total de autores vivos en " + ano + ": " + autoresVivos.size());

        } catch (Exception e) {
            System.out.println("❌ Error al buscar autores vivos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== LIBROS POR IDIOMA ====================
    public void listarLibrosPorIdioma(Idioma idioma) {
        if (idioma == null) {
            System.out.println("❌ Idioma no válido.");
            return;
        }

        try {
            List<Libro> librosPorIdioma = libroRepository.encontrarLibrosPorIdioma(idioma);

            if (librosPorIdioma.isEmpty()) {
                System.out.println("📚 No se encontraron libros en " + idioma.getNombreIdioma() + ".");
                return;
            }

            System.out.println("\n========== LIBROS EN " + idioma.getNombreIdioma().toUpperCase() + " ==========");
            librosPorIdioma.forEach(System.out::println);
            System.out.println("\n📊 Total de libros en " + idioma.getNombreIdioma() + ": " + librosPorIdioma.size());

        } catch (Exception e) {
            System.out.println("❌ Error al buscar libros por idioma: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== MOSTRAR IDIOMAS DISPONIBLES ====================
    public void mostrarIdiomasDisponibles() {
        try {
            List<Idioma> idiomasEnBD = libroRepository.encontrarIdiomasUnicos();

            System.out.println("\n========== IDIOMAS DISPONIBLES ==========");
            if (idiomasEnBD.isEmpty()) {
                System.out.println("No hay libros registrados aún.");
                return;
            }

            for (Idioma idioma : idiomasEnBD) {
                Long cantidad = libroRepository.contarLibrosPorIdioma(idioma);
                System.out.println(idioma.getNombreIdioma() + " (" + idioma.getCodigoIdioma() + ") - " + cantidad + " libros");
            }

        } catch (Exception e) {
            System.out.println("❌ Error al obtener idiomas disponibles: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== GENERAR ESTADÍSTICAS ====================
    public void generarEstadisticas() {
        try {
            Long totalLibros = libroRepository.contarTotalLibros();
            Long totalAutores = autorRepository.count();

            if (totalLibros == 0) {
                System.out.println("📊 No hay datos para generar estadísticas.");
                return;
            }

            Double promedioDescargas = libroRepository.promedioDescargas();
            Double maxDescargas = libroRepository.maximoDescargas();
            Double minDescargas = libroRepository.minimoDescargas();
            Double totalDescargas = libroRepository.totalDescargas();

            System.out.println("\n========== ESTADÍSTICAS GENERALES ==========");
            System.out.println("📚 Total de libros: " + totalLibros);
            System.out.println("👤 Total de autores: " + totalAutores);

            if (promedioDescargas != null) {
                System.out.println("📈 Promedio de descargas: " + String.format("%.2f", promedioDescargas));
            }
            if (maxDescargas != null) {
                System.out.println("🥇 Máximo de descargas: " + String.format("%.0f", maxDescargas));
            }
            if (minDescargas != null) {
                System.out.println("📉 Mínimo de descargas: " + String.format("%.0f", minDescargas));
            }
            if (totalDescargas != null) {
                System.out.println("📊 Total de descargas: " + String.format("%.0f", totalDescargas));
            }

            // Mostrar idiomas disponibles
            System.out.println("\n========== IDIOMAS EN LA BASE DE DATOS ==========");
            List<Idioma> idiomas = libroRepository.encontrarIdiomasUnicos();
            for (Idioma idioma : idiomas) {
                Long cantidad = libroRepository.contarLibrosPorIdioma(idioma);
                System.out.println("🌐 " + idioma.getNombreIdioma() + ": " + cantidad + " libros");
            }

            // Top 5 libros más descargados
            System.out.println("\n========== TOP 5 LIBROS MÁS DESCARGADOS ==========");
            List<Libro> top5 = libroRepository.encontrarLibrosMasDescargados()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());

            for (int i = 0; i < top5.size(); i++) {
                Libro libro = top5.get(i);
                System.out.println((i + 1) + ". " + libro.getTitulo() +
                        " - " + String.format("%.0f", libro.getNumeroDescargas()) + " descargas");
            }

        } catch (Exception e) {
            System.out.println("❌ Error al generar estadísticas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}