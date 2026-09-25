package modelo;

import java.io.Serializable;
import java.time.LocalDate;

public class Inscripcion implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate fecha;
    private String estado;
    private Estudiante estudiante;

    public Inscripcion(Estudiante estudiante) {
        this.fecha = LocalDate.now();
        this.estado = "PENDIENTE";
        this.estudiante = estudiante;
    }

    public LocalDate getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Estudiante getEstudiante() { return estudiante; }

    public class TicketDeAcceso implements Serializable {
        private static final long serialVersionUID = 1L;
        private String idTicket;

        public TicketDeAcceso() {
            this.idTicket = "TCK-" + estudiante.getLegajo() + "-" + (int)(Math.random() * 9000 + 1000);
        }

        public String getIdTicket() { return idTicket; }

        public void enviarTicket() {
            System.out.println("  [HILO TICKET] Enviando Ticket " + idTicket + " a " + estudiante.getNombre() + "...");
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println(" ¡Ticket " + idTicket + " enviado con éxito!");
        }
    }


    public TicketDeAcceso generarTicket() {
        if ("CONFIRMADA".equalsIgnoreCase(this.estado)) {
            return new TicketDeAcceso();
        }
        return null;
    }
}