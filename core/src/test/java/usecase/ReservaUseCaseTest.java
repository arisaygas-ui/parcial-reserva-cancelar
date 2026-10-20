package reserva.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import reserva.exception.*;
import reserva.modelo.ReservationRepository;
import reserva.modelo.Reserva;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancelReservationUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private CancelReservationUseCase useCase;

    @Test
    void debeCancelarReservaExitosamente() {
        UUID id = UUID.randomUUID();
        Reserva reserva = Reserva.instance(id, "usuario@ejemplo.com", LocalDate.now(), 4);

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reserva));
        when(reservationRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        Reserva reservaCancelada = useCase.execute(id);

        assertNotNull(reservaCancelada);
        assertEquals("CANCELADA", reservaCancelada.getEstado());
        verify(reservationRepository, times(1)).findById(id);
        verify(reservationRepository, times(1)).save(any(Reserva.class));
    }

    @Test
    void debeLanzarExcepcionSiReservaNoExiste() {
        UUID idInexistente = UUID.randomUUID();

        when(reservationRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> {
            useCase.execute(idInexistente);
        });

        verify(reservationRepository, times(1)).findById(idInexistente);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiReservaYaEstabaCancelada() {
        UUID id = UUID.randomUUID();
        Reserva reserva = Reserva.instance(id, "usuario@ejemplo.com", LocalDate.now(), 4);
        reserva.cancelar(); // Queda cancelada antes del UseCase

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reserva));

        assertThrows(ReservaYaCanceladaException.class, () -> {
            useCase.execute(id);
        });

        verify(reservationRepository, times(1)).findById(id);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void debeLanzarExcepcionSiFallaLaPersistencia() {
        UUID id = UUID.randomUUID();
        Reserva reserva = Reserva.instance(id, "usuario@ejemplo.com", LocalDate.now(), 4);

        when(reservationRepository.findById(id)).thenReturn(Optional.of(reserva));
        when(reservationRepository.save(any(Reserva.class))).thenThrow(new PersistenceException("Error de base de datos"));

        assertThrows(PersistenceException.class, () -> {
            useCase.execute(id);
        });

        verify(reservationRepository, times(1)).findById(id);
        verify(reservationRepository, times(1)).save(any(Reserva.class));
    }
}