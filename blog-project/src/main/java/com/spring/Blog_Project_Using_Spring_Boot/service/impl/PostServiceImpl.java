package com.spring.Blog_Project_Using_Spring_Boot.service.impl;

import com.spring.Blog_Project_Using_Spring_Boot.model.Posts;
import com.spring.Blog_Project_Using_Spring_Boot.model.Tag;
import com.spring.Blog_Project_Using_Spring_Boot.repository.PostsRepository;
import com.spring.Blog_Project_Using_Spring_Boot.repository.TagRepository;
import com.spring.Blog_Project_Using_Spring_Boot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostsRepository postsRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostsRepository postsRepository, TagRepository tagRepository) {
        this.postsRepository = postsRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void create(Posts posts) {
        postsRepository.save(posts);
    }

    @Override
    public List<Posts> getAllPosts() {
        return postsRepository.findAll();
    }

    @Override
    public Posts getPostById(Integer id) {
        return postsRepository.findById(id).get();
    }

    @Override
    public void update(Posts post) {
        postsRepository.save(post);
    }

    @Override
    public void deleteById(Integer id) {
        postsRepository.deleteById(id);
        }

    @Override
    public Page<Posts> searchPosts(String keyword,Pageable pageable) {
        return postsRepository.searchPosts(keyword,pageable);
    }

    @Override
    public Set<String> findAllAuthors() {
        return postsRepository.findAllAuthors();
    }

    @Override
    public Set<String> findAllDates() {
        return postsRepository.getAllDates();
    }

    @Override
    public Page<Posts> filterPosts(Set<String> authors, Set<String> tags, Set<String> dates, Pageable pageable) {
        return postsRepository.findPostsByAuthorsAndTagsAndDates(authors, tags, dates, pageable);
    }

    @Override
    public Page<Posts> getPaginatedPosts(Pageable pageable) {
        return postsRepository.findAll(pageable);
    }
}