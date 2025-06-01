package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Habitacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins ="http://localhost")
@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    private static final Logger logger = LoggerFactory.getLogger(HabitacionController.class);
    private List<Habitacion> habitaciones = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Habitacion>> getAllHabitaciones() {
        logger.info("Obteniendo lista completa de habitaciones");
        return ResponseEntity.ok(habitaciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHabitacionById(@PathVariable Long id) {
        logger.info("Buscando habitación con ID: {}", id);
        Optional<Habitacion> habitacion = habitaciones.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst();

        if (habitacion.isPresent()) {
            return ResponseEntity.ok(habitacion.get());
        } else {
            logger.warn("Habitación con ID {} no encontrada", id);
            return ResponseEntity.status(404).body("Habitación no encontrada");
        }
    }

    @PostMapping
    public ResponseEntity<?> createHabitacion(@RequestBody Habitacion habitacion) {
        if (habitacion.getId() == null) {
            logger.error("Intento de crear habitación sin ID");
            return ResponseEntity.status(400).body("Se requiere ID para crear una habitación");
        }

        if (habitaciones.stream().anyMatch(h -> h.getId().equals(habitacion.getId()))) {
            logger.error("Ya existe una habitación con ID: {}", habitacion.getId());
            return ResponseEntity.status(400).body("Ya existe una habitación con este ID");
        }

        habitaciones.add(habitacion);
        logger.info("Nueva habitación creada con ID: {}", habitacion.getId());
        return ResponseEntity.ok(habitacion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHabitacion(@PathVariable Long id, @RequestBody Habitacion habitacionDetails) {
        for (Habitacion habitacion : habitaciones) {
            if (habitacion.getId().equals(id)) {
                habitacion.setTipoHabitacion(habitacionDetails.getTipoHabitacion());
                habitacion.setEstado(habitacionDetails.getEstado());
                habitacion.setPrecioNoche(habitacionDetails.getPrecioNoche());

                logger.info("Habitación con ID {} actualizada completamente", id);
                return ResponseEntity.ok(habitacion);
            }
        }

        logger.warn("No se encontró habitación con ID {} para actualizar", id);
        return ResponseEntity.status(404).body("Habitación no encontrada para actualización");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHabitacion(@PathVariable Long id) {
        boolean removed = habitaciones.removeIf(h -> h.getId().equals(id));
        if (removed) {
            logger.info("Habitación con ID {} eliminada exitosamente", id);
            return ResponseEntity.ok("Habitación eliminada con éxito");
        } else {
            logger.warn("Intento de eliminar habitación no existente con ID: {}", id);
            return ResponseEntity.status(404).body("Habitación no encontrada para eliminar");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateHabitacion(@PathVariable Long id, @RequestBody Habitacion partialHabitacion) {
        Optional<Habitacion> habitacionOpt = habitaciones.stream()
                .filter(h -> h.getId().equals(id))
                .findFirst();

        if (habitacionOpt.isEmpty()) {
            logger.warn("Intento de actualizar habitación no existente con ID: {}", id);
            return ResponseEntity.status(404).body("Habitación no encontrada para actualización parcial");
        }

        Habitacion habitacion = habitacionOpt.get();

        if (partialHabitacion.getTipoHabitacion() != null) {
            habitacion.setTipoHabitacion(partialHabitacion.getTipoHabitacion());
            logger.info("Tipo de habitación actualizado para ID: {}", id);
        }

        if (partialHabitacion.getEstado() != null) {
            habitacion.setEstado(partialHabitacion.getEstado());
            logger.info("Estado actualizado para habitación ID: {}", id);
        }

        if (partialHabitacion.getPrecioNoche() > 0) {
            habitacion.setPrecioNoche(partialHabitacion.getPrecioNoche());
            logger.info("Precio por noche actualizado para habitación ID: {}", id);
        }

        return ResponseEntity.ok(habitacion);
    }
}