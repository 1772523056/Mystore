package com.example.demo.controller;

import com.example.demo.DTO.CommentCreateDTO;
import com.example.demo.DTO.CommentDTO;
import com.example.demo.DTO.ResultDTO;
import com.example.demo.enums.CommentTypeEnums;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.model.UserExample;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserMapper userMapper;



    @GetMapping("/commentlikes/{commentId}/{questionId}")
    public String question(@PathVariable(name="commentId") Integer commentId,
                           @PathVariable(name="questionId") Integer questionId,
                           Model model,
                           HttpServletRequest request) {
        String token = null;
        User user = null;
        if (request.getCookies () != null) {
            Cookie[] cookies = request.getCookies ();
            for (Cookie cookie : cookies) {
                if (cookie.getName ().equals ("Token")) {
                    token = cookie.getValue ();
                    UserExample userExample = new UserExample ();
                    userExample.createCriteria ()
                            .andTokenEqualTo (token);
                    List<User> users = userMapper.selectByExample (userExample);
                    if (users.size () != 0) {
                        request.getSession ().setAttribute ("user", users.get (0));

                    }
                    break;
                }
            }
        }
        user = (User) request.getSession ().getAttribute ("user");
        if (user == null) {
            model.addAttribute ("error", "用户未登陆");
            return "redirect:/question/"+questionId;
        }
        commentService.updateLikeCount (commentId);
        return "redirect:/question/"+questionId;

    }
    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post (@RequestBody CommentCreateDTO commentCreateDTO,
                        HttpServletRequest request) {
        User user = (User) request.getSession ().getAttribute ("user");
        if (user == null) {
            return ResultDTO.errorOf (CustomizeErrorCode.NO_LOG_IN);
        }
        if (commentCreateDTO.getContent () == null || commentCreateDTO.getContent ().length () == 0) {
            return ResultDTO.errorOf (CustomizeErrorCode.CONTENT_EMPTY);
        }
        Comment comment = new Comment ();
        if (commentCreateDTO.getContent () != null) {
            comment.setContent (commentCreateDTO.getContent ());
            comment.setParentId (commentCreateDTO.getParentId ());
            comment.setTimeCreate (System.currentTimeMillis ());
            comment.setTimeModify (System.currentTimeMillis ());
            comment.setType (commentCreateDTO.getType ());
            comment.setCommentator (user.getId ());
            comment.setLikeCount (commentCreateDTO.getLikeCount ());
            comment.setCommentCount (0);
            commentService.insert (comment, user);
        }else{

        }
        return ResultDTO.okOf ();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments (@PathVariable(name = "id") Integer id) {
        List<CommentDTO> subCommentDTOList = commentService.listByTargetId (id, CommentTypeEnums.COMMENT);
        return ResultDTO.okOf (subCommentDTOList);
    }
}
