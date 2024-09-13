package com.spring.Blog_Project_Using_Spring_Boot.service.impl;

import com.spring.Blog_Project_Using_Spring_Boot.model.Comment;
import com.spring.Blog_Project_Using_Spring_Boot.repository.CommentRepository;
import com.spring.Blog_Project_Using_Spring_Boot.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(Integer id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public void update(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }
}
