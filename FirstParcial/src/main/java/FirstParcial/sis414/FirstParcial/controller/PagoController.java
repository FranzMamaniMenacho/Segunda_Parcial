package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Pago;
import FirstParcial.sis414.FirstParcial.repository.PagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/pagos")
public class PagoController {

    private static final Logger logger = LoggerFactory.getLogger(PagoController.class);
    private final PagoRepository pagoRepository;

    public PagoController(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Pago>> getAllPagos() {
        logger.info("Obteniendo lista completa de pagos");
        return ResponseEntity.ok(pagoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> getClienteById(@PathVariable Long id) {
        return pagoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createPago(@RequestBody Pago pago) {
        logger.info("Creando nuevo pago");
        return ResponseEntity.ok(pagoRepository.save(pago));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePago(@PathVariable Long id, @RequestBody Pago pagoDetails) {
        Optional<Pago> optionalPago = pagoRepository.findById(id);
        if (optionalPago.isEmpty()) {
            logger.warn("Pago con ID {} no encontrado para actualizar", id);
            return ResponseEntity.status(404).body("Pago no encontrado");
        }

        Pago pago = optionalPago.get();
        pago.setMonto(pagoDetails.getMonto());
        pago.setDate(pagoDetails.getDate());
        pago.setDate(pagoDetails.getDate());
        pago.setEstado(pagoDetails.getEstado());

        logger.info("Pago con ID {} actualizado completamente", id);
        return ResponseEntity.ok(pagoRepository.save(pago));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePago(@PathVariable Long id) {
        if (!pagoRepository.existsById(id)) {
            logger.warn("Pago con ID {} no encontrado para eliminar", id);
            return ResponseEntity.status(404).body("Pago no encontrado");
        }

        pagoRepository.deleteById(id);
        logger.info("Pago con ID {} eliminado exitosamente", id);
        return ResponseEntity.ok("Pago eliminado exitosamente");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdatePago(@PathVariable Long id, @RequestBody Pago updates) {
        Optional<Pago> optionalPago = pagoRepository.findById(id);
        if (optionalPago.isEmpty()) {
            logger.warn("Pago con ID {} no encontrado para actualización parcial", id);
            return ResponseEntity.status(404).body("Pago no encontrado");
        }

        Pago pago = optionalPago.get();

        if (updates.getMonto() != 0) {
            pago.setMonto(updates.getMonto());
            logger.info("Monto actualizado para pago ID: {}", id);
        }
        if (updates.getDate() != null) {
            pago.setDate(updates.getDate());
            logger.info("Fecha de pago actualizada para pago ID: {}", id);
        }
        if (updates.getMetodo() != null) {
            pago.setMetodo(updates.getMetodo());
            logger.info("Método de pago actualizado para pago ID: {}", id);
        }
        if (updates.getEstado() != null) {
            pago.setEstado(updates.getEstado());
            logger.info("Estado actualizado para pago ID: {}", id);
        }

        return ResponseEntity.ok(pagoRepository.save(pago));
    }
}
