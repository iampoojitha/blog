package com.spring.Blog_Project_Using_Spring_Boot.service.impl;

import com.spring.Blog_Project_Using_Spring_Boot.model.Tag;
import com.spring.Blog_Project_Using_Spring_Boot.repository.TagRepository;
import com.spring.Blog_Project_Using_Spring_Boot.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public Set<String> findAllTags() {
        return tagRepository.findAllTags();
    }
}
