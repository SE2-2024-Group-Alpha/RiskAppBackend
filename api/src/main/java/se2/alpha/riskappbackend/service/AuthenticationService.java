package se2.alpha.riskappbackend.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import se2.alpha.riskappbackend.entity.User;
import se2.alpha.riskappbackend.entity.Role;

import se2.alpha.riskappbackend.model.auth.JwtAuthenticationResponse;
import se2.alpha.riskappbackend.model.auth.SignInRequest;
import se2.alpha.riskappbackend.model.auth.SignUpRequest;

import se2.alpha.riskappbackend.model.auth.ValidateTokenRequest;
import se2.alpha.riskappbackend.repository.UserRepository;

import se2.alpha.riskappbackend.util.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);
        var jwt = jwtTokenUtil.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtTokenUtil.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public boolean validateToken(ValidateTokenRequest request) {
        try {
            String token = request.getToken();
            String username = jwtTokenUtil.extractUserName(token);

            UserDetails userDetails = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            return jwtTokenUtil.validateToken(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }
}