package se.alpha.riskappbackend.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;

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

import java.util.Arrays;

import se.alpha.riskappbackend.controller.UserController;
import se.alpha.riskappbackend.entity.Role;
import se.alpha.riskappbackend.entity.User;
import se.alpha.riskappbackend.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("Create User - Success")
    void createUser_Success() throws Exception {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .role(Role.USER)
                .score(0)
                .wins(0)
                .losses(0)
                .gamesPlayed(0)
                .build();
        when(userService.saveUser(user)).thenReturn(user);

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("Get all Users - Success")
    void getAllUsers_Success() throws Exception {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .role(Role.USER)
                .score(0)
                .build();
        User user2 = User.builder()
                .username("testuser2")
                .password("password123")
                .role(Role.USER)
                .score(0)
                .build();
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user, user2));

        mockMvc.perform(get("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[1].username").value("testuser2"));
    }

    @Test
    @DisplayName("Delete all Users - Success")
    void deleteAllUsers_Success() throws Exception {
        User user = User.builder()
                .username("testuser")
                .password("password123")
                .role(Role.USER)
                .score(0)
                .build();
        userService.deleteUser(any(Integer.class));

        mockMvc.perform(delete("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(user)))
                .andExpect(status().isOk());
    }
}
