package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Personal;
import FirstParcial.sis414.FirstParcial.repository.PersonalRepository;
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
@RequestMapping("/personal")
public class PersonalController {

    private static final Logger logger = LoggerFactory.getLogger(PersonalController.class);
    private final PersonalRepository personalRepository;

    public PersonalController(PersonalRepository personalRepository) {
        this.personalRepository = personalRepository;
    }

    @GetMapping
    public ResponseEntity<List<Personal>> getAllPersonal() {
        logger.info("Obteniendo lista completa del personal");
        return ResponseEntity.ok(personalRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Personal> getClienteById(@PathVariable Long id) {
        return personalRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createPersonal(@RequestBody Personal personal) {
        logger.info("Creando nuevo personal");
        return ResponseEntity.ok(personalRepository.save(personal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePersonal(@PathVariable Long id, @RequestBody Personal personalDetails) {
        Optional<Personal> optional = personalRepository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("Personal con ID {} no encontrado para actualizar", id);
            return ResponseEntity.status(404).body("Personal no encontrado");
        }

        Personal personal = optional.get();

        personal.setNombre(personalDetails.getNombre());
        personal.setRol(personalDetails.getRol());


        logger.info("Personal con ID {} actualizado completamente", id);
        return ResponseEntity.ok(personalRepository.save(personal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonal(@PathVariable Long id) {
        if (!personalRepository.existsById(id)) {
            logger.warn("Personal con ID {} no encontrado para eliminar", id);
            return ResponseEntity.status(404).body("Personal no encontrado");
        }

        personalRepository.deleteById(id);
        logger.info("Personal con ID {} eliminado exitosamente", id);
        return ResponseEntity.ok("Personal eliminado exitosamente");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdatePersonal(@PathVariable Long id, @RequestBody Personal updates) {
        Optional<Personal> optional = personalRepository.findById(id);
        if (optional.isEmpty()) {
            logger.warn("Personal con ID {} no encontrado para actualización parcial", id);
            return ResponseEntity.status(404).body("Personal no encontrado");
        }

        Personal personal = optional.get();

        if (updates.getNombre() != null) {
            personal.setNombre(updates.getNombre());
            logger.info("Nombre actualizado para personal ID: {}", id);
        }

        if (updates.getRol() != null) {
            personal.setRol(updates.getRol());
            logger.info("Cargo actualizado para personal ID: {}", id);
        }

        return ResponseEntity.ok(personalRepository.save(personal));
    }
}
