package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Parqueo;
import FirstParcial.sis414.FirstParcial.repository.ParqueoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/parqueos")
public class ParqueoController {

    private static final Logger logger = LoggerFactory.getLogger(ParqueoController.class);
    private final ParqueoRepository parqueoRepository;

    public ParqueoController(ParqueoRepository parqueoRepository) {
        this.parqueoRepository = parqueoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Parqueo>> getAllParqueos() {
        logger.info("Obteniendo lista completa de parqueos");
        return ResponseEntity.ok(parqueoRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Parqueo> getClienteById(@PathVariable Long id) {
        return parqueoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createParqueo(@RequestBody Parqueo parqueo) {
        logger.info("Creando nuevo parqueo");
        return ResponseEntity.ok(parqueoRepository.save(parqueo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateParqueo(@PathVariable Long id, @RequestBody Parqueo parqueoDetails) {
        Optional<Parqueo> optional = parqueoRepository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("Parqueo con ID {} no encontrado para actualizar", id);
            return ResponseEntity.status(404).body("Parqueo no encontrado");
        }

        Parqueo parqueo = optional.get();

        parqueo.setEstado(parqueoDetails.getEstado());
        parqueo.setPrecioPorNoche(parqueoDetails.getPrecioPorNoche());
        parqueo.setMarca(parqueoDetails.getMarca());
        parqueo.setColor(parqueoDetails.getColor());
        parqueo.setPlaca(parqueoDetails.getPlaca());

        logger.info("Parqueo con ID {} actualizado completamente", id);
        return ResponseEntity.ok(parqueoRepository.save(parqueo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParqueo(@PathVariable Long id) {
        if (!parqueoRepository.existsById(id)) {
            logger.warn("Parqueo con ID {} no encontrado para eliminar", id);
            return ResponseEntity.status(404).body("Parqueo no encontrado");
        }

        parqueoRepository.deleteById(id);
        logger.info("Parqueo con ID {} eliminado exitosamente", id);
        return ResponseEntity.ok("Parqueo eliminado exitosamente");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateParqueo(@PathVariable Long id, @RequestBody Parqueo updates) {
        Optional<Parqueo> optional = parqueoRepository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("Parqueo con ID {} no encontrado para actualización parcial", id);
            return ResponseEntity.status(404).body("Parqueo no encontrado");
        }

        Parqueo parqueo = optional.get();

        if (updates.getEstado() != null) {
            parqueo.setEstado(updates.getEstado());
            logger.info("Estado actualizado para parqueo ID: {}", id);
        }
        if (updates.getPrecioPorNoche() > 0) {
            parqueo.setPrecioPorNoche(updates.getPrecioPorNoche());
            logger.info("Precio por noche actualizado para parqueo ID: {}", id);
        }
        if (updates.getMarca() != null) {
            parqueo.setMarca(updates.getMarca());
            logger.info("Marca actualizada para parqueo ID: {}", id);
        }
        if (updates.getColor() != null) {
            parqueo.setColor(updates.getColor());
            logger.info("Color actualizado para parqueo ID: {}", id);
        }
        if (updates.getPlaca() != null) {
            parqueo.setPlaca(updates.getPlaca());
            logger.info("Placa actualizada para parqueo ID: {}", id);
        }

        return ResponseEntity.ok(parqueoRepository.save(parqueo));
    }
}
