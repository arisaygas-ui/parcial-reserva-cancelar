package reserva.modelo;

import org.junit.jupiter.api.Test;
import reserva.exception.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import java.time.LocalDate;

public class ReservaTest {

    @Test
    void debeCrearReservaConDatosValidos() {
        UUID id = UUID.randomUUID();
        String emailContacto = "usuario@ejemplo.com";
        LocalDate fecha = LocalDate.now();
        int cantidadPersonas = 4;

        Reserva reserva = Reserva.instance(id, emailContacto, fecha, cantidadPersonas);

        assertEquals(id, reserva.getId());
        assertEquals(emailContacto, reserva.getEmailContacto());
        assertEquals(fecha, reserva.getFecha());
        assertEquals(cantidadPersonas, reserva.getCantidadPersonas());
        assertEquals("ACTIVA", reserva.getEstado());
    }

    @Test
    void debeLanzarExcepcionSiIdEsNulo() {
        assertThrows(ReservaConIdNuloException.class, () -> {
            Reserva.instance(null, "usuario@ejemplo.com", LocalDate.now(), 4);
        });
    }

    @Test
    void debeLanzarExcepcionSiFechaEsNula() {
        assertThrows(ReservaConFechaNulaException.class, () -> {
            Reserva.instance(UUID.randomUUID(), "usuario@ejemplo.com", null, 4);
        });
    }

    @Test
    void debeLanzarExcepcionSiEmailContactoEsVacio() {
        assertThrows(ReservaConEmailVacioLanzaException.class, () -> {
            Reserva.instance(UUID.randomUUID(), "", LocalDate.now(), 4);
        });
    }

    @Test
    void debeLanzarExcepcionSiCantidadPersonasEsMenorOIgualACero() {
        assertThrows(ReservaConCantidadPersonasNegativoLanzaException.class, () -> {
            Reserva.instance(UUID.randomUUID(), "usuario@ejemplo.com", LocalDate.now(), 0);
        });
    }

    @Test
    void debeCancelarReservaExitosamente() {
        Reserva reserva = Reserva.instance(UUID.randomUUID(), "usuario@ejemplo.com", LocalDate.now(), 4);
        reserva.cancelar();

        assertEquals("CANCELADA", reserva.getEstado());
    }

    @Test
    void debeLanzarExcepcionSiSeIntentaCancelarUnaReservaYaCancelada() {
        Reserva reserva = Reserva.instance(UUID.randomUUID(), "usuario@ejemplo.com", LocalDate.now(), 4);
        reserva.cancelar();

        assertThrows(ReservaYaCanceladaException.class, () -> {
            reserva.cancelar();
        });
    }
}