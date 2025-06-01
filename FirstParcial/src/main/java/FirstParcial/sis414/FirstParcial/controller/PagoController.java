package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Pago;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/pagos")
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);
    private final List<Pago> pagos = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Pago>> getAllPagos() {
        logger.info("Obteniendo listado completo de pagos");
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPagoById(@PathVariable String id) {
        logger.info("Consultando pago con ID: {}", id);
        Optional<Pago> pago = pagos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (pago.isPresent()) {
            return ResponseEntity.ok(pago.get());
        } else {
            logger.warn("Pago con ID {} no encontrado", id);
            return ResponseEntity.status(404).body("Pago no encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<?> createPago(@RequestBody Pago pago) {
        if (pago.getId() == null || pago.getId().isEmpty()) {
            logger.error("Error: ID es requerido para crear pago");
            return ResponseEntity.badRequest().body("El ID es requerido para crear un pago");
        }

        if (pagos.stream().anyMatch(p -> p.getId().equals(pago.getId()))) {
            logger.error("Error: Pago con ID {} ya existe", pago.getId());
            return ResponseEntity.badRequest().body("El ID del pago ya está registrado");
        }

        if (pago.getMonto() <= 0) {
            return ResponseEntity.badRequest().body("El monto debe ser positivo");
        }

        pagos.add(pago);
        logger.info("Pago creado exitosamente con ID: {}", pago.getId());
        return ResponseEntity.ok(pago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePago(@PathVariable String id, @RequestBody Pago pago) {
        if (!id.equals(pago.getId())) {
            logger.error("Discrepancia en IDs: path {} vs body {}", id, pago.getId());
            return ResponseEntity.badRequest().body("ID en URL no coincide con ID en body");
        }

        Optional<Pago> existingPago = pagos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (existingPago.isEmpty()) {
            logger.warn("No existe pago con ID {} para actualizar", id);
            return ResponseEntity.status(404).body("Pago no encontrado");
        }

        if (pago.getMonto() <= 0) {
            return ResponseEntity.badRequest().body("El monto debe ser positivo");
        }

        Pago toUpdate = existingPago.get();
        toUpdate.setMetodo(pago.getMetodo());
        toUpdate.setEstado(pago.getEstado());
        toUpdate.setMonto(pago.getMonto());
        toUpdate.setDate(pago.getDate());

        logger.info("Pago con ID {} actualizado exitosamente", id);
        return ResponseEntity.ok(toUpdate);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdatePago(@PathVariable String id, @RequestBody Map<String, Object> updates) {
        Optional<Pago> pagoOpt = pagos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (pagoOpt.isEmpty()) {
            logger.warn("Pago con ID {} no encontrado para actualización", id);
            return ResponseEntity.status(404).body("Pago no encontrado");
        }

        Pago pago = pagoOpt.get();
        boolean hasUpdates = false;

        if (updates.containsKey("metodo") && updates.get("metodo") != null) {
            pago.setMetodo((String) updates.get("metodo"));
            hasUpdates = true;
            logger.info("Método actualizado para pago ID: {}", id);
        }

        if (updates.containsKey("estado") && updates.get("estado") != null) {
            pago.setEstado((String) updates.get("estado"));
            hasUpdates = true;
            logger.info("Estado actualizado para pago ID: {}", id);
        }

        if (updates.containsKey("monto") && updates.get("monto") != null) {
            double monto = ((Number) updates.get("monto")).doubleValue();
            if (monto >= 0) {
                pago.setMonto(monto);
                hasUpdates = true;
                logger.info("Monto actualizado para pago ID: {}", id);
            }
        }

        if (updates.containsKey("date") && updates.get("date") != null) {
            try {
                String dateStr = (String) updates.get("date");
                LocalDate parsedDate = LocalDate.parse(dateStr);
                pago.setDate(parsedDate);
                hasUpdates = true;
                logger.info("Fecha actualizada para pago ID: {}", id);
            } catch (Exception e) {
                logger.error("Error al parsear fecha: {}", e.getMessage());
                return ResponseEntity.badRequest().body("Formato de fecha inválido, debe ser yyyy-MM-dd");
            }
        }

        if (!hasUpdates) {
            return ResponseEntity.badRequest().body("No se proporcionaron campos válidos para actualizar");
        }

        logger.info("Pago con ID {} actualizado parcialmente", id);
        return ResponseEntity.ok(pago);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePago(@PathVariable String id) {
        boolean removed = pagos.removeIf(p -> p.getId().equals(id));
        if (removed) {
            logger.info("Pago con ID {} eliminado correctamente", id);
            return ResponseEntity.ok("Pago eliminado exitosamente");
        }
        logger.warn("No se encontró pago con ID {} para eliminar", id);
        return ResponseEntity.status(404).body("Pago no encontrado");
    }
}
