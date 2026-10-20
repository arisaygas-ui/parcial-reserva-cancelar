package reserva.usecase;
import java.util.UUID;

import reserva.exception.ReservationNotFoundException;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

public class CancelReservationUseCase {

    private final ReservationRepository reservationRepository;

    public CancelReservationUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reserva execute(UUID id) {
        Reserva reserva = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("No se encontró la reserva con ID: " + id));

        reserva.cancelar();

        return reservationRepository.save(reserva);
    }
}