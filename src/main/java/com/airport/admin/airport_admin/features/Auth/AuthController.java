package com.airport.admin.airport_admin.features.Auth;

import com.airport.admin.airport_admin.features.Auth.dto.AuthRequest;
import com.airport.admin.airport_admin.security.CustomUserDetails;
import com.airport.admin.airport_admin.security.JwtTokenProvider;
import com.airport.admin.airport_admin.features.Admin.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String cleanRole = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority()
                .replace("ROLE_", "");

        Map<String, Object> response = new HashMap<>();
        response.put("id", userDetails.getId());
        response.put("email", userDetails.getUsername());
        response.put("role", cleanRole);

        return ResponseEntity.ok(response);
    }



    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
        return ResponseEntity.ok("Logged out");
    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            System.out.println("Authenticating user...");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            System.out.println("Authenticated!");

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            System.out.println("Generating token for: " + userDetails.getUsername());

            String token = jwtTokenProvider.generateToken(userDetails);

            System.out.println("JWT generated: " + token);

            String cleanRole = userDetails.getAuthorities()
                    .iterator()
                    .next()
                    .getAuthority()
                    .replace("ROLE_", "");

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("token", token);
            responseBody.put("id", userDetails.getId());
            responseBody.put("email", userDetails.getUsername());
            responseBody.put("role", cleanRole);

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Login error: " + e.getMessage());
        }
    }

}
