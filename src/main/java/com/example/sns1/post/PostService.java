package com.example.sns1.post;

import com.example.sns1.user.UserData;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public List<Post> getList() {
        return this.postRepository.findAllByOrderByIdDesc();
    }

    public Post create(String content, UserData user) {
        Post post = new Post();
        post.setContent(content);
        post.setCreateDate(LocalDateTime.now());
        post.setAuthor(user);
        return this.postRepository.save(post);
    }

    public Post getPost(Integer id) {
        Optional<Post> post = this.postRepository.findById(Long.valueOf(id));
        
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new RuntimeException("Post not found");
        }
    }
}
