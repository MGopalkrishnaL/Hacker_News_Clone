package com.MountBlue.Hacker.News.Clone.service;

import com.MountBlue.Hacker.News.Clone.dto.PostDto;
import com.MountBlue.Hacker.News.Clone.model.Comment;
import com.MountBlue.Hacker.News.Clone.model.Post;
import com.MountBlue.Hacker.News.Clone.model.User;
import com.MountBlue.Hacker.News.Clone.respository.CommentRepository;
import com.MountBlue.Hacker.News.Clone.respository.PostRepository;
import com.MountBlue.Hacker.News.Clone.security.CustomOauth2User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final CommentService commentService;
    @Autowired
    HttpSession httpSession;

    public PostService(PostRepository postRepository, CommentService commentService, CommentRepository commentRepository, UserService userService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.commentService = commentService;
    }

    public Page<Post> getAllPost(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> pagePost = postRepository.findAll(pageable);
        return pagePost;
    }

    public Page<Post> getPostBySort(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<Post> pagePost = postRepository.findAll(pageable);
        return pagePost;
    }

    public List<Post> getAllpost() {
        return postRepository.findAll();
    }

    public void savePostData(Post post) {
        postRepository.save(post);
    }

    public void deletePostById(int postId) {
        postRepository.deleteById(postId);

    }

    public void UpdatePostById(int postId, PostDto postDto) {
        if (postRepository.existsById(postId)) {
            Post newPost = postRepository.findById(postId).get();
            newPost.setTitle(postDto.getTitle());
            newPost.setContent(postDto.getContent());
            newPost.setCreatedBy(postDto.getCreatedBy());
            newPost.setDomainName(postDto.getDomainName());
            postRepository.save(newPost);
        }
    }

    public List<Post> getPostById(int postId) {
        return postRepository.findById(postId).stream().toList();
    }

    public List<Post> search(String search) {
        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrCreatedByContainingIgnoreCase
                (search, search, search);
    }

    public Page<Post> filterByDomainName(String word) {
        List<Post> postList = postRepository.findAllByDomainName(word);
        Page<Post> posts = convertingListToPostPage(postList);
        return posts;
    }

    public Post findByPostId(int postId) {
        return postRepository.findById(postId).get();
    }

    public Page<Post> convertingListToPostPage(List<Post> postList) {
        Pageable pageable = PageRequest.of(0, 20).withSort(Sort.by("createdAt").descending());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postList.size());
        return new PageImpl<>(postList.subList(start, end), pageable, postList.size());
    }

    public Page<Post> getAllPostOfShow(int pageNumber, int pageSize) {
        List<Post> postList = postRepository.findByContentStartingWithShow();
        Page<Post> posts = convertingListToPostPage(postList);
        return posts;
    }

    public Page<Post> getAllPostOfAsk(int pageNumber, int pageSize) {
        List<Post> postList = postRepository.findByContentStartingWithAsk();
        Page<Post> posts = convertingListToPostPage(postList);
        return posts;
    }

    public Page<Post> getAllPostOfJobs(int pageNumber, int pageSize) {
        List<Post> postList = postRepository.findByContentContainingHiring();
        Page<Post> posts = convertingListToPostPage(postList);
        return posts;
    }

    public Page<Post> getAllPostOfNews(int pageNumber, int pageSize) {
        List<Post> postList = postRepository.findAllPostOfNews();
        Page<Post> posts = convertingListToPostPage(postList);
        return posts;
    }

    public void postVote(int postId, String userId) {
        Post post = findByPostId(postId);
        User user = userService.findUserByName(userId);
        if (post.getVotedUpByUsers().contains(user)) {
            post.getVotedUpByUsers().remove(user);
        } else {
            post.getVotedUpByUsers().add(user);
        }
        savePostData(post);
    }

    public void commentVote(int commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).get();
        System.out.println(comment);
        User user = userService.findUserByName(userId);
        if (comment.getVotedUpByUsers().contains(user)) {
            comment.getVotedUpByUsers().remove(user);
        } else {
            comment.getVotedUpByUsers().add(user);
        }
        commentService.saveCommentData(comment);
    }

    public void hide(int postId, int userId) {
        Post post = findByPostId(postId);
        User user = userService.findUserById(userId);
        if (post.getHiddenByUsers().contains(user)) {
            Set<User> userSet = post.getHiddenByUsers();
            userSet.remove(user);
            post.setHiddenByUsers(userSet);
            savePostData(post);
        } else {
            Set<User> userSet = post.getHiddenByUsers();
            userSet.add(user);
            post.setHiddenByUsers(userSet);
            savePostData(post);
        }
    }

    public static String getTimeAgo(Timestamp createdAt) {
        Instant currentTimestamp = Instant.now();
        Instant postTimestamp = createdAt.toInstant();
        Duration duration = Duration.between(postTimestamp, currentTimestamp);
        long seconds = duration.getSeconds();
        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " minutes ago";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " hours ago";
        } else {
            long days = seconds / 86400;
            return days + " days ago";
        }
    }

    public LocalDate navigateDate(LocalDate currentDate, String action) {
        switch (action) {
            case "previousDay":
                return currentDate.minusDays(1);
            case "previousMonth":
                return currentDate.minusMonths(1);
            case "previousYear":
                return currentDate.minusYears(1);
            case "nextDay":
                return currentDate.plusDays(1);
            case "nextMonth":
                return currentDate.plusMonths(1);
            case "nextYear":
                return currentDate.plusYears(1);
            default:
                return currentDate;
        }
    }

    public LocalDate convertToLocalDate(Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return localDateTime.toLocalDate();
    }

    public Page<Post> getPostForPast(LocalDate currentDate, String action, int pageNumber, int pageSize) {
        List<Post> allPost = postRepository.findAll();
        List<Post> posts = new ArrayList<>();
        for (Post post : allPost) {
            if (currentDate.equals(convertToLocalDate(post.getCreatedAt()))) {
                posts.add(post);
            }
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), posts.size());
        return new PageImpl<>(posts.subList(start, end), pageable, posts.size());
    }

    public void savePost(Post postDto) {
        CustomOauth2User principal = new CustomOauth2User ((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String userName = principal.getName();
        System.out.println(userName);
        Post post = new Post();
        post.setPostId(postDto.getPostId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDomainName(postDto.getDomainName());
        post.setCreatedBy(userName);
        User user = userService.findUserByName(userName);
        post.setUser(user);
        System.out.println(user);
        postRepository.save(post);
    }
}


