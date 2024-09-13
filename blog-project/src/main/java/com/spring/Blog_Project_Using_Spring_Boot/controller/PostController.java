package com.spring.Blog_Project_Using_Spring_Boot.controller;

import com.spring.Blog_Project_Using_Spring_Boot.model.Posts;
import com.spring.Blog_Project_Using_Spring_Boot.model.Tag;
import com.spring.Blog_Project_Using_Spring_Boot.repository.PostsRepository;
import com.spring.Blog_Project_Using_Spring_Boot.service.PostService;
import com.spring.Blog_Project_Using_Spring_Boot.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final TagService tagService;
    private final PostsRepository postsRepository;

    @Autowired
    public PostController(PostService postService, TagService tagService, PostsRepository postsRepository) {
        this.postService = postService;
        this.tagService = tagService;
        this.postsRepository = postsRepository;
    }

    @GetMapping
    public String getPosts(@RequestParam(value = "page", defaultValue = "0") int page,
                           @RequestParam(value = "size", defaultValue = "10") int size,
                           Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        model.addAttribute("username", username);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Posts> paginatedPosts = postService.getPaginatedPosts(pageable);

        Set<String> allAuthors = postService.findAllAuthors();
        Set<String> allTags = tagService.findAllTags();
        Set<String> allDatesAndTime = postService.findAllDates();

        model.addAttribute("posts", paginatedPosts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedPosts.getTotalPages());
        model.addAttribute("pageSize", paginatedPosts.getSize());

        model.addAttribute("authors", allAuthors);
        model.addAttribute("tags", allTags);
        model.addAttribute("publishedDates", allDatesAndTime);

        return "posts/list";
    }

    @GetMapping("/create")
    public String createPostForm(Model model) {
        model.addAttribute("post", new Posts());
        return "posts/create";
    }

    @PostMapping("/save")
    public String savePost(@ModelAttribute Posts post, @RequestParam String tagNames) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(post.getAuthor()==null){
            post.setAuthor(username);
        }
        LocalDateTime date = LocalDateTime.now();
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        post.setPublishedAt(formattedDate);
        post.setUpdatedAt(LocalDateTime.now());

        if (tagNames != null && !tagNames.trim().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            String[] tagArray = tagNames.split(",");
            for (String tagName : tagArray) {
                Tag tag = tagService.getTagByName(tagName);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setCreatedAt(LocalDateTime.now());
                    tag.setUpdatedAt(LocalDateTime.now());
                    tagService.save(tag);
                }
                tags.add(tag);
            }
            post.setTags(tags);
        }
        postService.create(post);

        return "redirect:/posts";
    }

    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable Integer id, Model model) {
        Posts post = postService.getPostById(id);
        if (post != null) {
            StringBuilder tagsBuilder = new StringBuilder();
            for (Tag tag : post.getTags()) {
                if (!tagsBuilder.isEmpty()) {
                    tagsBuilder.append(", ");
                }
                tagsBuilder.append(tag.getName());
            }
            String tagsString = tagsBuilder.toString();

            model.addAttribute("post", post);
            model.addAttribute("tagsString", tagsString);
            return "posts/create";
        }
        return "/posts";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Integer id, @ModelAttribute Posts post, @RequestParam String tagNames, @RequestParam("postId") Integer postId) {
        Posts existingPost = postService.getPostById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if (existingPost != null) {
            existingPost.setTitle(post.getTitle());
            existingPost.setExcerpt(post.getExcerpt());
            existingPost.setAuthor(username);
            existingPost.setContent(post.getContent());
            existingPost.setUpdatedAt(LocalDateTime.now());

            Set<Tag> updatedTags = new HashSet<>();

            for (String tagName : tagNames.split(",")) {
                Tag tag = tagService.getTagByName(tagName);
                if (tag == null) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tag.setCreatedAt(LocalDateTime.now());
                    tag.setUpdatedAt(LocalDateTime.now());
                    tag = tagService.save(tag);
                }
                updatedTags.add(tag);
            }

            existingPost.setTags(updatedTags);
            postService.update(existingPost);

            return "redirect:/posts/view/" + postId;
        }
        return "redirect:/posts";
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Integer id) {
        postService.deleteById(id);
        return "redirect:/posts";
    }

    @GetMapping("/view/{id}")
    public String viewPost(@PathVariable Integer id, Model model) {
        Posts post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "posts/view";
    }

    @GetMapping("/sort")
    public String sortPosts(@RequestParam(value = "sort", required = false) String sort,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "10") int size,
                            Model model) {

        Pageable pageable;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username of the logged-in user

        // Add the username to the model
        model.addAttribute("username", username);

        if ("asc".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "publishedAt"));
        } else if ("desc".equalsIgnoreCase(sort)) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        } else {
            pageable = PageRequest.of(page, size);
        }

        Page<Posts> postPage = postService.getPaginatedPosts(pageable);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        model.addAttribute("sort", sort);

        Set<String> allAuthors = postService.findAllAuthors();
        Set<String> allTags = tagService.findAllTags();
        Set<String> allDatesAndTime = postService.findAllDates();

        model.addAttribute("authors", allAuthors);
        model.addAttribute("tags", allTags);
        model.addAttribute("publishedDates", allDatesAndTime);

        return "posts/list";
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("keyword") String keyword, Model model,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username of the logged-in user

        // Add the username to the model
        model.addAttribute("username", username);

        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> paginatedPosts = postService.searchPosts(keyword, pageable);
        Set<String> allAuthors = postService.findAllAuthors();
        Set<String> allTags = tagService.findAllTags();
        Set<String> allDatesAndTime = postService.findAllDates();

        model.addAttribute("authors", allAuthors);
        model.addAttribute("tags", allTags);
        model.addAttribute("publishedDates", allDatesAndTime);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedPosts.getTotalPages());
        model.addAttribute("pageSize", paginatedPosts.getSize());

        List<Posts> posts = paginatedPosts.getContent();
        model.addAttribute("posts", posts);
        return "posts/list";
    }

    @GetMapping("/filter")
    public String filterPosts(@RequestParam(value = "authors", required = false) Set<String> authors,
                              @RequestParam(value = "tags", required = false) Set<String> tags,
                              @RequestParam(value = "dates", required = false) Set<String> dates,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        model.addAttribute("username", username);

        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> paginatedPosts = postService.filterPosts(authors, tags, dates, pageable);

        Set<String> allAuthors = postService.findAllAuthors();
        Set<String> allTags = tagService.findAllTags();
        Set<String> allDatesAndTime = postService.findAllDates();

        model.addAttribute("authors", allAuthors);
        model.addAttribute("tags", allTags);
        model.addAttribute("publishedDates", allDatesAndTime);

        model.addAttribute("posts", paginatedPosts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedPosts.getTotalPages());
        model.addAttribute("pageSize", paginatedPosts.getSize());

        return "posts/list";
    }
}
