package modelo.actividades;

import certificacion.Certificable;
import modelo.Estudiante;

public class Taller extends Actividad implements Certificable {
    private boolean requiereNotebook;

    public Taller(int id, String titulo, int cupoMaximo, boolean requiereNotebook) {
        super(id, titulo, cupoMaximo);
        this.requiereNotebook = requiereNotebook;
    }

    public boolean isRequiereNotebook() { return requiereNotebook; }

    @Override
    public double calcularCostoMateriales() {
        return requiereNotebook ? 5000.0 : 3000.0;
    }

    @Override
    public String getTipo() {
        return "Taller";
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO DE ASISTENCIA: Se otorga a " + estudiante.getNombre() +
                " por participar en el Taller '" + getTitulo() + "'. Emite: " + ENTIDAD_EMISORA;
    }
}