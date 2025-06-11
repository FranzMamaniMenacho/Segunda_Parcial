package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.entity.user;
import FirstParcial.sis414.FirstParcial.repository.UserRepository;
import FirstParcial.sis414.FirstParcial.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost")
@RestController
@RequestMapping("/admin")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody user userRequest) {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();

        Optional<user> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(password)) {
            return ResponseEntity.status(403).body("Usuario o contraseña incorrectos");
        }

        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Sesión cerrada");
    }
}

