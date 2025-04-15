package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.UserRequestDto;
import com.airport.admin.airport_admin.models.User;
import com.airport.admin.airport_admin.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        User createdUser = userService.createUser(userRequestDto);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers(){
        return  ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email){
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }
}
