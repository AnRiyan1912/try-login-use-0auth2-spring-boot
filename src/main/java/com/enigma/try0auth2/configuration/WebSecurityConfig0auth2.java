package com.enigma.try0auth2.configuration;

import com.enigma.try0auth2.entities.User;
import com.enigma.try0auth2.services.OAuth2Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
@Configuration
public class WebSecurityConfig0auth2 {

    private final OAuth2Service oAuth2Service;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.trace("Configuring SecurityFilterChain");
        return http.authorizeHttpRequests(authorize -> {
                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2Service)).successHandler(new AuthenticationSuccessHandler() {
                            @Override
                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                if (authentication instanceof OAuth2AuthenticationToken) {
                                    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                                    OAuth2User oauthUser = oauthToken.getPrincipal();
                                    User findUser = oAuth2Service.findUserByEmail(oauthUser.getAttribute("email"));
                                    if (findUser != null) {
                                        oAuth2Service.updateExistingUser(findUser, oauthUser);
                                    } else {
                                        oAuth2Service.registerNewUser(((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId(), oauthUser);
                                    }
                                } else {


                                }

                            }
                        })
                )
                .build();

    }
}
