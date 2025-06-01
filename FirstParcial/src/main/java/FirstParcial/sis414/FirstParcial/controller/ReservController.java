package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Cliente;
import FirstParcial.sis414.FirstParcial.entity.Habitacion;
import FirstParcial.sis414.FirstParcial.entity.Pago;
import FirstParcial.sis414.FirstParcial.entity.Reserv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/reservas")
public class ReservController {

    private static final Logger logger = LoggerFactory.getLogger(ReservController.class);
    private final List<Reserv> reservas = new ArrayList<>();
    @GetMapping
    public ResponseEntity<List<Reserv>> getAllReservas() {
        logger.info("Obteniendo listado completo de reservas");
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservaById(@PathVariable Long id) {
        Optional<Reserv> reserva = reservas.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();

        if (reserva.isPresent()) {
            return ResponseEntity.ok(reserva.get());
        } else {
            logger.warn("Reserva con ID {} no encontrada", id);
            return ResponseEntity.status(404).body("Reserva no encontrada");
        }
    }

    @PostMapping
    public ResponseEntity<?> createReserva(@RequestBody Reserv reserva) {
        try {
            if (reserva.getId() == null) {
                return ResponseEntity.badRequest().body("El ID es requerido");
            }

            if (reservas.stream().anyMatch(r -> r.getId().equals(reserva.getId()))) {
                return ResponseEntity.badRequest().body("El ID ya está registrado");
            }

            if (reserva.getFechaEntrada().isAfter(reserva.getFechaSalida())) {
                return ResponseEntity.badRequest().body("La fecha de entrada debe ser anterior a la fecha de salida");
            }

            reservas.add(reserva);
            logger.info("Reserva creada exitosamente: {}", reserva);
            return ResponseEntity.ok(reserva);

        } catch (Exception e) {
            logger.error("Error al crear reserva: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error interno del servidor");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReserva(@PathVariable Long id, @RequestBody Reserv reserva) {
        try {
            if (!id.equals(reserva.getId())) {
                return ResponseEntity.badRequest().body("ID en URL no coincide con ID en body");
            }

            Optional<Reserv> existingReserva = reservas.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst();

            if (existingReserva.isEmpty()) {
                return ResponseEntity.status(404).body("Reserva no encontrada");
            }

            if (reserva.getFechaEntrada().isAfter(reserva.getFechaSalida())) {
                return ResponseEntity.badRequest().body("La fecha de entrada debe ser anterior a la fecha de salida");
            }

            Reserv toUpdate = existingReserva.get();
            toUpdate.setClienteId(reserva.getClienteId());
            toUpdate.setHabitacionId(reserva.getHabitacionId());
            toUpdate.setFechaEntrada(reserva.getFechaEntrada());
            toUpdate.setFechaSalida(reserva.getFechaSalida());
            toUpdate.setPagoId(reserva.getPagoId());

            logger.info("Reserva actualizada: {}", toUpdate);
            return ResponseEntity.ok(toUpdate);

        } catch (Exception e) {
            logger.error("Error al actualizar reserva: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error interno del servidor");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateReserva(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        try {
            Optional<Reserv> reservaOpt = reservas.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst();

            if (reservaOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Reserva no encontrada");
            }

            Reserv reserva = reservaOpt.get();
            boolean hasUpdates = false;

            if (updates.containsKey("pago")) {
                Object pagoUpdate = updates.get("pago");

                if (pagoUpdate instanceof Map) {
                    Map<?, ?> pagoMap = (Map<?, ?>) pagoUpdate;
                    reserva.setPagoId(((Number) pagoMap.get("id")).longValue());
                } else if (pagoUpdate instanceof Number) {
                    reserva.setPagoId(((Number) pagoUpdate).longValue());
                }

                hasUpdates = true;
            }

            if (updates.containsKey("fechaEntrada")) {
                reserva.setFechaEntrada(LocalDate.parse((String) updates.get("fechaEntrada")));
                hasUpdates = true;
            }

            if (!hasUpdates) {
                return ResponseEntity.badRequest().body("No se proporcionaron campos válidos");
            }

            return ResponseEntity.ok(reserva);

        } catch (Exception e) {
            logger.error("Error en actualización parcial", e);
            return ResponseEntity.badRequest().body("Formato de datos inválido");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReserva(@PathVariable Long id) {
        try {
            boolean removed = reservas.removeIf(r -> r.getId().equals(id));
            if (removed) {
                logger.info("Reserva con ID {} eliminada", id);
                return ResponseEntity.ok("Reserva eliminada exitosamente");
            }
            return ResponseEntity.status(404).body("Reserva no encontrada");
        } catch (Exception e) {
            logger.error("Error al eliminar reserva: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error interno del servidor");
        }
    }
}