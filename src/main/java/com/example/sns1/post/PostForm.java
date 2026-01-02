package com.example.sns1.post;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostForm {
    
    @NotEmpty
    private String content;
}
