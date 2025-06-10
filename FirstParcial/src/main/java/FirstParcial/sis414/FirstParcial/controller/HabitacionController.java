package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Habitacion;
import FirstParcial.sis414.FirstParcial.repository.HabitacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    private static final Logger logger = LoggerFactory.getLogger(HabitacionController.class);
    private final HabitacionRepository habitacionRepository;

    public HabitacionController(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    @GetMapping
    public ResponseEntity<List<Habitacion>> getAllHabitaciones() {
        logger.info("Obteniendo lista completa de habitaciones");
        return ResponseEntity.ok(habitacionRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habitacion> getClienteById(@PathVariable Long id) {
        return habitacionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createHabitacion(@RequestBody Habitacion habitacion) {
        logger.info("Creando nueva habitación");
        return ResponseEntity.ok(habitacionRepository.save(habitacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHabitacion(@PathVariable Long id, @RequestBody Habitacion habitacionDetails) {
        Optional<Habitacion> optional = habitacionRepository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("No se encontró habitación con ID {} para actualizar", id);
            return ResponseEntity.status(404).body("Habitación no encontrada");
        }

        Habitacion habitacion = optional.get();
        habitacion.setTipoHabitacion(habitacionDetails.getTipoHabitacion());
        habitacion.setEstado(habitacionDetails.getEstado());
        habitacion.setPrecioNoche(habitacionDetails.getPrecioNoche());

        logger.info("Habitación con ID {} actualizada completamente", id);
        return ResponseEntity.ok(habitacionRepository.save(habitacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHabitacion(@PathVariable Long id) {
        if (!habitacionRepository.existsById(id)) {
            logger.warn("Habitación con ID {} no encontrada para eliminar", id);
            return ResponseEntity.status(404).body("Habitación no encontrada");
        }

        habitacionRepository.deleteById(id);
        logger.info("Habitación con ID {} eliminada exitosamente", id);
        return ResponseEntity.ok("Habitación eliminada con éxito");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateHabitacion(@PathVariable Long id, @RequestBody Habitacion partialHabitacion) {
        Optional<Habitacion> optional = habitacionRepository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("Habitación con ID {} no encontrada para actualización parcial", id);
            return ResponseEntity.status(404).body("Habitación no encontrada");
        }

        Habitacion habitacion = optional.get();

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

        return ResponseEntity.ok(habitacionRepository.save(habitacion));
    }
}
