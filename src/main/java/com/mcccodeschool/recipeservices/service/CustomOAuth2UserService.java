package com.mcccodeschool.recipeservices.service;

import com.mcccodeschool.recipeservices.dto.UserDTO;
import com.mcccodeschool.recipeservices.exception.OAuth2Exception;
import com.mcccodeschool.recipeservices.model.User;
import com.mcccodeschool.recipeservices.repository.UserRepository;
import com.mcccodeschool.recipeservices.security.UserPrincipal;
import com.mcccodeschool.recipeservices.security.user.OAuth2UserInfo;
import com.mcccodeschool.recipeservices.security.user.OAuth2UserInfoFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    public String email;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws Exception {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());



        /*
            For github: If the OAuth user info that was pulled from the default userInfoUri doesn't contain an email address,
                call the /emails/ endpoint to get the user's private emails that aren't publicly displayed.

                User will always have at least one email in their list of emails. We will use 'primary' email.

                If unsuccessful, throw error back to front-end and tell user to try with a different provider.
         */
        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            if (oAuth2UserRequest.getClientRegistration().getRegistrationId().equalsIgnoreCase("github")) {

                String GITHUB_EMAILS_URI = "https://api.github.com/user/emails";
                RestTemplate restTemplate = new RestTemplate();
                String token = oAuth2UserRequest.getAccessToken().getTokenValue();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);
                HttpEntity<String> request = new HttpEntity<String>(headers);
                ParameterizedTypeReference<Map<String, String>[]> responseType = new ParameterizedTypeReference<Map<String, String>[]>() {
                };
                ResponseEntity<Map<String, String>[]> response = restTemplate.exchange(GITHUB_EMAILS_URI, HttpMethod.GET, request, responseType);
                if (response.getStatusCode().equals(HttpStatus.OK)) {

                    email = Objects.requireNonNull(Arrays.stream(Objects.requireNonNull(response.getBody())).filter(stringStringMap -> stringStringMap.get("primary").equalsIgnoreCase("true"))
                            .findFirst().orElse(null)).get("email");

                } else {
                    throw new InternalAuthenticationServiceException("could+not+extract+" + oAuth2UserRequest.getClientRegistration().getRegistrationId() + "+email+address");
                }
            } else {
                throw new InternalAuthenticationServiceException("could+not+extract+" + oAuth2UserRequest.getClientRegistration().getRegistrationId() + "+email+address");
            }
        } else {
            email = oAuth2UserInfo.getEmail();
        }

        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!user.getProvider().equals(oAuth2UserRequest.getClientRegistration().getRegistrationId())) {
                throw new OAuth2Exception("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setProvider(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(email);
        user.setPhotoUrl(oAuth2UserInfo.getImageUrl());
        user.setEnabled(false);
        return userRepository.save(user);
    }

    private UserDTO updateUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User foundUser = userRepository.findByUsername(userDTO.getUsername());
        user.setId(foundUser.getId());
        User savedUser = userRepository.save(user);
        UserDTO savedUserDTO = modelMapper.map(savedUser, UserDTO.class);
        return savedUserDTO;
    }

    public Boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    public UserDTO findUserById(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            User foundUser = userRepository.findById(userId).get();
            UserDTO foundUserDTO = modelMapper.map(foundUser, UserDTO.class);
            return foundUserDTO;
        }
        return null;
    }

    public List<String> getUsernames() {
        return userRepository.findAllByUsernameNotNull()
                .stream()
                .map(User::getUsername)
                .filter(username -> username.length() > 0)
                .collect(Collectors.toList());
    }

    public UserDTO addEnabledUsername(Long userId, Map<String, String> object) {
        if(userRepository.findById(userId).isPresent()) {
            User foundUser = userRepository.findById(userId).get();
            foundUser.setUsername(object.get("username"));
            foundUser.setEnabled(true);
            userRepository.save(foundUser);
            UserDTO userDTO = modelMapper.map(foundUser, UserDTO.class);
            return userDTO;
        }
        return null;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<>();
        for(User u : users) {
            UserDTO uDTO = modelMapper.map(u, UserDTO.class);
            userDTOs.add(uDTO);
        }
        return userDTOs;
    }

}
