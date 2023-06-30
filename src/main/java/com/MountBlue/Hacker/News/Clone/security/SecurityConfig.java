package com.MountBlue.Hacker.News.Clone.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
//        return http
//                .formLogin(Customizer.withDefaults())
//                .oauth2Login(oc->oc.userInfoEndpoint(ui->ui.userService(oauth2LoginHandler()).oidcUserService(oidcLoginHandler())))
//                .authorizeHttpRequests(c->c.anyRequest().authenticated())
//                .build();
//
//    }
//
//    private OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler() {
//        return userRequest -> {
//            OidcUserService delegate = new OidcUserService();
//            return delegate.loadUser(userRequest);
//        };
//    }
//
//    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler() {
//        return userRequest -> {
//            DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
//            return delegate.loadUser(userRequest);
//        };
//    }

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(configure ->
                        configure
                                .requestMatchers("/oauth2/**",
                                        "/v1/HackerNews/home",
                                        "/v1/HackerNews/",
                                        "/",
                                        "/images/**",
                                        "/v1/HackerNews/item",
                                        "/v1/HackerNews/newsguidelines.html",
                                        "/v1/HackerNews/newsfaq.html",
                                        "/v1/HackerNews/security.html",
                                        "/v1/HackerNews/welcome.html",
                                        "/v1/HackerNews/search",
                                        "/v1/HackerNews/form",
                                        "/v1/HackerNews/new",
                                        "/v1/HackerNews/show",
                                        "/v1/HackerNews/ask",
                                        "/v1/HackerNews/jobs",
                                        "/v1/HackerNews/news",
                                        "/v1/HackerNews/front",
                                        "/v1/HackerNews/getAllCommentByPost",
                                        "/v1/HackerNews/CreateUser"
                                        ).permitAll()
                                .requestMatchers("/v1/HackerNews/savePost",
                                        "/v1/HackerNews/votePost",
                                        "/v1/HackerNews/createPost",
                                        "/v1/HackerNews/deletePost",
                                        "/v1/HackerNews/UpdatePost",
                                        "/v1/HackerNews/newComments",
                                        "/v1/HackerNews/saveComment",
                                        "/v1/HackerNews/updateComment",
                                        "/v1/HackerNews/voteComment",
                                        "/v1/HackerNews/",
                                        "/v1/HackerNews/user",
                                        "v1/HackerNews/hideList",
                                                "/v1/HackerNews/upvotedList",
                                        "/v1/HackerNews/CommentList")
                                .authenticated())
                .formLogin(form ->
                        form
                                .loginPage("/v1/HackerNews/Login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                                .defaultSuccessUrl("/v1/HackerNews/home"))
                .oauth2Login(c -> c
                        .loginPage("/v1/HackerNews/Login")
                        .userInfoEndpoint(customizer ->
                                customizer.userService(oAuth2UserService))
                        .successHandler(oAuth2LoginSuccessHandler))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/v1/HackerNews/home")
                        .permitAll());
        return httpSecurity.build();
    }

}
