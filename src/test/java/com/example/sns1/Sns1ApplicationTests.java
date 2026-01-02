package com.example.sns1;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.sns1.post.PostRepository;
import com.example.sns1.post.Post;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class Sns1ApplicationTests {

	@Autowired
    private PostRepository postRepository;

	@Test
	void contextLoads() {
		Post p1 = new Post();
		p1.setContent("안녕하세요.");
		p1.setCreateDate(LocalDateTime.now());
		this.postRepository.save(p1);

		Post p2 = new Post();
		p2.setContent("고맙습니다.");
		p2.setCreateDate(LocalDateTime.now());
		this.postRepository.save(p2);
	}

}
