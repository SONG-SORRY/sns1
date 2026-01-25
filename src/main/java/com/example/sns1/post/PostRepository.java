package com.example.sns1.post;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.sns1.user.UserData;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByIdDesc();

    @Modifying
    @Query("update Post p set p.author = null where p.author = :author")
    void updateAuthorToNull(@Param("author") UserData author);
}
