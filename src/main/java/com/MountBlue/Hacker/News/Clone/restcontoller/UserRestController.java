package com.MountBlue.Hacker.News.Clone.restcontoller;

import com.MountBlue.Hacker.News.Clone.dto.UserDto;
import com.MountBlue.Hacker.News.Clone.model.User;
import com.MountBlue.Hacker.News.Clone.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/HackerNews")
public class UserRestController {
    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("getAllUser")
    public List<User> getAllUser() {
        return userService.findAllUser();
    }

    @PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return new ResponseEntity<>("User Saved Successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam String userName) {
        User user = userService.findUserByName(userName);
        if (user != null) {
            userService.deleteUserByUserId(user.getUserId());
            return new ResponseEntity<>("User Deleted Successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User is not found", HttpStatus.BAD_REQUEST);
        }
    }
}
