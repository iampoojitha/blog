package com.spring.Blog_Project_Using_Spring_Boot.controller;

import com.spring.Blog_Project_Using_Spring_Boot.model.Comment;
import com.spring.Blog_Project_Using_Spring_Boot.model.Posts;
import com.spring.Blog_Project_Using_Spring_Boot.service.CommentService;
import com.spring.Blog_Project_Using_Spring_Boot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/comments")
public class CommentController {

    private  final PostService postService;
    private final CommentService commentService;

    @Autowired
    public CommentController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/{postId}/add-comment")
    public String showCommentForm(@PathVariable("postId") Integer postId, Model model) {

        Posts post = postService.getPostById(postId);
        Comment comment = new Comment();
        comment.setPost(post);
        model.addAttribute("comment", comment);
        model.addAttribute("post", post);
        return "comments/create";
    }

    @PostMapping("/{postId}/show-comments")
    public String showCommentData(@PathVariable("postId") Integer postId,
                                  @RequestParam("name") String name,
                                  @RequestParam("email") String email,
                                  @RequestParam("commentData") String commentData,
                                  Model model) {

        Posts post = postService.getPostById(postId);
        Comment comment = new Comment();
        comment.setName(name);
        comment.setEmail(email);
        comment.setComment(commentData);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUpdatedAt(LocalDateTime.now());
        comment.setPost(post);

        commentService.saveComment(comment);

        return "redirect:/posts/view/" + postId;
    }

    @GetMapping("/edit-comment/{id}")
    public String editPostForm(@PathVariable Integer id, Model model) {
        Comment comment = commentService.getCommentById(id);
        model.addAttribute("comment", comment);
        model.addAttribute("post", comment.getPost());
        return "comments/edit";
    }

    @PostMapping("/update-comment/{id}")
    public String updateComment(@PathVariable Integer id,
                                @RequestParam("postId") Integer postId,
                                @RequestParam("comment") String comment) {

        Comment existingComment = commentService.getCommentById(id);

        existingComment.setComment(comment);
        existingComment.setUpdatedAt(LocalDateTime.now());

        commentService.update(existingComment);

        return "redirect:/posts/view/" + postId;
    }

    @PostMapping("/delete-comment/{id}")
    public String deleteComment(@PathVariable Integer id, @RequestParam("postId") Integer postId) {
        commentService.deleteById(id);
        return "redirect:/posts/view/" + postId;
    }
}


