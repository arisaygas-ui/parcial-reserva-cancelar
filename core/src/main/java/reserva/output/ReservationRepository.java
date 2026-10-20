package reserva.output;
import reserva.modelo.Reserva;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    java.util.Optional<Reserva> findById(java.util.UUID id);
    Reserva save(Reserva reserva);
}
