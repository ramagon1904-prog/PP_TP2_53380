import certificacion.Certificable;
import exepciones.CupoExcedidoException;
import hilos.EnvioTicketsThread;
import modelo.*;
import modelo.actividades.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class App {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  UTN FRM - PP TP2: SISTEMA DE EVENTOS");
        System.out.println("=================================================");


        Estudiante e1 = new Estudiante("50123", "Ana López");
        Estudiante e2 = new Estudiante("50124", "Carlos Gómez");
        Estudiante e3 = new Estudiante("50125", "María Rodríguez");

        Sala salaA = new Sala(101, "Auditorio Central");

        EventoUniversitario evento = new EventoUniversitario("EVT01", "Jornadas de Tecnología 2026", 10000.0, false);
        evento.asignarSala(salaA);


        evento.crearActividad(1, "IA Generativa", 2, "Charla", "Dr. Pérez"); // Cupo máximo = 2
        evento.crearActividad(2, "Taller de Spring Boot", 5, "Taller", true);
        evento.crearActividad(3, "Curso de Java Avanzado", 5, "Curso", 20);

        Actividad charlaIA = evento.getActividades().get(0);
        Actividad tallerSpring = evento.getActividades().get(1);
        Actividad cursoJava = evento.getActividades().get(2);


        System.out.println("--- EJERCICIO 1: Inscripciones, Excepciones y Persistencia ---");
        try {

            System.out.println("Inscribiendo a Ana en IA Generativa...");
            charlaIA.inscribir(e1);

            System.out.println("Inscribiendo a Carlos en IA Generativa...");
            charlaIA.inscribir(e2);

            System.out.println("Intentando inscribir a María en IA Generativa (excediendo cupo)...");
            charlaIA.inscribir(e3);

        } catch (CupoExcedidoException e) {
            System.out.println(" [CATCH CUPO] Excepción capturada: " + e.getMessage());
        } finally {
            System.out.println(" [FINALLY] Proceso de prueba de inscripciones finalizado.");
        }

        System.out.println("Persistiendo evento en disco...");
        evento.persistirEvento();

        System.out.println("Recuperando evento desde archivo...");
        try {
            EventoUniversitario recuperado = EventoUniversitario.recuperarEvento("EVT01");
            System.out.println(" Evento recuperado correctamente: " + recuperado.getTitulo());
        } catch (FileNotFoundException e) {
            System.err.println(" [CATCH PERSISTENCIA] Archivo no encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println(" [CATCH PERSISTENCIA] Error de entrada/salida: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(" [CATCH PERSISTENCIA] Clase no encontrada: " + e.getMessage());
        }


        System.out.println("--- EJERCICIO 2: Certificados de Asistencia ---");
        try {
            tallerSpring.inscribir(e1);
            cursoJava.inscribir(e2);
        } catch (CupoExcedidoException ignored) {
        }

        System.out.println("Emitiendo certificados para actividades certificables:");
        for (Actividad act : evento.getActividades()) {
            if (act instanceof Certificable) {
                Certificable cert = (Certificable) act;
                for (Inscripcion ins : act.getInscripciones()) {
                    System.out.println(" -> " + cert.generarCertificado(ins.getEstudiante()));
                }
            } else {
                System.out.println(" -> La actividad '" + act.getTitulo() + "' (" + act.getTipo() + ") NO emite certificados.");
            }
        }

        System.out.println("\n--- EJERCICIO 3: Generics y Filtrado ---");
        List<Charla> charlas = evento.filtrarActividadesPorTipo(Charla.class);
        List<Taller> talleres = evento.filtrarActividadesPorTipo(Taller.class);
        List<Curso> cursos = evento.filtrarActividadesPorTipo(Curso.class);

        System.out.println("Cantidad de Charlas: " + charlas.size() + " | Costo materiales: $" + evento.calcularCostoMateriales(charlas));
        System.out.println("Cantidad de Talleres: " + talleres.size() + " | Costo materiales: $" + evento.calcularCostoMateriales(talleres));
        System.out.println("Cantidad de Cursos: " + cursos.size() + " | Costo materiales: $" + evento.calcularCostoMateriales(cursos));


        System.out.println("--- EJERCICIO 4: Concurrencia y Envió de Tickets ---");

        for (Actividad act : evento.getActividades()) {
            for (Inscripcion ins : act.getInscripciones()) {
                ins.setEstado("CONFIRMADA");
            }
        }


        EnvioTicketsThread hiloEnvio = new EnvioTicketsThread(evento);
        hiloEnvio.start();


        for (int i = 1; i <= 3; i++) {
            System.out.println(" Ejecutando tarea en paralelo " + i + "/3...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            hiloEnvio.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}