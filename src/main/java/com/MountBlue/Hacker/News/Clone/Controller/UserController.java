package com.MountBlue.Hacker.News.Clone.Controller;

import com.MountBlue.Hacker.News.Clone.dto.UserDto;
import com.MountBlue.Hacker.News.Clone.model.User;
import com.MountBlue.Hacker.News.Clone.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/v1/HackerNews")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    HttpSession httpSession;

    @GetMapping("/CreateUser")
    public String creationOfUser(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "userCreationForm";
    }

    @GetMapping("/Login")
    public String checkLogin(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "login";
    }

    @GetMapping("/user")
    public String getUserByName(Model model, @RequestParam("userId") String userName) {
        User user = userService.findUserByName(userName);
        model.addAttribute("user", user);
        return "user";
    }
}
