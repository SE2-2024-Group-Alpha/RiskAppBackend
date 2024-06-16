package se.alpha.riskappbackend.servicetest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import se.alpha.riskappbackend.entity.Role;
import se.alpha.riskappbackend.entity.User;
import se.alpha.riskappbackend.repository.UserRepository;
import se.alpha.riskappbackend.model.auth.JwtAuthenticationResponse;
import se.alpha.riskappbackend.model.auth.SignInRequest;
import se.alpha.riskappbackend.model.auth.SignUpRequest;
import se.alpha.riskappbackend.model.auth.ValidateTokenRequest;
import se.alpha.riskappbackend.service.AuthenticationService;
import se.alpha.riskappbackend.util.JwtTokenUtil;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignup() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(User.builder().username("testuser").password("encodedPassword").role(Role.USER).build());
        when(jwtTokenUtil.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtAuthenticationResponse response = authenticationService.signup(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testSignin() {
        SignInRequest request = new SignInRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        User user = User.builder().username("testuser").password("encodedPassword").role(Role.USER).build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtTokenUtil.generateToken(any(User.class))).thenReturn("jwtToken");

        JwtAuthenticationResponse response = authenticationService.signin(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testValidateToken() {
        ValidateTokenRequest request = new ValidateTokenRequest();
        request.setToken("jwtToken");

        User user = User.builder().username("testuser").password("encodedPassword").role(Role.USER).build();

        when(jwtTokenUtil.extractUserName(anyString())).thenReturn("testuser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtTokenUtil.validateToken(anyString(), any(UserDetails.class))).thenReturn(true);

        boolean isValid = authenticationService.validateToken(request);

        assertTrue(isValid);
        verify(jwtTokenUtil, times(1)).validateToken(anyString(), any(UserDetails.class));
    }
}

