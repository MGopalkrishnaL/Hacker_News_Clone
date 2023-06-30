package com.MountBlue.Hacker.News.Clone.Controller;

import com.MountBlue.Hacker.News.Clone.model.Comment;
import com.MountBlue.Hacker.News.Clone.model.Post;
import com.MountBlue.Hacker.News.Clone.model.User;
import com.MountBlue.Hacker.News.Clone.security.CustomOauth2User;
import com.MountBlue.Hacker.News.Clone.service.CommentService;
import com.MountBlue.Hacker.News.Clone.service.PostService;
import com.MountBlue.Hacker.News.Clone.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/v1/HackerNews")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;

    public PostController(PostService postService, CommentService commentService, UserService userService) {

        this.postService = postService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/newsguidelines.html")
    public String getNewsGuidelines() {
        return "news-guidelines";
    }

    @GetMapping("/newsfaq.html")
    public String getNewsFaq() {
        return "newsfaq";
    }

    @GetMapping("/security.html")
    public String getSecurity() {
        return "security";
    }

    @GetMapping("/welcome.html")
    public String getWelcome() {
        return "welcome";
    }

    @GetMapping({"/home", "/"})
    public String getHomePage(Model model, @RequestParam(value = "p", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "limit", defaultValue = "20", required = false) int pageSize) {
        String word = "home";
        Page<Post> postList = postService.getAllPost(pageNumber, pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postList.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("postList", postList);
        model.addAttribute("word", word);
        return "home";
    }

    @GetMapping("/postView/{id}")
    public String postView(Model model, @PathVariable("postId") int id) {
        return "redirect:/item?postId=${postId}";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "search", defaultValue = "") String search,
                         Model model,
                         @RequestParam(value = "select", defaultValue = "stories") String select,
                         @RequestParam(value = "p", defaultValue = "0", required = false) int pageNumber,
                         @RequestParam(value = "limit", defaultValue = "20", required = false) int pageSize) {
        if (select.equals("stories")) {
            List<Post> posts = postService.search(search);
            Page<Post> postList = postService.convertingListToPostPage(posts);
            model.addAttribute("postList", postList);
            model.addAttribute("totalPages", postList.getTotalPages());
        } else if (select.equals("comments")) {
            List<Comment> comments = commentService.search(search);
            Page<Comment> commentList = commentService.convertingListToPage(comments);
            model.addAttribute("commentList", commentList);
            model.addAttribute("totalPages", commentList.getTotalPages());
        }
        model.addAttribute("search", search);
        model.addAttribute("select", select);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("search", search);

        return "search";
    }

    @GetMapping("/form")
    public String filterByDomainName(@RequestParam("site") String domainName, Model model) {
        int pageNumber = 0, pageSize = 20;
        Page<Post> postList = postService.filterByDomainName(domainName);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postList.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("postList", postList);
        return "home";
    }

    @GetMapping("/item")
    public String postView(@RequestParam("postId") int postId, Model model) {
        Post post = postService.findByPostId(postId);
        List<Comment> commentList = post.getCommentId();
        model.addAttribute("post", post);
        model.addAttribute("commentList", commentList);
        return "postpage";
    }

    @PostMapping("/savePost")
    public String savePost(@ModelAttribute Post post) {
        postService.savePost(post);
        return "redirect:/v1/HackerNews/home";
    }

    @GetMapping("/createPost")
    public String Post(Model model) {
        Post post = new Post();
        model.addAttribute("post", post);
        return "post-Submit";
    }

    @GetMapping("/new")
    public String newest(Model model, @RequestParam(value = "p", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "limit", defaultValue = "20", required = false) int pageSize) {
        String word = "new";
        Page<Post> postList = postService.getPostBySort(pageNumber, pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postList.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("postList", postList);
        model.addAttribute("word", word);
        return "home";
    }

    @GetMapping("/show")
    public String show(Model model, @RequestParam(value = "p", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "limit", defaultValue = "20", required = false) int pageSize) {
        String word = "show";
        Page<Post> postList = postService.getAllPostOfShow(pageNumber, pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postList.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("postList", postList);
        model.addAttribute("word", word);
        return "home";
    }

    @GetMapping("/ask")
    public String ask(Model model, @RequestParam(value = "p", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "limit", defaultValue = "20", required = false) int pageSize) {
        String word = "ask";
        Page<Post> postList = postService.getAllPostOfAsk(pageNumber, pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postList.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("postList", postList);
        model.addAttribute("word", word);
        return "home";
    }

    @GetMapping("/jobs")
    public String job(Model model, @RequestParam(value = "p", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "limit", defaultValue = "20", required = false) int pageSize) {
        Page<Post> postList = postService.getAllPostOfJobs(pageNumber, pageSize);
        String word = "job";
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postList.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("postList", postList);
        model.addAttribute("word", word);
        return "home";
    }

    @GetMapping("/news")
    public String news(Model model, @RequestParam(value = "p", defaultValue = "0", required = false) int pageNumber, @RequestParam(value = "limit", defaultValue = "20", required = false) int pageSize) {
        String word = "news";
        Page<Post> postList = postService.getAllPostOfNews(pageNumber, pageSize);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postList.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("postList", postList);
        model.addAttribute("word", word);
        return "home";
    }

    @GetMapping("/votePost")
    public String voteCount(@RequestParam int postId) {
        CustomOauth2User principal =new CustomOauth2User((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String userName = principal.getName();
        postService.postVote(postId, userName);
        return "redirect:/v1/HackerNews/home";
    }

    @GetMapping("/hide")
    public String hide(@RequestParam int postId, @RequestParam int userId) {
        postService.hide(postId, userId);
        return "redirect:/v1/HackerNews/home";
    }
    @GetMapping("/hideList")
    public String getHideList(@RequestParam String userId,
                              Model model,
                              @RequestParam(value = "p", defaultValue = "0") int pageNumber,
                              @RequestParam(value = "limit", defaultValue = "20") int pageSize){
        User user= userService.findUserByName(userId);
        List<Post> postList=user.getHiddenPost();
        Page<Post> posts = postService.convertingListToPostPage(postList);
        String word = "hide";
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("postList", posts);
        model.addAttribute("word", word);
        return "home";
    }

    @GetMapping("/front")
    public String getPast(Model model, @RequestParam(value = "p", defaultValue = "0") int pageNumber, @RequestParam(value = "limit", defaultValue = "20") int pageSize) {
        Page<Post> postList = postService.getPostForPast(LocalDate.now(), "todayDate", pageNumber, pageSize);
        model.addAttribute(postList.getContent());
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postList.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("day", LocalDate.now());
        return "past";
    }

    @PostMapping("/front")
    public String navigateDate(@RequestParam(value = "day", required = false) LocalDate day, @RequestParam("action") String action, Model model, @RequestParam(value = "p", defaultValue = "0") int pageNumber, @RequestParam(value = "limit", defaultValue = "20") int pageSize) {
        LocalDate navDate = postService.navigateDate(day, action);
        Page<Post> postList = postService.getPostForPast(navDate, action, pageNumber, pageSize);

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postList.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("day", navDate);
        model.addAttribute("postList", postList.getContent());
        return "past";
    }

    @GetMapping("/deletePost")
    public String deletePost(@RequestParam("postId") int postId) {
        Post post = postService.findByPostId(postId);
        postService.deletePostById(postId);
        return "redirect:/v1/HackerNews/home";
    }

    @GetMapping("/UpdatePost")
    public String updatePost(@RequestParam("postId") int postId, Model model) {
        Post post = postService.findByPostId(postId);
        model.addAttribute("post", post);
        return "post-Submit";
    }
    @GetMapping("/upvotedList")
    public String upvotedList(@RequestParam String userName,
                              Model model,
                              @RequestParam(value = "p", defaultValue = "0") int pageNumber,
                              @RequestParam(value = "limit", defaultValue = "20") int pageSize){
        List<Post> post = userService.findUserByName(userName).getVoteOfPost();
        Page<Post> postPage = postService.convertingListToPostPage(post);
        String word= "votedList";
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("postList", postPage);
        model.addAttribute("word", word);
        return "home";
    }

}
