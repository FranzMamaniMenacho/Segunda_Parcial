package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Personal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins ="http://localhost")
@RestController
@RequestMapping("/personal")
public class PersonalController {

    private static final Logger logger = LoggerFactory.getLogger(PersonalController.class);
    private final List<Personal> personalList = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Personal>> getAllPersonal() {
        logger.info("Obteniendo listado completo de personal");
        return ResponseEntity.ok(personalList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonalById(@PathVariable Long id) {
        logger.info("Consultando personal con ID: {}", id);
        Optional<Personal> personal = personalList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (personal.isPresent()) {
            return ResponseEntity.ok(personal.get());
        } else {
            logger.warn("Personal con ID {} no encontrado", id);
            return ResponseEntity.status(404).body("Personal no encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<?> createPersonal(@RequestBody Personal personal) {
        if (personal.getId() == null) {
            logger.error("Error: ID es requerido para crear personal");
            return ResponseEntity.badRequest().body("El ID es requerido para crear un registro de personal");
        }

        if (personalList.stream().anyMatch(p -> p.getId().equals(personal.getId()))) {
            logger.error("Error: Personal con ID {} ya existe", personal.getId());
            return ResponseEntity.badRequest().body("El ID del personal ya está registrado");
        }

        personalList.add(personal);
        logger.info("Personal creado exitosamente con ID: {}", personal.getId());
        return ResponseEntity.ok(personal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePersonal(@PathVariable Long id, @RequestBody Personal personal) {
        Optional<Personal> existingPersonal = personalList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (existingPersonal.isEmpty()) {
            logger.warn("No existe personal con ID {} para actualizar", id);
            return ResponseEntity.status(404).body("Personal no encontrado");
        }

        Personal toUpdate = existingPersonal.get();
        toUpdate.setNombre(personal.getNombre());
        toUpdate.setRol(personal.getRol());
        toUpdate.setCI(personal.getCI());

        logger.info("Personal con ID {} actualizado exitosamente", id);
        return ResponseEntity.ok(toUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonal(@PathVariable Long id) {
        boolean removed = personalList.removeIf(p -> p.getId().equals(id));
        if (removed) {
            logger.info("Personal con ID {} eliminado correctamente", id);
            return ResponseEntity.ok("Personal eliminado exitosamente");
        }
        logger.warn("No se encontró personal con ID {} para eliminar", id);
        return ResponseEntity.status(404).body("Personal no encontrado");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdatePersonal(@PathVariable Long id, @RequestBody Personal updates) {
        Optional<Personal> personalOpt = personalList.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (personalOpt.isEmpty()) {
            logger.warn("Personal con ID {} no encontrado para actualización", id);
            return ResponseEntity.status(404).body("Personal no encontrado");
        }

        Personal personal = personalOpt.get();

        if (updates.getNombre() != null) {
            personal.setNombre(updates.getNombre());
            logger.info("Nombre actualizado para personal ID: {}", id);
        }
        if (updates.getRol() != null) {
            personal.setRol(updates.getRol());
            logger.info("Rol actualizado para personal ID: {}", id);
        }
        if (updates.getCI() != null) {
            personal.setCI(updates.getCI());
            logger.info("ID de empleado actualizado para personal ID: {}", id);
        }

        logger.info("Personal con ID {} actualizado parcialmente", id);
        return ResponseEntity.ok(personal);
    }
}