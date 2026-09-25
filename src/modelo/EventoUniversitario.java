package modelo;

import modelo.actividades.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EventoUniversitario implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private static int cantidadEventos = 0;

    private Sala sala;
    private List<Actividad> actividades;

    public EventoUniversitario(String id, String titulo, double costoBase, boolean gratuito) {
        this.id = id;
        this.titulo = titulo;
        this.costoBase = costoBase;
        this.gratuito = gratuito;
        this.actividades = new ArrayList<>();
        cantidadEventos++;
    }

    public EventoUniversitario(EventoUniversitario otro) {
        this.id = otro.id;
        this.titulo = otro.titulo;
        this.costoBase = otro.costoBase;
        this.gratuito = otro.gratuito;
        this.sala = otro.sala;
        this.actividades = new ArrayList<>(otro.actividades);
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public Sala getSala() { return sala; }
    public List<Actividad> getActividades() { return actividades; }
    public static int getCantidadEventos() { return cantidadEventos; }

    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    public void crearActividad(int id, String titulo, int cupoMaximo, String tipo, Object extra) {
        Actividad act = null;
        if ("Charla".equalsIgnoreCase(tipo)) {
            act = new Charla(id, titulo, cupoMaximo, (String) extra);
        } else if ("Taller".equalsIgnoreCase(tipo)) {
            act = new Taller(id, titulo, cupoMaximo, (Boolean) extra);
        } else if ("Curso".equalsIgnoreCase(tipo)) {
            act = new Curso(id, titulo, cupoMaximo, (Integer) extra);
        }
        if (act != null) {
            actividades.add(act);
        }
    }

    public double calcularCostoEstimado() {
        if (gratuito) return 0.0;
        double totalMateriales = 0.0;
        for (Actividad act : actividades) {
            totalMateriales += act.calcularCostoMateriales();
        }
        return costoBase + totalMateriales;
    }


    @SuppressWarnings("unchecked")
    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> resultado = new ArrayList<>();
        for (Actividad act : actividades) {
            if (tipo.isInstance(act)) {
                resultado.add((T) act);
            }
        }
        return resultado;
    }

    public double calcularCostoMateriales(List<? extends Actividad> listaActividades) {
        double total = 0.0;
        for (Actividad act : listaActividades) {
            total += act.calcularCostoMateriales();
        }
        return total;
    }

    public boolean persistirEvento() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("evento_" + id + ".dat"))) {
            oos.writeObject(this);
            return true;
        } catch (IOException e) {
            System.err.println("Error de I/O al persistir evento: " + e.getMessage());
            return false;
        }
    }

    public static EventoUniversitario recuperarEvento(String id) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("evento_" + id + ".dat"))) {
            return (EventoUniversitario) ois.readObject();
        }
    }

    public void mostrarDatos() {
        System.out.println("=== EVENTO UNIVERSITARIO ===");
        System.out.println("ID: " + id + " | Título: " + titulo);
        System.out.println("Costo Base: $" + costoBase + " | Gratuito: " + gratuito);
        System.out.println("Sala: " + (sala != null ? sala.getNombre() : "No asignada"));
        System.out.println("Cantidad de Actividades: " + actividades.size());
        for (Actividad act : actividades) {
            act.mostrarIdentificacion();
            act.mostrarInscripciones();
        }
    }
}