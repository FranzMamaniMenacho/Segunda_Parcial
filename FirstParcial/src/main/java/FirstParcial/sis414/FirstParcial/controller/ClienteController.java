package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins ="http://localhost")

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);
    private final List<Cliente> clientes = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Cliente>> getAllClientes() {
        logger.info("Obteniendo listado completo de clientes");
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable Long id) {
        logger.info("Consultando cliente con ID: {}", id);
        Optional<Cliente> cliente = clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        } else {
            logger.warn("Cliente con ID {} no encontrado", id);
            return ResponseEntity.status(404).body("Cliente no encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente) {
        if (cliente.getId() == null) {
            logger.error("Error: ID es requerido para crear cliente");
            return ResponseEntity.badRequest().body("El ID es requerido para crear un cliente");
        }

        if (clientes.stream().anyMatch(c -> c.getId().equals(cliente.getId()))) {
            logger.error("Error: Cliente con ID {} ya existe", cliente.getId());
            return ResponseEntity.badRequest().body("El ID del cliente ya está registrado");
        }

        clientes.add(cliente);
        logger.info("Cliente creado exitosamente con ID: {}", cliente.getId());
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        Optional<Cliente> existingCliente = clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (existingCliente.isEmpty()) {
            logger.warn("No existe cliente con ID {} para actualizar", id);
            return ResponseEntity.status(404).body("Cliente no encontrado");
        }

        Cliente toUpdate = existingCliente.get();
        toUpdate.setNombres(cliente.getNombres());
        toUpdate.setApellidos(cliente.getApellidos());
        toUpdate.setCi(cliente.getCi());
        toUpdate.setTelefono(cliente.getTelefono());

        logger.info("Cliente con ID {} actualizado exitosamente", id);
        return ResponseEntity.ok(toUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        boolean removed = clientes.removeIf(c -> c.getId().equals(id));
        if (removed) {
            logger.info("Cliente con ID {} eliminado correctamente", id);
            return ResponseEntity.ok("Cliente eliminado exitosamente");
        }
        logger.warn("No se encontró cliente con ID {} para eliminar", id);
        return ResponseEntity.status(404).body("Cliente no encontrado");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> partialUpdateCliente(@PathVariable Long id, @RequestBody Cliente updates) {
        Optional<Cliente> clienteOpt = clientes.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

        if (clienteOpt.isEmpty()) {
            logger.warn("Cliente con ID {} no encontrado para actualización", id);
            return ResponseEntity.status(404).body("Cliente no encontrado");
        }

        Cliente cliente = clienteOpt.get();

        if (updates.getNombres() != null) {
            cliente.setNombres(updates.getNombres());
            logger.
            info("Nombres actualizados para cliente ID: {}", id);
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

        logger.info("Cliente con ID {} actualizado parcialmente", id);
        return ResponseEntity.ok(cliente);
    }
}