package com.airport.admin.airport_admin.features.Auth;

import com.airport.admin.airport_admin.features.Auth.dto.AuthRequest;
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
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        try {
            System.out.println("👉 Authenticating user...");

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            System.out.println("✅ Authenticated!");

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            System.out.println("🔐 Generating token for: " + userDetails.getUsername());

            String token = jwtTokenProvider.generateToken(userDetails);

            System.out.println("✅ JWT generated: " + token);

            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("None")
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();

            response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            System.out.println("🍪 Cookie set successfully");

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace(); // log the stack trace
            return ResponseEntity.status(500).body("Login error: " + e.getMessage());
        }
    }
}
