package com.spring.Blog_Project_Using_Spring_Boot.repository;

import com.spring.Blog_Project_Using_Spring_Boot.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByName(String name);

    @Query("SELECT DISTINCT t.name FROM Tag t")
    Set<String> findAllTags();

}
