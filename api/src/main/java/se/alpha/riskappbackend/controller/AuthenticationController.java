package se.alpha.riskappbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.alpha.riskappbackend.model.auth.SignInRequest;
import se.alpha.riskappbackend.model.auth.SignUpRequest;
import se.alpha.riskappbackend.model.auth.ValidateTokenRequest;
import se.alpha.riskappbackend.service.AuthenticationService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request) {
        try {
            var signupResponse = authenticationService.signup(request);
            return ResponseEntity.ok(signupResponse);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Username already taken!");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {
        try {
            var signinResponse = authenticationService.signin(request);
            return ResponseEntity.ok(signinResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Wrong Credentials!");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody ValidateTokenRequest request) {
        try {
            var validated = authenticationService.validateToken(request);
            return ResponseEntity.ok(validated);
        } catch (Exception e) {
            // Modify this part to return a structured error message
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid Token!"));
        }
    }

}