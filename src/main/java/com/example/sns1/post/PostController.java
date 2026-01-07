package com.example.sns1.post;

import com.example.sns1.answer.Answer;
import com.example.sns1.user.UserData;
import com.example.sns1.user.UserService;

import java.util.Map;
import java.util.ArrayList;
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
    public String mainpage(Model model) {
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

        if (content == null || content.trim().isEmpty()) {
            response.put("status", "error");
            response.put("message", "내용을 입력해주세요.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            UserData userData = this.userService.getUser(principal.getName());
            
            Post savedPost = this.postService.create(content, userData, file);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = savedPost.getCreateDate().format(formatter);

            response.put("status", "success");
            response.put("postId", savedPost.getId());
            response.put("content", savedPost.getContent());
            response.put("username", savedPost.getAuthor().getUsername());
            response.put("createDate", formattedDate);
            response.put("imgUrl", savedPost.getImgUrl());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/posts")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getPostListApi() {
        
        List<Post> postList = this.postService.getList();
        
        List<Map<String, Object>> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Post post : postList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", post.getId());
            map.put("content", post.getContent());
            
            if (post.getCreateDate() != null) {
                map.put("createDate", post.getCreateDate().format(formatter));
            } else {
                map.put("createDate", "");
            }

            map.put("imgUrl", post.getImgUrl());

            Map<String, Object> authorMap = new HashMap<>();
            if (post.getAuthor() != null) {
                authorMap.put("id", post.getAuthor().getId());
                authorMap.put("username", post.getAuthor().getUsername());
            } else {
                authorMap.put("username", "알 수 없음");
            }
            map.put("author", authorMap);

            List<Map<String, Object>> answerList = new ArrayList<>();
            if (post.getAnswerList() != null) {
                for (Answer answer : post.getAnswerList()) {
                    Map<String, Object> answerMap = new HashMap<>();
                    answerMap.put("id", answer.getId());
                    answerMap.put("content", answer.getContent());
                    if (answer.getAuthor() != null) {
                        Map<String, Object> answerAuthor = new HashMap<>();
                        answerAuthor.put("username", answer.getAuthor().getUsername());
                        answerMap.put("author", answerAuthor);
                    }
                    
                    if (answer.getCreateDate() != null) {
                         answerMap.put("createDate", answer.getCreateDate().format(formatter));
                    }
                    answerList.add(answerMap);
                }
            }
            map.put("answerList", answerList);

            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
}