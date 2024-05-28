package se.alpha.riskappbackend.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import se.alpha.riskappbackend.controller.AuthenticationController;
import se.alpha.riskappbackend.model.auth.JwtAuthenticationResponse;
import se.alpha.riskappbackend.model.auth.SignInRequest;
import se.alpha.riskappbackend.model.auth.SignUpRequest;
import se.alpha.riskappbackend.service.AuthenticationService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;
    private final String userPayload = "{\"username\":\"user\",\"password\":\"pass\"}";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    @DisplayName("Sign Up Success")
    void testSignUpSuccess() throws Exception {
        given(authenticationService.signup(any(SignUpRequest.class)))
                .willReturn(new JwtAuthenticationResponse("token"));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @DisplayName("Sign Up Failure - Username Already Taken")
    void testSignUpFailureUsernameTaken() throws Exception {
        given(authenticationService.signup(any(SignUpRequest.class)))
                .willThrow(new DataIntegrityViolationException("Username already taken!"));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(jsonPath("$").value("Username already taken!"));
    }

    @Test
    @DisplayName("Sign In Success")
    void testSignInSuccess() throws Exception {
        given(authenticationService.signin(any(SignInRequest.class)))
                .willReturn(new JwtAuthenticationResponse("token"));

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @DisplayName("Sign In Failure - Wrong Credentials")
    void testSignInFailureWrongCredentials() throws Exception {
        given(authenticationService.signin(any(SignInRequest.class)))
                .willThrow(new RuntimeException("Wrong Credentials!"));

        mockMvc.perform(post("/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(jsonPath("$").value("Wrong Credentials!"));
    }

    @Test
    @DisplayName("Validate Token Success")
    void testValidateTokenSuccess() throws Exception {
        // Assuming `validateToken` method returns boolean or some success indication
        given(authenticationService.validateToken(any()))
                .willReturn(true); // Mock successful validation

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"validToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @DisplayName("Validate Token Failure")
    void testValidateTokenFailure() throws Exception {
        // Assuming handling of token validation failure
        given(authenticationService.validateToken(any()))
                .willThrow(new RuntimeException("Invalid Token!"));

        mockMvc.perform(post("/auth/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"invalidToken\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Token!"));
    }

}

