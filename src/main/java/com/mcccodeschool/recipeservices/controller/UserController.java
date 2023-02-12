package com.mcccodeschool.recipeservices.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mcccodeschool.recipeservices.model.Recipe;
import com.mcccodeschool.recipeservices.model.User;
import com.mcccodeschool.recipeservices.repository.UserRepository;
import com.mcccodeschool.recipeservices.security.UserPrincipal;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class UserController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;


    /*
        Returns current logged in user
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUser(){
        User user = new User();
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userRepository.findById(principal.getId()).isPresent()){
            user = userRepository.findById(principal.getId()).get();
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
        HashMap<String, String> body = new HashMap<>();
        body.put("error", "Internal server error. No user information found");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @GetMapping("/usernames")
    public ResponseEntity<?> getUsernames(){
        List<String> body = userRepository.findAllByUsernameNotNull()
                .stream()
                .map(User::getUsername)
                .filter(username -> username.length() > 0)
                .collect(Collectors.toList());
        if (body.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error. Try again later");
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/user/update")
    public ResponseEntity<?> postUsername(@Validated @RequestBody Map<String,String> object) {
        System.out.println(object);
        User user;
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (userRepository.findById(principal.getId()).isPresent()) {
                user = userRepository.findById(principal.getId()).get();
                user.setUsername(object.get("username"));
                user.setEnabled(true);
                userRepository.save(user);
                return ResponseEntity.status(HttpStatus.OK).body(user);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Service Error");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Service Error");
    }





}

