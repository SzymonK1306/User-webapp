package com.example.userapp;

import com.example.userapp.controller.UserController;
import com.example.userapp.model.User;
import com.example.userapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User("Jan Kowalski", "male", 30);
        User user2 = new User("Anna Nowak", "female", 25);
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        assertEquals(2, userController.getAllUsers().size());   // Check size of array
        verify(userService, times(1)).getAllUsers();    // Check that method was called once
    }

    @Test
    void testGetUserById_UserFound() {
        User user = new User("Jan Kowalski", "male", 30);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());  // Check response code
        assertEquals(user, response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userController.getUserById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());   // Check response code
        assertEquals("User with ID 1 not found.", response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testCreateUser_Success() {
        User user = new User("Jan Kowalski", "male", 30);
        when(userService.createUser(user)).thenReturn(user);

        ResponseEntity<?> response = userController.createUser(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Check response code
        assertEquals(user, response.getBody());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void testCreateUser_Conflict() {
        User user = new User("male", 30);
        when(userService.createUser(user)).thenThrow(new RuntimeException("Conflict"));

        ResponseEntity<?> response = userController.createUser(user);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody());
        verify(userService, times(1)).createUser(user);
    }

    @Test
    void testUpdateUser_Success() {
        User userDetails = new User("male", 31);
        when(userService.updateUser(1L, userDetails)).thenReturn(userDetails);

        ResponseEntity<?> response = userController.updateUser(1L, userDetails);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDetails, response.getBody());
        verify(userService, times(1)).updateUser(1L, userDetails);
    }

    @Test
    void testUpdateUser_NotFound() {
        User userDetails = new User("female", 31);
        when(userService.updateUser(1L, userDetails)).thenThrow(new RuntimeException("User with ID 1 not found"));

        ResponseEntity<?> response = userController.updateUser(1L, userDetails);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with ID 1 not found.", response.getBody());
        verify(userService, times(1)).updateUser(1L, userDetails);
    }

    @Test
    void testDeleteUser_Success() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        doThrow(new RuntimeException("User with ID 1 not found")).when(userService).deleteUser(1L);

        ResponseEntity<?> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User with ID 1 not found.", response.getBody());
        verify(userService, times(1)).deleteUser(1L);
    }
}
