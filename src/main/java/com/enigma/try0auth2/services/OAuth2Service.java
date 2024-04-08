package com.enigma.try0auth2.services;

import com.enigma.try0auth2.dto.Oauth2UserInfoDto;
import com.enigma.try0auth2.entities.User;
import com.enigma.try0auth2.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) {
        log.trace("OAuth2UserRequest: {}", oAuth2UserRequest);
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return null;
    }

//    public OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
//        Oauth2UserInfoDto userInfoDto = Oauth2UserInfoDto.builder()
//                .id(oAuth2User.getAttribute("sub").toString())
//                .name(oAuth2User.getAttribute("name").toString())
//                .email(oAuth2User.getAttribute("email").toString())
//                .picture(oAuth2User.getAttribute("picture").toString())
//                .build();
//
//        log.trace("User info is {}", userInfoDto);
//        Optional<User> userOptional = userRepository.findByUsername(userInfoDto.getEmail());
//        log.trace("User is {}", userOptional);
//        User user = userOptional.map(existingUser -> updateExistingUser(existingUser, userInfoDto))
//                .orElseGet(() -> registerNewUser(oAuth2UserRequest, userInfoDto));
//        return oAuth2User;
//    }

    public User registerNewUser(String clientRegistration, OAuth2User oAuth2User) {
        User user = new User();
        user.setProvider(clientRegistration);
        user.setName(oAuth2User.getAttribute("name"));
        user.setUsername(oAuth2User.getAttribute("email"));
        user.setPicture(oAuth2User.getAttribute("picture"));
        user.setId(UUID.randomUUID());
        return userRepository.save(user);
    }

    public Boolean checkExistingUser(String email) {
        User user = userRepository.findByUsername(email).orElse(null);
        if (user == null) {
            return false;
        }
        return true;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByUsername(email).orElse(null);
    }

    public User updateExistingUser(User existingUser, OAuth2User userInfoDto) {
        existingUser.setName(userInfoDto.getName());
        existingUser.setPicture(userInfoDto.getAttribute("picture").toString());
        return userRepository.save(existingUser);
    }
}
