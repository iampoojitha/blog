package com.spring.Blog_Project_Using_Spring_Boot.service;

import com.spring.Blog_Project_Using_Spring_Boot.model.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface PostService {

    public void create(Posts posts);

    public List<Posts> getAllPosts();

    public Posts getPostById(Integer id);

    public void update(Posts post);

    public void deleteById(Integer id);

    public Page<Posts> searchPosts(String keyword,Pageable pageable);

    public Set<String> findAllAuthors();

    public Set<String> findAllDates();

    public Page<Posts> filterPosts(Set<String> authors, Set<String> tags, Set<String> dates, Pageable pageable);


    public Page<Posts> getPaginatedPosts(Pageable pageable);


}

