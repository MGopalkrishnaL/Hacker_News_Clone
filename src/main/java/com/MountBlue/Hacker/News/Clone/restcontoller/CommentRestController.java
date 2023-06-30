package com.MountBlue.Hacker.News.Clone.restcontoller;

import com.MountBlue.Hacker.News.Clone.dto.CommentDto;
import com.MountBlue.Hacker.News.Clone.model.Comment;
import com.MountBlue.Hacker.News.Clone.model.Post;
import com.MountBlue.Hacker.News.Clone.service.CommentService;
import com.MountBlue.Hacker.News.Clone.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/HackerNews")
public class CommentRestController {
    private final CommentService commentService;
    private final PostService postService;

    public CommentRestController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @GetMapping("/getAllComment")
    public ResponseEntity<String> getAllCommentByPost(@RequestParam(required = false) Integer postId) {
        if (postId == null) {
            return new ResponseEntity<>("List of all comment present on all posts:" + commentService.getAllComment(), HttpStatus.OK);
        } else {
            if (postService.getPostById(postId) == null) {
                return new ResponseEntity<>("Post With the id " + postId + "Is not available", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("List of comment :" + commentService.getAllCommentByPost(postId), HttpStatus.OK);
            }
        }
    }

    @PostMapping("/saveComment")
    public ResponseEntity<String> saveCommentData(@RequestBody CommentDto commentDto, @RequestParam Integer postId) {
        Comment comment = new Comment();
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setComment(commentDto.getComment());
        Post post = postService.findByPostId(postId);
        comment.setPostId(post);
        commentService.saveCommentData(comment);
        return new ResponseEntity<>("Comment saved successfully", HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteComment")
    public ResponseEntity<String> deleteCommentById(@RequestParam Integer commentId) {
        if (commentId == null) {
            return new ResponseEntity<>("No Comment ID was given.", HttpStatus.NOT_FOUND);
        } else {
            if (commentService.getCommentById(commentId) != null) {
                commentService.deleteCommentById(commentId);
                return new ResponseEntity<>("Comment deleted", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No Comment present with comment ID" + commentId, HttpStatus.NOT_FOUND);
            }
        }
    }

    @PutMapping("/updateComment")
    public ResponseEntity<String> updatePost(@RequestParam int commentId, @RequestBody CommentDto commentDto) {
        if (commentService.getCommentById(commentId) != null) {
            commentService.UpdateCommentById(commentId, commentDto);
            return new ResponseEntity<>("Comment Has been updated", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Comment with id is not present please create the new post", HttpStatus.NOT_FOUND);
    }
}
