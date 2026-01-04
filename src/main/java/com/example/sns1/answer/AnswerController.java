package com.example.sns1.answer;

import com.example.sns1.post.Post;
import com.example.sns1.post.PostService;
import com.example.sns1.user.UserData;
import com.example.sns1.user.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.HashMap;

import java.security.Principal;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final PostService postService;
    private final AnswerService answerService;
    private final UserService userService;

        @PreAuthorize("isAuthenticated()")
        @PostMapping("/api/answer/create/{id}")
        @ResponseBody
        public ResponseEntity<Map<String, Object>> createAnswerApi(
                @PathVariable("id") Long id,
                @RequestParam("content") String content,
                Principal principal) {
            
                Post post = this.postService.getPost(id);
                UserData userData = this.userService.getUser(principal.getName());

                Answer answer = this.answerService.create(post, content, userData);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDate = answer.getCreateDate().format(formatter);

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("answerId", answer.getId());
                response.put("content", answer.getContent());
                response.put("username", answer.getAuthor().getUsername());
                response.put("createDate", formattedDate);

                return ResponseEntity.ok(response);
            }
    
}