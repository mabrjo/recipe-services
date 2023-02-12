package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.*;
import com.mcccodeschool.recipeservices.model.*;
import com.mcccodeschool.recipeservices.repository.Recipe2Repository;
import com.mcccodeschool.recipeservices.repository.User2Repository;
import org.apache.catalina.util.ResourceSet;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.management.modelmbean.ModelMBean;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class User2Service {

    public final User2Repository user2Repository;
    public final ModelMapper modelMapper;
    public final Recipe2Repository recipe2Repository;

    public User2Service(ModelMapper modelMapper , User2Repository user2Repository, Recipe2Repository recipe2Repository){
        this.user2Repository=user2Repository;
        this.modelMapper=modelMapper;
        this.recipe2Repository = recipe2Repository;
    }

    public UserInfoDTO mapDtoToPayload(User2DTO userDTO){
        return modelMapper.map(userDTO, UserInfoDTO.class);
    }

    public User2DTO mapPayloadToUser2DTO(UserInfoDTO userInfoDTO){
        if (user2Repository.findById(userInfoDTO.getId()).isPresent()){
            User2DTO user2DTO =
                    modelMapper.map(user2Repository.findById(userInfoDTO.getId()).get(), User2DTO.class);
            user2DTO.setUsername(userInfoDTO.getUsername());
            user2DTO.setDisplayName(userInfoDTO.getDisplayName());
            user2DTO.setPhotoUrl(userInfoDTO.getPhotoUrl());
            user2DTO.setEnabled(userInfoDTO.isEnabled());
            user2DTO.setUserRecipes(userInfoDTO.getUserRecipes());
            user2DTO.setBookmarks(userInfoDTO.getBookmarks());
            user2DTO.setCommentNotifications(userInfoDTO.isCommentNotifications());
            user2DTO.setLikeNotifications(userInfoDTO.isLikeNotifications());
            user2DTO.setNewFollowerNotifications(userInfoDTO.isNewFollowerNotifications());
            user2DTO.setRecipeSavedNotifications(userInfoDTO.isRecipeSavedNotifications());
            return user2DTO;
        } else {
            return null;
        }
    }

    public User2DTO mapUserToDTO(User2 user2){
        return modelMapper.map(user2, User2DTO.class);
    }

    public User2DTO saveUser(User2DTO user2DTO){
        return modelMapper.map(user2Repository.save(modelMapper.map(user2DTO, User2.class)), User2DTO.class);
    }

    public Boolean existsById(Long userId) {
        return user2Repository.existsById(userId);
    }

    public User2DTO findUserById(Long userId) {
        if (user2Repository.findById(userId).isPresent()) {
            User2 foundUser = user2Repository.findById(userId).get();
            return modelMapper.map(foundUser, User2DTO.class);
        }
        return null;
    }

    public User2DTO addRecipe(Long userId, Long recipe2Id) {
        User2 user2 = new User2();
        Recipe2 recipe2 = new Recipe2();
        if (user2Repository.findById(userId).isPresent()){
            user2 = user2Repository.findById(userId).get();
        }
        if (recipe2Repository.findById(recipe2Id).isPresent()){
            recipe2 = recipe2Repository.findById(recipe2Id).get();
        }
        if (user2.getUserRecipes().isEmpty()) {
            Set<Recipe2> userRecipeList = new HashSet<>();
            userRecipeList.add(recipe2);
            user2.setUserRecipes(userRecipeList);
        } else {
            user2.addRecipe(recipe2);
        }

        return modelMapper.map(user2Repository.save(user2), User2DTO.class);
    }

    // TODO: This needs to take in collection ID as well, store to user's collection
    public User2DTO addBookmark(Long userId, Long recipe2Id){
        Optional<User2> opU2 = user2Repository.findById(userId);
        Optional<Recipe2> opR2 = recipe2Repository.findById(recipe2Id);
        //TODO: add error checking if user doesn't exist
        opU2.get().addBookmark(opR2.get());
        User2 u2 = user2Repository.save(opU2.get());
        return modelMapper.map(u2, User2DTO.class);
    }

    public User2DTO addEnabledUsername(Long userId, Map<String, String> object) {
        if(user2Repository.findById(userId).isPresent()) {
            User2 foundUser = user2Repository.findById(userId).get();
            foundUser.setUsername(object.get("username"));
            foundUser.setEnabled(true);
            user2Repository.save(foundUser);
            return modelMapper.map(foundUser, User2DTO.class);
        }
        return null;
    }

    public List<String> getUsernames() {
        return user2Repository.findAllByUsernameNotNull()
                .stream()
                .map(User2::getUsername)
                .filter(username -> username.length() > 0)
                .collect(Collectors.toList());
    }

    public List<User2DTO> getAllUsers() {
        List<User2> users = user2Repository.findAll();
        List<User2DTO> user2DTOs = new ArrayList<>();
        for(User2 u : users) {
            User2DTO u2DTO = modelMapper.map(u, User2DTO.class);
            user2DTOs.add(u2DTO);
        }
        return user2DTOs;
    }


}
