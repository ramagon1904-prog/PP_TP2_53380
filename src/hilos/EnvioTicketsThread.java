package hilos;

import modelo.EventoUniversitario;
import modelo.Inscripcion;
import modelo.actividades.Actividad;

public class EnvioTicketsThread extends Thread {
    private EventoUniversitario evento;

    public EnvioTicketsThread(EventoUniversitario evento) {
        this.evento = evento;
    }

    @Override
    public void run() {
        System.out.println("Iniciando envío de tickets concurrentes...");
        for (Actividad act : evento.getActividades()) {
            for (Inscripcion ins : act.getInscripciones()) {
                Inscripcion.TicketDeAcceso ticket = ins.generarTicket();
                if (ticket != null) {
                    ticket.enviarTicket();
                }
            }
        }
        System.out.println("Finalizó el envío de tickets.");
    }
}