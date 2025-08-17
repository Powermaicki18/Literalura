package com.example.aluracursos.Literalura.principal;

import com.example.aluracursos.Literalura.model.Idioma;
import com.example.aluracursos.Literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal<DatosLibros> {

    @Autowired
    private LibroService libroService;

    private Scanner teclado = new Scanner(System.in);

    public void mostrarMenu() {
        // Verificar que la inyección funcione
        if (libroService == null) {
            System.out.println("❌ FALLA CRÍTICA: LibroService es null. Spring Boot no está funcionando correctamente.");
            return;
        }

        System.out.println("✅ LibroService inyectado correctamente. Iniciando aplicación...");
        mostrarBienvenida();

        int opcion = -1;
        while (opcion != 0) {
            mostrarOpciones();
            try {
                opcion = teclado.nextInt();
                teclado.nextLine();

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        libroService.listarLibrosRegistrados();
                        break;
                    case 3:
                        libroService.listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosPorAno();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        libroService.generarEstadisticas();
                        break;
                    case 7:
                        libroService.mostrarIdiomasDisponibles();
                        break;
                    case 8:
                        System.out.println("⚠️ Top 10 temporalmente usando estadísticas.");
                        libroService.generarEstadisticas();
                        break;
                    case 0:
                        mostrarDespedida();
                        break;
                    default:
                        System.out.println("❌ Opción inválida.");
                }

                if (opcion != 0) {
                    esperarEnter();
                }

            } catch (InputMismatchException e) {
                System.out.println("❌ Por favor ingresa un número válido.");
                teclado.nextLine();
                esperarEnter();
            }
        }

        teclado.close();
    }

    private void buscarLibroPorTitulo() {
        System.out.println("\n🔍 BUSCAR LIBRO POR TÍTULO");
        System.out.print("📖 Ingresa el título: ");
        String titulo = teclado.nextLine().trim().toUpperCase();

        if (titulo.isEmpty()) {
            System.out.println("❌ El título no puede estar vacío.");
            return;
        }

        libroService.buscarLibroPorTitulo(titulo);
    }

    private void listarAutoresVivosPorAno() {
        System.out.println("\n🕒 AUTORES VIVOS EN DETERMINADO AÑO");
        System.out.print("📅 Ingresa el año: ");

        try {
            Integer ano = teclado.nextInt();
            teclado.nextLine();
            libroService.listarAutoresVivosPorAno(ano);
        } catch (InputMismatchException e) {
            System.out.println("❌ Por favor ingresa un año válido.");
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\n🌐 LIBROS POR IDIOMA");
        mostrarIdiomasParaSeleccionar();
        System.out.print("\n🔤 Código del idioma: ");
        String codigo = teclado.nextLine().trim().toLowerCase();

        try {
            Idioma idioma = Idioma.fromString(codigo);
            libroService.listarLibrosPorIdioma(idioma);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Código de idioma no válido.");
        }
    }

    private void mostrarBienvenida() {
        System.out.println("\n📚 LITERALURA - Challenge Alura + ONE 📚");
    }

    private void mostrarOpciones() {
        System.out.println("\n========== MENÚ PRINCIPAL ==========");
        System.out.println("1️⃣ - Buscar libro por título");
        System.out.println("2️⃣ - Listar libros registrados");
        System.out.println("3️⃣ - Listar autores registrados");
        System.out.println("4️⃣ - Autores vivos en determinado año");
        System.out.println("5️⃣ - Libros por idioma");
        System.out.println("6️⃣ - Generar estadísticas");
        System.out.println("7️⃣ - Mostrar idiomas disponibles");
        System.out.println("8️⃣ - Top libros más descargados");
        System.out.println("0️⃣ - Salir");
        System.out.print("\n🔍 Selecciona una opción: ");
    }

    private void mostrarIdiomasParaSeleccionar() {
        System.out.println("\n📋 Idiomas disponibles:");
        System.out.println("es - Español | en - Inglés | fr - Francés");
        System.out.println("pt - Portugués | it - Italiano | de - Alemán");
    }

    private void esperarEnter() {
        System.out.println("\n💡 Presiona Enter para continuar...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Manejar silenciosamente
        }
    }

    private void mostrarDespedida() {
        System.out.println("\n¡Gracias por usar LiterAlura! 📚✨");
    }
}