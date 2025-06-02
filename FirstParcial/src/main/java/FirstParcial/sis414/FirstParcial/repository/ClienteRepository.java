package FirstParcial.sis414.FirstParcial.repository;

import FirstParcial.sis414.FirstParcial.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
