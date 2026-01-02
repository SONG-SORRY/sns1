package com.example.sns1.post;

import com.example.sns1.answer.AnswerForm;
import com.example.sns1.user.UserData;
import com.example.sns1.user.UserService;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.security.Principal;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;


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
    @PostMapping("/")
    public String createPost(@Valid PostForm postForm, BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            List<Post> postList = this.postService.getList();
            model.addAttribute("postList", postList);
            return "mainpage";
        }
        UserData userData = this.userService.getUser(principal.getName());
        this.postService.create(postForm.getContent(), userData);
        return "redirect:/";
    }
}