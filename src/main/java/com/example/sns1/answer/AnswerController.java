package com.example.sns1.answer;

import com.example.sns1.post.Post;
import com.example.sns1.post.PostForm;
import com.example.sns1.post.PostService;
import com.example.sns1.user.UserData;
import com.example.sns1.user.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.HashMap;

import java.security.Principal;
import java.time.format.DateTimeFormatter;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final PostService postService;
    private final AnswerService answerService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/answer/create/{id}")
    public String createAnswer(@PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Model model, Principal principal) {
        
        Post post = this.postService.getPost(id);
        UserData userData = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("postList", this.postService.getList());
            model.addAttribute("postForm", new PostForm());
            return "mainpage";
        }
        this.answerService.create(post, answerForm.getContent(), userData);

        return "redirect:/";
    }

        @PreAuthorize("isAuthenticated()")
        @PostMapping("/api/answer/create/{id}")
        @ResponseBody
        public ResponseEntity<Map<String, Object>> createAnswerApi(
                @PathVariable("id") Integer id,
                @RequestParam("content") String content,
                Principal principal) {
            
                Post post = this.postService.getPost(id);
                UserData userData = this.userService.getUser(principal.getName());

                Answer answer = this.answerService.create(post, content, userData);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDate = answer.getCreateDate().format(formatter);

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("content", answer.getContent());
                response.put("username", answer.getAuthor().getUsername());
                response.put("createDate", formattedDate);
                response.put("answerId", answer.getId());

                return ResponseEntity.ok(response);
            }
    
}