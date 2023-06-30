package com.MountBlue.Hacker.News.Clone.restcontoller;

import com.MountBlue.Hacker.News.Clone.dto.PostDto;
import com.MountBlue.Hacker.News.Clone.model.Post;
import com.MountBlue.Hacker.News.Clone.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/HackerNews")
public class PostRestController {
    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/getAllPost")
    public List<Post> getAllPosts(@RequestParam(required = false) Integer postId) {
        if (postId == null) {
            return postService.getAllpost();
        } else {
            return postService.getPostById(postId);
        }
    }

    @PostMapping("/savePost")
    public ResponseEntity<String> savePost(@RequestBody PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setCreatedBy(postDto.getCreatedBy());
        post.setDomainName(postDto.getDomainName());
        postService.savePostData(post);
        return new ResponseEntity<>("Data is saved", HttpStatus.CREATED);
    }

    @DeleteMapping("/deletePost")
    public ResponseEntity<String> deletePost(@RequestParam int postId) {
        if (postService.getPostById(postId) != null) {
            postService.deletePostById(postId);
            return new ResponseEntity<>("Post has been deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Post has been not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("UpdatePost")
    public ResponseEntity<String> updatePost(@RequestParam int postId, @RequestBody PostDto postDto) {
        if (postService.getPostById(postId) != null) {
            postService.UpdatePostById(postId, postDto);
            return new ResponseEntity<>("Post Has been updated", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Post with id is not present please create the new post", HttpStatus.NOT_FOUND);
        }
    }

}
