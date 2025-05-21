package com.airport.admin.airport_admin.controllers;

import com.airport.admin.airport_admin.dto.AuthRequest;
import com.airport.admin.airport_admin.dto.AuthResponse;
import com.airport.admin.airport_admin.dto.UserRequestDto;
import com.airport.admin.airport_admin.security.CustomUserDetails;
import com.airport.admin.airport_admin.security.JwtTokenProvider;
import com.airport.admin.airport_admin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.airport.admin.airport_admin.models.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        System.out.println("email=="+ authRequest.getEmail());
        System.out.println(("password=="+ authRequest.getPassword()));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenProvider.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }

//    @PostMapping("/register")
//    public ResponseEntity<AuthResponse> register(@RequestBody UserRequestDto requestDto) {
//        // 1. Create new user
//        User createdUser = userService.createUser(requestDto);
//
//        // 2. Generate token for newly registered user
//        UserDetails userDetails = new CustomUserDetails(createdUser);
//        String token = jwtTokenProvider.generateToken(userDetails);
//
//        // 3. Return token
//        return ResponseEntity.ok(new AuthResponse(token));
//    }

}
