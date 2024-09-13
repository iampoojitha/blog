package com.spring.Blog_Project_Using_Spring_Boot.service;

import com.spring.Blog_Project_Using_Spring_Boot.model.Comment;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
   public void saveComment(Comment comment);

   public Comment getCommentById(Integer id);

   public void update(Comment comment);

   public void deleteById(Integer id);
}
