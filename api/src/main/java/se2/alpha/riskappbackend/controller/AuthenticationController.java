package se2.alpha.riskappbackend.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se2.alpha.riskappbackend.model.JwtAuthenticationResponse;
import se2.alpha.riskappbackend.model.SignInRequest;
import se2.alpha.riskappbackend.model.SignUpRequest;
import se2.alpha.riskappbackend.service.AuthenticationService;

import java.sql.SQLIntegrityConstraintViolationException;

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
}