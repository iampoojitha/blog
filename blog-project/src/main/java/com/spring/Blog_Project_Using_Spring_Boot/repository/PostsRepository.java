package com.spring.Blog_Project_Using_Spring_Boot.repository;

import com.spring.Blog_Project_Using_Spring_Boot.model.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Integer> {

    @Query("SELECT p FROM Posts p JOIN p.tags t WHERE p.title LIKE %:keyword% OR p.author LIKE %:keyword% OR p.content LIKE %:keyword% OR t.name LIKE %:keyword% ")
    Page<Posts> searchPosts(@Param("keyword") String keyword,Pageable pageable);

    @Query(value = "SELECT DISTINCT LOWER(p.author) FROM Posts p", nativeQuery = true)
    Set<String> findAllAuthors();

    @Query("SELECT DISTINCT FUNCTION('DATE', p.publishedAt) FROM Posts p")
    Set<String> getAllDates();

    @Query("SELECT DISTINCT p FROM Posts p " +
            "LEFT JOIN p.tags t " +
            "WHERE (:authors IS NULL OR LOWER(p.author) IN :authors) " +
            "AND (:tags IS NULL OR t.name IN :tags) " +
            "AND (:dates IS NULL OR SUBSTRING(p.publishedAt, 1, 10) IN :dates)")
    Page<Posts> findPostsByAuthorsAndTagsAndDates(@Param("authors") Set<String> authors,
                                                  @Param("tags") Set<String> tags,
                                                  @Param("dates") Set<String> dates,
                                                  Pageable pageable);
}

