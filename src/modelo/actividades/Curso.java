package modelo.actividades;

import certificacion.Certificable;
import modelo.Estudiante;

public class Curso extends Actividad implements Certificable {
    private int horas;

    public Curso(int id, String titulo, int cupoMaximo, int horas) {
        super(id, titulo, cupoMaximo);
        this.horas = horas;
    }

    public int getHoras() { return horas; }

    @Override
    public double calcularCostoMateriales() {
        return horas * 800.0;
    }

    @Override
    public String getTipo() {
        return "Curso";
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO DE APROBACIÓN: Se otorga a " + estudiante.getNombre() +
                " por completar el Curso '" + getTitulo() + "' (" + horas + " hs). Emite: " + ENTIDAD_EMISORA;
    }
}