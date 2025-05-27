package com.airport.admin.airport_admin.features.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        User createdUser = userService.createUser(userRequestDto);
        return ResponseEntity.ok(createdUser);
    }

    @PreAuthorize("hasRole('Admin')")
    @PostMapping("/{id}")
        public ResponseEntity<?> updateUser(@Valid @RequestBody UserRequestDto userRequestDto){
            User updateUser = userService.updateUser(userRequestDto);
            return ResponseEntity.ok(updateUser);
        }

    @PreAuthorize("hasAnyRole('Admin' ,'Supervisor' ,'Manager')")
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers(){
        return  ResponseEntity.ok(userService.getAllUsers());
    }


    @PreAuthorize("hasAnyRole('Admin', 'Supervisor', 'Manager')")
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email){
        return userService.findUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('Admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return  ResponseEntity.noContent().build();
    }
}
