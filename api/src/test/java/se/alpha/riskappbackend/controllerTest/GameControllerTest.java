package se.alpha.riskappbackend.controllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import se.alpha.riskappbackend.controller.GameController;
import se.alpha.riskappbackend.model.game.GameSession;
import se.alpha.riskappbackend.service.GameService;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    @DisplayName("Get Active Lobbies - Success")
    void getActiveLobbies_Success() throws Exception {
        GameSession gameSession = new GameSession("Test Lobby");
        when(gameService.getJoinableSessions()).thenReturn(List.of(gameSession));

        mockMvc.perform(get("/game/lobby"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Lobby"));
    }

    @Test
    @DisplayName("Create Lobby - Success")
    void createLobby_Success() throws Exception {
        UUID sessionId = UUID.randomUUID();
        when(gameService.createNewSession(any(String.class))).thenReturn(sessionId);

        mockMvc.perform(post("/game/lobby")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"lobbyName\":\"New Lobby\"}"))
                .andExpect(status().isOk())
                .andExpect(result -> System.out.println("Response: " + result.getResponse().getContentAsString())) // This line adds logging
                .andExpect(jsonPath("$.gameSessionId").value(sessionId.toString()));

    }
    @Test
    @DisplayName("Get Active Lobbies - No Active Lobbies")
    void getActiveLobbies_NoActiveLobbies() throws Exception {
        when(gameService.getJoinableSessions()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/game/lobby"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }





}