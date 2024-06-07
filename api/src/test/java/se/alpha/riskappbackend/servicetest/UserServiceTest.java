package se.alpha.riskappbackend.servicetest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import se.alpha.riskappbackend.entity.User;
import se.alpha.riskappbackend.repository.UserRepository;
import se.alpha.riskappbackend.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

        @InjectMocks
        private UserService userService;

        @Mock
        private UserRepository userRepository;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testSaveUser() {
            User user = new User();
            user.setId(1);
            user.setUsername("testUser");

            when(userRepository.save(user)).thenReturn(user);

            User savedUser = userService.saveUser(user);
            assertNotNull(savedUser);
            assertEquals(1, savedUser.getId());
            assertEquals("testUser", savedUser.getUsername());
        }

        @Test
        public void testGetUserById() {
            User user = new User();
            user.setId(1);
            user.setUsername("testUser");

            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            Optional<User> retrievedUser = userService.getUserById(1L);
            assertTrue(retrievedUser.isPresent());
            assertEquals("testUser", retrievedUser.get().getUsername());
        }

        @Test
        public void testGetAllUsers() {
            User user1 = new User();
            user1.setId(1);
            user1.setUsername("testUser1");

            User user2 = new User();
            user2.setId(2);
            user2.setUsername("testUser2");

            List<User> users = Arrays.asList(user1, user2);
            when(userRepository.findAll()).thenReturn(users);

            List<User> retrievedUsers = userService.getAllUsers();
            assertEquals(2, retrievedUsers.size());
            assertEquals("testUser1", retrievedUsers.get(0).getUsername());
            assertEquals("testUser2", retrievedUsers.get(1).getUsername());
        }

        @Test
        public void testDeleteUser() {
            Long userId = 1L;
            doNothing().when(userRepository).deleteById(userId);

            userService.deleteUser(userId.intValue());
            verify(userRepository, times(1)).deleteById(userId);
        }

        @Test
        public void testUserDetailsService() {
            User user = new User();
            user.setUsername("testUser");
            user.setPassword("password");

            when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

            UserDetailsService userDetailsService = userService.userDetailsService();
            assertNotNull(userDetailsService);

            org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");
            assertNotNull(userDetails);
            assertEquals("testUser", userDetails.getUsername());
        }

        @Test
        public void testUserDetailsService_UserNotFound() {
            when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

            UserDetailsService userDetailsService = userService.userDetailsService();
            assertThrows(UsernameNotFoundException.class, () -> {
                userDetailsService.loadUserByUsername("unknownUser");
            });
        }
    }


