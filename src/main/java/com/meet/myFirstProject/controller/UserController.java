package com.meet.myFirstProject.controller;

import com.meet.myFirstProject.entity.User;
import com.meet.myFirstProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<User> getAllUserEntries() {
        return userService.getAll();
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                System.out.println("Authentication is null or unauthenticated");
                return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
            }
            System.out.println("Authentication Name: " + authentication.getName());
            System.out.println("Authentication Principal: " + authentication.getPrincipal());

            User userInDb = userService.findByUserName(authentication.getName());

            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveEntry(userInDb);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody User loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUserName(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }
}
