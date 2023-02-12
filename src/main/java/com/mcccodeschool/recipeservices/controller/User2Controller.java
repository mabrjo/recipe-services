package com.mcccodeschool.recipeservices.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mcccodeschool.recipeservices.dto.User2DTO;
import com.mcccodeschool.recipeservices.dto.UserDTO;
import com.mcccodeschool.recipeservices.dto.UserInfoDTO;
import com.mcccodeschool.recipeservices.model.Recipe;
import com.mcccodeschool.recipeservices.model.User;
import com.mcccodeschool.recipeservices.repository.UserRepository;
import com.mcccodeschool.recipeservices.security.User2Principal;
import com.mcccodeschool.recipeservices.security.UserPrincipal;
import com.mcccodeschool.recipeservices.service.CustomOAuth2UserService;
import com.mcccodeschool.recipeservices.service.User2Service;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin()
@RestController
@RequestMapping("/api/v2")
public class User2Controller {

    @Autowired
    User2Service user2Service;

    @Autowired
    ObjectMapper objectMapper;

    /*
        Returns current logged in user by checking securitycontext principal
     */
    @GetMapping("/user")
    public ResponseEntity<?> getUser(){
            try {
                User2Principal principal = (User2Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (user2Service.findUserById(principal.getId()) != null){
                    UserInfoDTO userInfoDTO = user2Service.mapDtoToPayload(user2Service.findUserById(principal.getId()));
                    return ResponseEntity.status(HttpStatus.OK).body(userInfoDTO);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
            } catch (ClassCastException ex){
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
            }
    }


    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam Map<String, String> params){
        if (params.containsKey("id")) {
            if (user2Service.existsById(Long.valueOf(params.get("id")))){
                User2DTO user2DTO = user2Service.findUserById(Long.valueOf(params.get("id")));
                return ResponseEntity.status(HttpStatus.OK).body(user2DTO);
            }
        }
        List<User2DTO> allUsers = user2Service.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @GetMapping("/usernames")
    public ResponseEntity<?> getUsernames(){
        List<String> body = user2Service.getUsernames();
        if (body.isEmpty()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("no content");
        }
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@Validated @RequestBody UserInfoDTO userInfoDTO) {
        try {
            User2Principal principal = (User2Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            // A second condition to ensure only updating our own profile
            if (user2Service.existsById(userInfoDTO.getId()) && (userInfoDTO.getId().equals(principal.getId()))) {
                UserInfoDTO userInfoDTOResponse = user2Service.mapDtoToPayload(
                        user2Service.saveUser(
                                user2Service.mapPayloadToUser2DTO(userInfoDTO)
                        )
                );
                return ResponseEntity.status(HttpStatus.OK).body(
                        userInfoDTOResponse
                );
            } else if (!(userInfoDTO.getId().equals(principal.getId()))) {
                return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot update another user");
            } else if (!user2Service.existsById(userInfoDTO.getId())){
                HashMap<String, String> body = new HashMap<>();
                body.put("error", "User not found : ");
                body.put("content", objectMapper.writeValueAsString(userInfoDTO));
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
            }
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        } catch (ClassCastException | JsonProcessingException ex){
                HashMap<String, String> body = new HashMap<>();
                body.put("error", ex.getMessage());
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }





}
