package se2.alpha.riskappbackend.service;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import se2.alpha.riskappbackend.entity.User;
import se2.alpha.riskappbackend.entity.Role;

import se2.alpha.riskappbackend.model.JwtAuthenticationResponse;
import se2.alpha.riskappbackend.model.SignInRequest;
import se2.alpha.riskappbackend.model.SignUpRequest;

import se2.alpha.riskappbackend.repository.UserRepository;

import se2.alpha.riskappbackend.util.JwtTokenUtil;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtjwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);
        var jwt = jwtjwtTokenUtil.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        var jwt = jwtjwtTokenUtil.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}