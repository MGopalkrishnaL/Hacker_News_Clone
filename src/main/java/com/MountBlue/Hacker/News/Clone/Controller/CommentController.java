package com.MountBlue.Hacker.News.Clone.Controller;

import com.MountBlue.Hacker.News.Clone.dto.CommentDto;
import com.MountBlue.Hacker.News.Clone.model.Comment;
import com.MountBlue.Hacker.News.Clone.model.Post;
import com.MountBlue.Hacker.News.Clone.model.User;
import com.MountBlue.Hacker.News.Clone.service.CommentService;
import com.MountBlue.Hacker.News.Clone.service.PostService;
import com.MountBlue.Hacker.News.Clone.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1/HackerNews")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;
    public CommentController(CommentService commentService, PostService postService,UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService= userService;
    }

    @Autowired
    HttpSession httpSession;

    @GetMapping("/newComments")
    public String getAllComment(Model model) {
        List<Comment> commentList = commentService.getAllComment();
        model.addAttribute("commentList", commentList);
        return "comment";
    }

    @GetMapping("/getAllCommentByPost")
    public String getAllCommentByPost(@RequestParam("postId") int postId, Model model) {
        List<Comment> comment = commentService.getCommentById(postId);
        model.addAttribute("postComment", comment);
        return "redirect:/v1/HackerNews/item?postId=" + postId;
    }

    @PostMapping("/saveComment")
    public String saveCommentData(@RequestParam("postId") int postId,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "email", required = false) String email,
                                  @RequestParam("comment") String comment, Model model) {
        Comment newComment = new Comment();
        newComment.setName((String) httpSession.getAttribute("name"));
        newComment.setEmail((String) httpSession.getAttribute("email"));
        newComment.setComment(comment);
        Post post = postService.findByPostId(postId);
        newComment.setPostId(post);
        commentService.saveCommentData(newComment);
        model.addAttribute("post", post);
        return "redirect:/v1/HackerNews/getAllCommentByPost?postId=" + postId;
    }

    @PutMapping("/updateComment")
    public String updateComment(@RequestParam("commentId") int commentId,
                                @RequestParam("comment") String comment, Model model) {
        CommentDto updatedComment = new CommentDto();
        updatedComment.setComment(comment);
        commentService.UpdateCommentById(commentId, updatedComment);
        return "home";
    }

    @GetMapping("/voteComment")
    public String voteCount(@RequestParam int commentId, @RequestParam String userId) {
        postService.commentVote(commentId, userId);
        return "redirect:/v1/HackerNews/newComments";
    }

}
