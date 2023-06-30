package com.MountBlue.Hacker.News.Clone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    private String userName;
    private String password;
    private String email;
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> postList;
    @ManyToMany(mappedBy = "votedUpByUsers", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> voteOfPost;
    @ManyToMany(mappedBy = "hiddenByUsers", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Post> hiddenPost;
    @ManyToMany(mappedBy = "votedUpByUsers", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> voteOfComment;
    @CreationTimestamp
    private Timestamp createdAt;
}