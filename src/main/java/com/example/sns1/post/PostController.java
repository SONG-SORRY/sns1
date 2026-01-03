package com.example.sns1.post;

import com.example.sns1.answer.AnswerForm;
import com.example.sns1.user.UserData;
import com.example.sns1.user.UserService;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import java.time.format.DateTimeFormatter;


import java.security.Principal;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String list(Model model, PostForm postForm, AnswerForm answerForm) {
        List<Post> postList = this.postService.getList();
        model.addAttribute("postList", postList);
        return "mainpage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/post/create")
    @ResponseBody 
    public ResponseEntity<Map<String, Object>> createPostApi(
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Principal principal) {
        
        Map<String, Object> response = new HashMap<>();

        try {
            UserData userData = this.userService.getUser(principal.getName());
            
            Post savedPost = this.postService.create(content, userData, file);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = savedPost.getCreateDate().format(formatter);

            response.put("status", "success");
            response.put("postId", savedPost.getId());
            response.put("username", savedPost.getAuthor().getUsername());
            response.put("createDate", formattedDate);
            response.put("content", savedPost.getContent());
            response.put("imgUrl", savedPost.getImgUrl());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }
}