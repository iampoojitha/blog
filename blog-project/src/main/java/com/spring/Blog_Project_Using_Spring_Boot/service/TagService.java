package com.spring.Blog_Project_Using_Spring_Boot.service;

import com.spring.Blog_Project_Using_Spring_Boot.model.Tag;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface TagService {
    Tag save(Tag tag);

    public Tag getTagByName(String name);

    public Set<String> findAllTags();

}
