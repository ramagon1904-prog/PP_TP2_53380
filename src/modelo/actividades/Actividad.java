package modelo.actividades;

import exepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.Inscripcion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Actividad implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private int cupoMaximo;
    public static final int CUPO_MINIMO = 2;

    private List<Inscripcion> inscripciones;

    public Actividad(int id, String titulo, int cupoMaximo) {
        this.id = id;
        this.titulo = titulo;
        this.cupoMaximo = cupoMaximo;
        this.inscripciones = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getCupoMaximo() { return cupoMaximo; }
    public List<Inscripcion> getInscripciones() { return inscripciones; }


    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        if (inscripciones.size() >= cupoMaximo) {
            throw new CupoExcedidoException("No hay cupo disponible en la actividad: " + titulo);
        }
        Inscripcion inscripcion = new Inscripcion(estudiante);
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    public void mostrarInscripciones() {
        System.out.println("  Inscripciones para [" + titulo + "]:");
        if (inscripciones.isEmpty()) {
            System.out.println("    (Sin inscripciones)");
        } else {
            for (Inscripcion ins : inscripciones) {
                System.out.println("    - " + ins.getEstudiante().getNombre() + " (Estado: " + ins.getEstado() + ")");
            }
        }
    }

    public final void mostrarIdentificacion() {
        System.out.println("Actividad #" + id + ": " + titulo + " [" + getTipo() + "]");
    }

    public abstract double calcularCostoMateriales();
    public abstract String getTipo();
}