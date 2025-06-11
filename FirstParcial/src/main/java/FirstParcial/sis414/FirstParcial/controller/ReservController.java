package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Reserv;
import FirstParcial.sis414.FirstParcial.repository.ReservRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/reservas")
public class ReservController {

    private static final Logger logger = LoggerFactory.getLogger(ReservController.class);
    private final ReservRepository reservRepository;

    public ReservController(ReservRepository reservRepository) {
        this.reservRepository = reservRepository;
    }

    @GetMapping
    public ResponseEntity<List<Reserv>> getAllReservas() {
        logger.info("Obteniendo lista completa de reservas");
        return ResponseEntity.ok(reservRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserv> getClienteById(@PathVariable Long id) {
        return reservRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody Reserv reserva) {
        logger.info("Creando nueva reserva");
        return ResponseEntity.ok(reservRepository.save(reserva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReserva(@PathVariable Long id, @RequestBody Reserv reservaDetails) {
        Optional<Reserv> optional = reservRepository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("Reserva con ID {} no encontrada para actualizar", id);
            return ResponseEntity.status(404).body("Reserva no encontrada");
        }

        Reserv reserva = optional.get();

        reserva.setCliente(reservaDetails.getCliente());
        reserva.setHabitacion(reservaDetails.getHabitacion());
        reserva.setFechaEntrada(reservaDetails.getFechaEntrada());
        reserva.setFechaSalida(reservaDetails.getFechaSalida());
        reserva.setPago(reservaDetails.getPago());

        logger.info("Reserva con ID {} actualizada completamente", id);
        return ResponseEntity.ok(reservRepository.save(reserva));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id) {
        if (!reservRepository.existsById(id)) {
            logger.warn("Reserva con ID {} no encontrada para eliminar", id);
            return ResponseEntity.status(404).body("Reserva no encontrada");
        }

        reservRepository.deleteById(id);
        logger.info("Reserva con ID {} eliminada exitosamente", id);
        return ResponseEntity.ok("Reserva eliminada exitosamente");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateReserva(@PathVariable Long id, @RequestBody Reserv updates) {
        Optional<Reserv> optional = reservRepository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("Reserva con ID {} no encontrada para actualización parcial", id);
            return ResponseEntity.status(404).body("Reserva no encontrada");
        }

        Reserv reserva = optional.get();

        if (updates.getCliente() != null) {
            reserva.setCliente(updates.getCliente());
            logger.info("Cliente actualizado para reserva ID: {}", id);
        }
        if (updates.getHabitacion() != null) {
            reserva.setHabitacion(updates.getHabitacion());
            logger.info("Habitación actualizada para reserva ID: {}", id);
        }
        if (updates.getFechaEntrada() != null) {
            reserva.setFechaEntrada(updates.getFechaEntrada());
            logger.info("Fecha de entrada actualizada para reserva ID: {}", id);
        }
        if (updates.getFechaSalida() != null) {
            reserva.setFechaSalida(updates.getFechaSalida());
            logger.info("Fecha de salida actualizada para reserva ID: {}", id);
        }
        if (updates.getPago() != null) {
            reserva.setPago(updates.getPago());
            logger.info("Pago actualizado para reserva ID: {}", id);
        }

        return ResponseEntity.ok(reservRepository.save(reserva));
    }
}
