package FirstParcial.sis414.FirstParcial.controller;

import FirstParcial.sis414.FirstParcial.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public String login(@RequestBody User userRequest)
    {
        if(userRequest.getUsername().equals("admin"))
        {
            return jwtUtil.generateToken(userRequest.getUsername());
        }
        else
        {
            return "No exite usuario";
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout()
    {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("work");
    }
}