package com.MountBlue.Hacker.News.Clone.respository;

import com.MountBlue.Hacker.News.Clone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String userName);

    User findByEmail(String email);
}
