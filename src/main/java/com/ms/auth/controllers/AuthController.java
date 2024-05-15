package com.ms.auth.controllers;

import com.ms.auth.dto.*;
import com.ms.auth.model.User;
import com.ms.auth.repository.UserRepository;
import com.ms.auth.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;



    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
       User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));

        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            UserDTO userDTO = new UserDTO(user);
            return ResponseEntity.ok(new ResponseDTO(userDTO));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/loginAdmin")
    public ResponseEntity loginAdmin(@RequestBody LoginRequestDTO body) {
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword()) && Constants.ROLE_ADMIN.equals(user.getRole())) {
            UserDTO userDTO = new UserDTO(user);
            return ResponseEntity.ok(new ResponseDTO(userDTO));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO body){
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            newUser.setUsername(body.username());
            this.repository.save(newUser);


            return ResponseEntity.ok(new ResponseDTO(new UserDTO(newUser)));
        } else {
        return ResponseEntity.badRequest().body("Email já cadastrado.");
    }
        }
}
