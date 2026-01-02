package com.example.sns1.answer;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {
    
    @NotEmpty
    private String content;
}
