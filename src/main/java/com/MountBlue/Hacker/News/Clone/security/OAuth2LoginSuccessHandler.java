package com.MountBlue.Hacker.News.Clone.security;

import com.MountBlue.Hacker.News.Clone.model.User;
import com.MountBlue.Hacker.News.Clone.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOauth2User oAuth2User = new CustomOauth2User((OAuth2User) authentication.getPrincipal());
        String email = oAuth2User.getEmail();
        User user = userService.getByEmail(email);
        if (user == null) {
            userService.addUserThroughOAuth(email, oAuth2User.getName());
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
