package com.mcccodeschool.recipeservices.security;

import com.mcccodeschool.recipeservices.model.User;
import com.mcccodeschool.recipeservices.model.User2;
import com.mcccodeschool.recipeservices.repository.User2Repository;
import com.mcccodeschool.recipeservices.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    User2Repository userRepository;

    @Transactional
    public User2Principal loadUserByEmail(String email) throws UsernameNotFoundException {
        User2 user2;
        if (userRepository.existsByEmail(email)){
            user2 = userRepository.findByEmail(email);
        } else {
            throw new UsernameNotFoundException("User not found with email : " + email);
        }
        return User2Principal.create(user2);
    }

    @Transactional
    public User2Principal loadUserById(Long id) {
        User2 user2;
        if (userRepository.findById(id).isPresent()){
            user2 = userRepository.findById(id).get();
        } else {
            throw new UsernameNotFoundException("User not found with id : " + id.toString());
        }
        return User2Principal.create(user2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}