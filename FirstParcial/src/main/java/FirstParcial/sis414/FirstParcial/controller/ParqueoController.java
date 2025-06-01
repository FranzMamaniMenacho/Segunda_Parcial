package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Parqueo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins ="http://localhost")
@RestController
@RequestMapping("/parqueos")
public class ParqueoController {

    private static final Logger logger = LoggerFactory.getLogger(ParqueoController.class);
    private List<Parqueo> parqueos = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Parqueo>> getAllParqueos() {
        logger.info("Solicitando lista de parqueos.");
        return ResponseEntity.ok(parqueos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getParqueoById(@PathVariable Long id) {
        logger.info("Buscando parqueo con ID: {}", id);
        Optional<Parqueo> parqueo = parqueos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (parqueo.isPresent()) {
            return ResponseEntity.ok(parqueo.get());
        } else {
            logger.warn("Parqueo con ID {} no encontrado", id);
            return ResponseEntity.status(404).body("Parqueo no encontrado.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createParqueo(@RequestBody Parqueo parqueo) {
        if (parqueo.getId() == null) {
            logger.error("Intento de crear parqueo sin ID");
            return ResponseEntity.status(400).body("Se requiere ID para crear un parqueo");
        }

        if (parqueos.stream().anyMatch(p -> p.getId().equals(parqueo.getId()))) {
            logger.error("Ya existe un parqueo con ID: {}", parqueo.getId());
            return ResponseEntity.status(400).body("Ya existe un parqueo con este ID");
        }

        parqueos.add(parqueo);
        logger.info("Nuevo parqueo creado con ID: {}", parqueo.getId());
        return ResponseEntity.ok(parqueo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateParqueo(@PathVariable Long id, @RequestBody Parqueo parqueoDetails) {
        for (Parqueo parqueo : parqueos) {
            if (parqueo.getId().equals(id)) {
                parqueo.setEstado(parqueoDetails.getEstado());
                parqueo.setPrecioPorNoche(parqueoDetails.getPrecioPorNoche());
                parqueo.setMarca(parqueoDetails.getMarca());
                parqueo.setColor(parqueoDetails.getColor());
                parqueo.setPlaca(parqueoDetails.getPlaca());

                logger.info("Parqueo con ID {} actualizado completamente", id);
                return ResponseEntity.ok(parqueo);
            }
        }

        logger.warn("No se encontró parqueo con ID {} para actualizar", id);
        return ResponseEntity.status(404).body("Parqueo no encontrado para actualización");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParqueo(@PathVariable Long id) {
        boolean removed = parqueos.removeIf(p -> p.getId().equals(id));
        if (removed) {
            logger.info("Parqueo con ID {} eliminado exitosamente", id);
            return ResponseEntity.ok("Parqueo eliminado con éxito");
        } else {
            logger.warn("Intento de eliminar parqueo no existente con ID: {}", id);
            return ResponseEntity.status(404).body("Parqueo no encontrado para eliminar");
        }
    }
}