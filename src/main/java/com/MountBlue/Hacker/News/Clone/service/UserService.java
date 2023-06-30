package com.MountBlue.Hacker.News.Clone.service;

import com.MountBlue.Hacker.News.Clone.dto.UserDto;
import com.MountBlue.Hacker.News.Clone.model.User;
import com.MountBlue.Hacker.News.Clone.respository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        userRepository.save(user);
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public void deleteUserByUserId(int userid) {
        userRepository.deleteById(userid);
    }

    public User findUserByName(String userName) {
        User user = userRepository.findByUserName(userName);
        return user;
    }

    public User findUserById(int userId) {
        return userRepository.findById(userId).get();
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void addUserThroughOAuth(String email, String name) {
        User user = new User();
        user.setUserName(name);
        user.setEmail(email);
        user.setPassword(UUID.randomUUID().toString());
        userRepository.save(user);
    }
}
