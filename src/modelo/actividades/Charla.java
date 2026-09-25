package modelo.actividades;

public class Charla extends Actividad {
    private String disertante;

    public Charla(int id, String titulo, int cupoMaximo, String disertante) {
        super(id, titulo, cupoMaximo);
        this.disertante = disertante;
    }

    public String getDisertante() { return disertante; }

    @Override
    public double calcularCostoMateriales() {
        return 1500.0;
    }

    @Override
    public String getTipo() {
        return "Charla";
    }
}