package com.MountBlue.Hacker.News.Clone.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homeContoller() {
        return "redirect:/v1/HackerNews/home";
    }
}
