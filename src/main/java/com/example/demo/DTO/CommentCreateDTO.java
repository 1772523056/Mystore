package com.example.demo.DTO;

import com.example.demo.model.User;
import lombok.Data;

@Data
public class CommentCreateDTO {
    private Integer parentId;
    private String content;
    private Integer type;
    private Integer commentator;
    private Integer commentId;
}
