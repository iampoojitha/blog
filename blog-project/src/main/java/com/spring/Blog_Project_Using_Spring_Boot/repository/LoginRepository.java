package com.spring.Blog_Project_Using_Spring_Boot.repository;

import com.spring.Blog_Project_Using_Spring_Boot.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<Users,Integer> {
    @Query("SELECT u.name FROM Users u")
    public List<String> userNames();

    @Query("SELECT u.email FROM Users u")
    public List<String> userEmails();
}
