package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Cliente;
import FirstParcial.sis414.FirstParcial.repository.ClienteRepository;
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
@RequestMapping("/clientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        logger.info("Obteniendo listado completo de clientes");
        return ResponseEntity.ok(clienteRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
        logger.info("Creando cliente nuevo");
        return ResponseEntity.ok(clienteRepository.save(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Optional<Cliente> existingCliente = clienteRepository.findById(id);
        if (existingCliente.isEmpty()) {
            logger.warn("Cliente con ID {} no encontrado", id);
            return ResponseEntity.status(404).body("Cliente no encontrado");
        }

        Cliente toUpdate = existingCliente.get();
        toUpdate.setNombres(cliente.getNombres());
        toUpdate.setApellidos(cliente.getApellidos());
        toUpdate.setCi(cliente.getCi());
        toUpdate.setTelefono(cliente.getTelefono());

        logger.info("Cliente con ID {} actualizado exitosamente", id);
        return ResponseEntity.ok(clienteRepository.save(toUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            logger.warn("Cliente con ID {} no encontrado", id);
            return ResponseEntity.status(404).body("Cliente no encontrado");
        }

        clienteRepository.deleteById(id);
        logger.info("Cliente con ID {} eliminado correctamente", id);
        return ResponseEntity.ok("Cliente eliminado exitosamente");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateCliente(@PathVariable Long id, @RequestBody Cliente updates) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isEmpty()) {
            logger.warn("Cliente con ID {} no encontrado", id);
            return ResponseEntity.status(404).body("Cliente no encontrado");
        }

        Cliente cliente = clienteOpt.get();

        if (updates.getNombres() != null) {
            cliente.setNombres(updates.getNombres());
            logger.info("Nombres actualizados para cliente ID: {}", id);
        }
        if (updates.getApellidos() != null) {
            cliente.setApellidos(updates.getApellidos());
            logger.info("Apellidos actualizados para cliente ID: {}", id);
        }
        if (updates.getCi() != null) {
            cliente.setCi(updates.getCi());
            logger.info("CI actualizado para cliente ID: {}", id);
        }
        if (updates.getTelefono() != null) {
            cliente.setTelefono(updates.getTelefono());
            logger.info("Teléfono actualizado para cliente ID: {}", id);
        }

        return ResponseEntity.ok(clienteRepository.save(cliente));
    }
}
