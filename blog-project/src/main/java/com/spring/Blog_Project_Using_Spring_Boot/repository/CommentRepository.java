package com.spring.Blog_Project_Using_Spring_Boot.repository;

import com.spring.Blog_Project_Using_Spring_Boot.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
