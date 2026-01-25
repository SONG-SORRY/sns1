package com.example.sns1.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.sns1.user.UserData;

public interface AnswerRepository extends JpaRepository<Answer, Long>{
    @Modifying
    @Query("update Answer a set a.author = null where a.author = :author")
    void updateAuthorToNull(@Param("author") UserData author);
}
