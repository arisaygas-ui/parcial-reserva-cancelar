
package reserva.modelo;

import reserva.exception.*;

import java.time.LocalDate;
import java.util.UUID;

public class Reserva {
    private final UUID id;
    private final String emailContacto;
    private final LocalDate fecha;
    private final int cantidadPersonas;
    private String estado;

    private Reserva(UUID id, String emailContacto, LocalDate fecha, int cantidadPersonas) {
        if (id == null) {
            throw new ReservaConIdNuloException("El ID es obligatorio");
        }
        if (fecha == null) {
            throw new ReservaConFechaNulaException("La fecha es obligatoira");
        }
        if (emailContacto == null || emailContacto.isBlank()) {
            throw new ReservaConEmailVacioLanzaException("El email de contacto no puede estar vacío.");
        }
        if (cantidadPersonas <= 0) {
            throw new ReservaConCantidadPersonasNegativoLanzaException("La cantidad de personas debe ser mayor a cero.");
        }

        this.id = id;
        this.emailContacto = emailContacto;
        this.fecha = fecha;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = "ACTIVA";
    }

    public static Reserva instance(UUID id, String emailContacto, LocalDate fecha, int cantidadPersonas) {
        return new Reserva(id, emailContacto, fecha, cantidadPersonas);
    }

    public void cancelar() {
        if ("CANCELADA".equals(this.estado)) {
            throw new ReservaYaCanceladaException("La reserva ya se encuentra cancelada.");
        }
        this.estado = "CANCELADA";
    }

    public UUID getId() { return id; }

    public String getEmailContacto() { return emailContacto; }

    public LocalDate getFecha() { return fecha;}

    public int getCantidadPersonas() { return cantidadPersonas; }

    public String getEstado() { return estado; }
}